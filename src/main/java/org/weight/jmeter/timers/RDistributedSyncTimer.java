package org.weight.jmeter.timers;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.timers.Timer;
import org.apache.jmeter.timers.TimerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RDistributedSyncTimer extends AbstractTestElement implements Timer, Serializable, TestBean, TestStateListener, ThreadListener {
    private static final Logger log = LoggerFactory.getLogger(RDistributedSyncTimer.class);

    /**
     * Wrapper to {@link CyclicBarrier} to allow lazy init of CyclicBarrier when SyncTimer is configured with 0
     */
    private static class BarrierWrapper implements Cloneable {

        private CyclicBarrier barrier;
        private Jedis jedis = null;
        private Thread subscribeThread = null;

        /**
         *
         */
        public BarrierWrapper() {
            this.barrier = null;
        }

        /**
         * Synchronized is required to ensure CyclicBarrier is initialized only once per Thread Group
         * @param parties Number of parties
         */
        public synchronized void setup(String subscribedServerIpAddress, int subscribedServerIpPort, String channelName, int parties) {
            if (this.barrier == null) {
                this.barrier = new CyclicBarrier(parties);
                //Subscribe thread, if onMessage is reset, reset and broke the barrier
                subscribeThread = new Thread(() -> {
                    jedis = new Jedis(subscribedServerIpAddress, subscribedServerIpPort);
                    try{
                        jedis.subscribe(new JedisPubSub() {
                            @Override
                            public void onMessage(String channel, String message) {
                                super.onMessage(channel, message);
                                if (message.equals("reset")) {
                                    Date now = new Date( );
                                    SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
                                    System.out.println("Aggregate released at" + format.format(now));
                                    reset();
                                }
                            }
                        }, channelName);
                    } catch (Exception e){
                        System.out.println("subscribeThread is stop...");
                    }
                });
                subscribeThread.start();

                System.out.println("subscribeThread.start()");
            }
        }


        /**
         * Wait until all threads called await on this timer
         *
         * @return The arrival index of the current thread
         * @throws InterruptedException
         *             when interrupted while waiting, or the interrupted status
         *             is set on entering this method
         * @throws BrokenBarrierException
         *             if the barrier is reset while waiting or broken on
         *             entering or while waiting
         * @see java.util.concurrent.CyclicBarrier#await()
         */
        public int await() throws InterruptedException, BrokenBarrierException{
            return barrier.await();
        }

        /**
         * Wait until all threads called await on this timer
         *
         * @param timeout
         *            The timeout in <code>timeUnit</code> units
         * @param timeUnit
         *            The time unit for the <code>timeout</code>
         * @return The arrival index of the current thread
         * @throws InterruptedException
         *             when interrupted while waiting, or the interrupted status
         *             is set on entering this method
         * @throws BrokenBarrierException
         *             if the barrier is reset while waiting or broken on
         *             entering or while waiting
         * @throws TimeoutException
         *             if the specified time elapses
         * @see java.util.concurrent.CyclicBarrier#await()
         */
        public int await(long timeout, TimeUnit timeUnit) throws InterruptedException, BrokenBarrierException, TimeoutException {
            return barrier.await(timeout, timeUnit);
        }

        /**
         * @see java.util.concurrent.CyclicBarrier#reset()
         */
        public void reset() {
            barrier.reset();
        }

        /**
         * @see java.lang.Object#clone()
         */
        @Override
        protected Object clone()  {
            BarrierWrapper barrierWrapper=  null;
            try {
                barrierWrapper = (BarrierWrapper) super.clone();
                barrierWrapper.barrier = this.barrier;
                barrierWrapper.subscribeThread = this.subscribeThread;
                barrierWrapper.jedis = this.jedis;
            } catch (CloneNotSupportedException e) {
                //Cannot happen
                e.printStackTrace();
            }
            return barrierWrapper;
        }
    }

    private static final long serialVersionUID = 3;

    private transient BarrierWrapper barrier;

    private int groupSize;
    private long timeoutInMs;
    private String channelName;
    private String subscribedServerIpAddress;
    private int subscribedServerIpPort;

    // Ensure transient object is created by the server
    private Object readResolve(){
        createBarrier();
        return this;
    }

    /**
     * @return Returns the numThreads.
     */
    public int getGroupSize() {
        return groupSize;
    }

    /**
     * @param numThreads
     *            The numThreads to set.
     */
    public void setGroupSize(int numThreads) {
        this.groupSize = numThreads;
    }

    public String getChannelName() {
        if (channelName.trim().equals("")) {
            channelName = "default";
        }
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getSubscribedServerIpAddress() {
        return subscribedServerIpAddress;
    }

    public void setSubscribedServerIpAddress(String subscribedServerIpAddress) {
        this.subscribedServerIpAddress = subscribedServerIpAddress;
    }

    public int getSubscribedServerIpPort() {
        return subscribedServerIpPort;
    }

    public void setSubscribedServerIpPort(int subscribedServerIpPort) {
        this.subscribedServerIpPort = subscribedServerIpPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long delay() {
        Jedis jedis = null;
        String script = "if redis.call('exists',KEYS[1]) == 0 then return redis.call('set', KEYS[1], '0') else return 2 end";
        String key = getChannelName() + "-key";
        if (getGroupSize() >= 0) {
            try {
                jedis = new Jedis(getSubscribedServerIpAddress(), getSubscribedServerIpPort());

                jedis.eval(script, 1, key);

                if (timeoutInMs == 0) {
                    long arrival = jedis.incr(key);

                    System.out.println("group:" + getGroupSize());
                    System.out.println(arrival);

                    if (arrival == getGroupSize()) {
                        jedis.set(key, "0");
                        jedis.publish(getChannelName(), "reset");
                    } else {
                        this.barrier.await(TimerService.getInstance().adjustDelay(Long.MAX_VALUE), TimeUnit.MILLISECONDS);
                    }

                } else if (timeoutInMs > 0) {
                    long arrival = jedis.incr(key);
                    if (arrival == getGroupSize()) {
                        jedis.set(key, "0");
                        jedis.publish(getChannelName(), "reset");
                    } else {
                        this.barrier.await(TimerService.getInstance().adjustDelay(timeoutInMs), TimeUnit.MILLISECONDS);
                    }

                } else {
                    throw new IllegalArgumentException("Negative value for timeout:" + timeoutInMs + " in Synchronizing Timer " + getName());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return 0;
            } catch (BrokenBarrierException e) {
                return 0;
            } catch (TimeoutException e) {
                if (log.isWarnEnabled()) {
                    log.warn("SyncTimer {} timeouted waiting for users after: {}ms", getName(), getTimeoutInMs());
                }
                return 0;
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        return 0;
    }

    /**
     * We have to control the cloning process because we need some cross-thread
     * communication if our synctimers are to be able to determine when to block
     * and when to release.
     */
    @Override
    public Object clone() {
        RDistributedSyncTimer newTimer = (RDistributedSyncTimer) super.clone();
        newTimer.barrier = barrier;
        newTimer.groupSize = groupSize;
        newTimer.timeoutInMs = timeoutInMs;
        newTimer.subscribedServerIpAddress = subscribedServerIpAddress;
        newTimer.subscribedServerIpPort = subscribedServerIpPort;
        newTimer.channelName = channelName;
        return newTimer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testEnded() {
        this.testEnded(null);
    }

    /**
     * Reset timerCounter
     */
    @Override
    public void testEnded(String host) {
        String key = getChannelName() + "-key";
        this.barrier.subscribeThread.interrupt();
        this.barrier.jedis.close();
        Jedis jedis = new Jedis(getSubscribedServerIpAddress(), getSubscribedServerIpPort());
        jedis.del(key);
        jedis.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testStarted() {
        testStarted(null);
    }

    /**
     * Reset timerCounter
     */
    @Override
    public void testStarted(String host) {
        createBarrier();
    }

    /**
     *
     */
    private void createBarrier() {
        this.barrier = new BarrierWrapper();
    }

    @Override
    public void threadStarted() {

        int parties = getGroupSize() == 0 ? JMeterContextService.getContext().getThreadGroup().getNumThreads() : getGroupSize();

        // Unique Barrier creation ensured by synchronized setup
        this.barrier.setup(getSubscribedServerIpAddress(), getSubscribedServerIpPort(), getChannelName(), parties);

    }

    @Override
    public void threadFinished() {
        // NOOP
    }

    /**
     * @return the timeoutInMs
     */
    public long getTimeoutInMs() {
        return timeoutInMs;
    }

    /**
     * @param timeoutInMs the timeoutInMs to set
     */
    public void setTimeoutInMs(long timeoutInMs) {
        this.timeoutInMs = timeoutInMs;
    }
}
