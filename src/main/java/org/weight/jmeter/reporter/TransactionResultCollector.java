package org.weight.jmeter.reporter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.reporters.AbstractListenerElement;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.Remoteable;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.weight.jmeter.samplers.TransactionBeginSampler;
import org.weight.jmeter.samplers.TransactionSampleResult;

public class TransactionResultCollector extends AbstractListenerElement implements SampleListener, Clearable, Serializable,
												TestStateListener, Remoteable, NoThreadClone {
	private TransactionTimeComputer ttc = new TransactionTimeComputer();
	public TransactionResultCollector(TransactionTimeComputer ttc){
		super();
		this.ttc = ttc;
	}
	
	@Override
	public TransactionResultCollector clone(){
		TransactionResultCollector collector = new TransactionResultCollector(ttc);
		collector.ttc = ttc;
		return collector;
		
	}
	
	public boolean isSampleWanted(SampleResult result){
		if((result instanceof TransactionSampleResult)){
			return true;
		}
		return false;
    }
	
	@Override
	public void testEnded() {
		TransactionBeginSampler.startTimeMap.clear();
		ttc.reset();
	}

	@Override
	public void testEnded(String host) {
		TransactionBeginSampler.startTimeMap.clear();
		ttc.reset();
	}

	@Override
	public void testStarted() {
		TransactionBeginSampler.startTimeMap.clear();
		ttc.reset();
	}

	@Override
	public void testStarted(String host) {
		TransactionBeginSampler.startTimeMap.clear();
		ttc.reset();
	}

	@Override
	public void clearData() {
		
	}

	@Override
	public void sampleOccurred(SampleEvent event) {
		SampleResult result = event.getResult();
		if(isSampleWanted(result)){
			TransactionSampleResult tsr = (TransactionSampleResult)result;
			long during = tsr.getEndTime() - tsr.getStartTime();
			Map<String, Vector<Long>> map = ttc.getResult();
			if(map.get(tsr.getName()) != null){
				map.get(tsr.getName()).add(during);
				
			} else {
				Vector<Long> v = new Vector<Long>();
				v.add(during);
				map.put(tsr.getName(), v);
			}
			ttc.setResult(map);
		}
		
	}

	@Override
	public void sampleStarted(SampleEvent event) {
		
	}

	@Override
	public void sampleStopped(SampleEvent event) {
		
	}

}
