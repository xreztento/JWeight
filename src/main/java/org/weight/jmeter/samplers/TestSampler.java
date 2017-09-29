package org.weight.jmeter.samplers;

import java.util.Random;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.processor.PreProcessor;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.ObjectProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.util.keystore.JmeterKeyStore;
import org.weight.jmeter.JMeterPluginUtils;

public class TestSampler extends AbstractSampler {
	
	public final static String NAME = "name"; 
	public final static String SLEEP = "sleep";
	@Override
	public SampleResult sample(Entry entry) {
		
		SampleResult res = new SampleResult();
		res.sampleStart();
		res.setSampleLabel(this.getPropertyAsString(NAME) + this.getThreadName());
		Random rand = new Random();
		
		try {
			long defaultValue = (rand.nextInt(5) + 1) * 1000;
			if(this.getPropertyAsString(SLEEP).trim().equals("")){
				Thread.sleep(defaultValue);
			} else {
				Thread.sleep(this.getPropertyAsLong(SLEEP, defaultValue));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		res.sampleEnd();
		res.setResponseCodeOK();
		res.setSuccessful(true);
		return res;
	}
	
	
	
}
