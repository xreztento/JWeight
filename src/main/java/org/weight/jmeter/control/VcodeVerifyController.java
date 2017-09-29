package org.weight.jmeter.control;

import java.io.Serializable;

import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.control.NextIsNullException;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.IntegerProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;

public class VcodeVerifyController extends GenericController implements Serializable{
public static final int INFINITE_LOOP_COUNT = -1; // $NON-NLS-1$
    
    public static final String LOOPS = "VcodeVerifyController.loops"; // $NON-NLS-1$

    private static final long serialVersionUID = 7833960784370272300L;

   
    private static final String CONTINUE_FOREVER = "VcodeVerifyController.continue_forever"; // $NON-NLS-1$

    private transient int loopCount = 0;

    // Cache loop value, see Bug 54467
    private transient Integer nbLoops;

    public VcodeVerifyController() {
        setContinueForever_private(true);
    }

    public void setLoops(int loops) {
        setProperty(new IntegerProperty(LOOPS, loops));
    }

    public void setLoops(String loopValue) {
        setProperty(new StringProperty(LOOPS, loopValue));
    }

    public int getLoops() {
        // Evaluation occurs when nbLoops is not yet evaluated 
        // or when nbLoops is equal to special value INFINITE_LOOP_COUNT
        if(nbLoops==null || // No evaluated yet
                nbLoops.intValue()==0 || // Last iteration led to nbLoops == 0, 
                                         // in this case as resetLoopCount will not be called, 
                                         // it leads to no further evaluations if we don't evaluate, see BUG 56276
                nbLoops.intValue()==INFINITE_LOOP_COUNT // Number of iteration is set to infinite
                ) {
            try {
                JMeterProperty prop = getProperty(LOOPS);
                nbLoops = Integer.valueOf(prop.getStringValue());
            } catch (NumberFormatException e) {
                nbLoops = Integer.valueOf(0);
            }
        }
        return nbLoops.intValue();
    }
    
    public String getLoopString() {
        return getPropertyAsString(LOOPS);
    }

    /**
     * Determines whether the loop will return any samples if it is rerun.
     *
     * @param forever
     *            true if the loop must be reset after ending a run
     */
    public void setContinueForever(boolean forever) {
        setContinueForever_private(forever);
    }

    private void setContinueForever_private(boolean forever) {
        setProperty(new BooleanProperty(CONTINUE_FOREVER, forever));
    }

    private boolean getContinueForever() {
        return getPropertyAsBoolean(CONTINUE_FOREVER);
    }
    
    private final static String PATTERN = "34789ABCEFHKLPRTUVWXY";
    private boolean isVerify(String vcode){
    	int length = vcode.length();
    	if(length != 4){
    		return false;
    	}
    	for(int i = 0; i < length; i++){
    		if(PATTERN.indexOf(vcode.toCharArray()[i]) < 0){
    			return false;
        	}
    	}
    	
    	return true;
    }
    
    @Override
    public Sampler next() {
    	JMeterContext context = getThreadContext();
    	JMeterVariables var = context.getVariables();
    	String vcode = var.get("vcode");
    	if(vcode != null){
    		if(isVerify(vcode)){
        		setDone(true);
        		return null;
        	}
    	}
    	
    	
        if(endOfLoop()) {
            if (!getContinueForever()) {
                setDone(true);
            }
            return null;
        }
        return super.next();
    }

    private boolean endOfLoop() {
        final int loops = getLoops();
        return (loops > INFINITE_LOOP_COUNT) && (loopCount >= loops);
    }

    @Override
    protected void setDone(boolean done) {
        nbLoops = null;
        super.setDone(done);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Sampler nextIsNull() throws NextIsNullException {
        reInitialize();
        if (endOfLoop()) {
            if (!getContinueForever()) {
                setDone(true);
            } else {
                resetLoopCount();
            }
            return null;
        }
        return next();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void triggerEndOfLoop() {
        super.triggerEndOfLoop();
        resetLoopCount();
    }
    
    protected void incrementLoopCount() {
        loopCount++;
    }

    protected void resetLoopCount() {
        loopCount = 0;
        nbLoops = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getIterCount() {
        return loopCount + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reInitialize() {
        setFirst(true);
        resetCurrent();
        incrementLoopCount();
        recoverRunningVersion();
    }
    
    /**
     * Start next iteration
     */
    public void startNextLoop() {
        reInitialize();
    }
}
