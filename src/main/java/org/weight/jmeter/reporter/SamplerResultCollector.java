package org.weight.jmeter.reporter;

import java.io.Serializable;

import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.reporters.AbstractListenerElement;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.Remoteable;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestStateListener;

public class SamplerResultCollector extends AbstractListenerElement implements SampleListener, Clearable, Serializable,
												TestStateListener, Remoteable, NoThreadClone {
	
	public boolean isSampleWanted(boolean success){
		if(success){
			return true;
		}
		return false;
    }
	
	@Override
	public void testEnded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testEnded(String host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testStarted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testStarted(String host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sampleOccurred(SampleEvent event) {
		SampleResult result = event.getResult();
		if(isSampleWanted(result.isSuccessful())){
			long during = result.getEndTime() - result.getStartTime();
			System.out.println(result.getSampleLabel() + ":" + during + "ms");
		}
		
	}

	@Override
	public void sampleStarted(SampleEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sampleStopped(SampleEvent event) {
		// TODO Auto-generated method stub
		
	}

}
