package org.weight.jmeter.samplers;

import java.util.HashMap;
import java.util.Map;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;

public class TransactionBeginSampler extends AbstractSampler{
	
	public final static String TRANS_NAME = "trans_name"; 
	public static Map<String, Long> startTimeMap = new HashMap<String, Long>();
	@Override
	public SampleResult sample(Entry entry) {
		// TODO Auto-generated method stub
		String name = this.getProperty(TRANS_NAME).toString().trim();
		long startTime = System.currentTimeMillis();
		String key = name + this.getThreadName();
		startTimeMap.put(key, startTime);
		return null;
	}

}
