package org.weight.jmeter.samplers;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;


public class TransactionEndSampler extends AbstractSampler {
	
	public final static String TRANS_NAME = "trans_name"; 
	@Override
	public SampleResult sample(Entry entry) {
		String name = this.getProperty(TRANS_NAME).toString().trim();
		String key = name + this.getThreadName();
		long startTime = TransactionBeginSampler.startTimeMap.get(key);
		TransactionSampleResult res = new TransactionSampleResult(startTime);
		res.setName(name);
		res.setSuccessful(true);
		return res;
	}
	
}
