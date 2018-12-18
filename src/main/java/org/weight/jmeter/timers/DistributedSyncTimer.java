package org.weight.jmeter.timers;

import java.io.Serializable;
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

public class DistributedSyncTimer extends AbstractTestElement implements Timer, Serializable, TestBean, TestStateListener, ThreadListener {
    private static final Logger log = LoggerFactory.getLogger(DistributedSyncTimer.class);

    /**
     * Wrapper to {@link CyclicBarrier} to allow lazy init of CyclicBarrier when DistributedSyncTimer is configured with 0
     */
    private static class BarrierWrapper {

        private CyclicBarrier barrier;
        private String channelName;
        private String subscribedServerIpAddress;
        private int subscribedServerIpPort;
        private Jedis jedis = null;
        private Thread subscribeThread = null;

        public BarrierWrapper(String subscribedServerIpAddress, int subscribedServerIpPort, String channelName) {
            this.barrier = null;
            this.subscribedServerIpAddress = subscribedServerIpAddress;
            this.subscribedServerIpPort = subscribedServerIpPort;
            this.channelName = channelName;

        }


        /**
         * Synchronized is required to ensure CyclicBarrier is initialized only once per Thread Group
         *
         * @param parties Number of parties
         */
        public synchronized void setup(int parties) {
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

        public int await() throws InterruptedException, BrokenBarrierException {
            return barrier.await();
        }

        public int await(long timeout, TimeUnit timeUnit) throws InterruptedException, BrokenBarrierException, TimeoutException {
            return barrier.await(timeout, timeUnit);
        }

        public void reset() {
            barrier.reset();
        }

//        @Override
//        protected Object clone() {
//            BarrierWrapper barrierWrapper = null;
//            try {
//                barrierWrapper = (BarrierWrapper) super.clone();
//                barrierWrapper.barrier = this.barrier;
//                barrierWrapper.subscribeThread = this.subscribeThread;
//                barrierWrapper.jedis = this.jedis;
////                barrierWrapper.subscribedServerIpAddress = subscribedServerIpAddress;
////                barrierWrapper.subscribedServerIpPort = subscribedServerIpPort;
////                barrierWrapper.channelName = channelName;
//                System.out.println(barrierWrapper);
//            } catch (CloneNotSupportedException e) {
//                //Cannot happen
//                e.printStackTrace();
//            }
//            return barrierWrapper;
//        }
    }

    private static final long serialVersionUID = 3;

    private transient BarrierWrapper barrier;


    private int groupSize;
    private long timeoutInMs;
    private String channelName;
    private String subscribedServerIpAddress;
    private int subscribedServerIpPort;

    // Ensure transient object is created by the server
    private Object readResolve() {
        createBarrier();
        return this;
    }

    /**
     * @return Returns the numThreads.
     */
    public int getGroupSize() {
        return groupSize;
    }


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
        if (getGroupSize() >= 0) {
            try {
                jedis = new Jedis(getSubscribedServerIpAddress(), getSubscribedServerIpPort());
                synchronized (DistributedSyncTimer.class) {
                    if (jedis.get(String.valueOf(this.barrier.hashCode())) == null) {
                        jedis.set(String.valueOf(this.barrier.hashCode()), "0");
                    }
                }

                if (timeoutInMs == 0) {
                    long arrival = jedis.incr(String.valueOf(this.barrier.hashCode()));
                    if (arrival == getGroupSize()) {
                        jedis.set(String.valueOf(this.barrier.hashCode()), "0");
                        jedis.publish(getChannelName(), "reset");
                    } else {
                        this.barrier.await(TimerService.getInstance().adjustDelay(Long.MAX_VALUE), TimeUnit.MILLISECONDS);
                    }

                } else if (timeoutInMs > 0) {
                    long arrival = jedis.incr(String.valueOf(this.barrier.hashCode()));
                    if (arrival == getGroupSize()) {
                        jedis.set(String.valueOf(this.barrier.hashCode()), "0");
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
//    @Override
//    public Object clone() {
//        DistributedSyncTimer newTimer = (DistributedSyncTimer) super.clone();
//        newTimer.barrier = barrier;
//        System.out.println("newTimer:" + newTimer);
//        return newTimer;
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testEnded() {
        this.testEnded(null);
    }

    /**
     * Reset env
     */
    @Override
    public void testEnded(String host) {
//        System.out.println("*******" + this.barrier.subscribeThread.getId());
//        System.out.println("*******" + this.barrier.jedis);
//        this.barrier.subscribeThread.interrupt();
//        System.out.println("interrupt:" + this.barrier.subscribeThread.isInterrupted());
//
//        this.barrier.jedis.close();
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
        // Lazy init
        this.barrier = new BarrierWrapper(getSubscribedServerIpAddress(), getSubscribedServerIpPort(), getChannelName());
    }

    @Override
    public void threadStarted() {
        if (getGroupSize() == 0) {
            int numThreadsInGroup = JMeterContextService.getContext().getThreadGroup().getNumThreads();
            this.groupSize = numThreadsInGroup;
            this.barrier.setup(numThreadsInGroup + 1);
        } else {
            this.barrier.setup(getGroupSize());
        }
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

