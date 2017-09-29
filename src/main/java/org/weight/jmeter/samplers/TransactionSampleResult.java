package org.weight.jmeter.samplers;

import org.apache.jmeter.samplers.SampleResult;

public class TransactionSampleResult extends SampleResult{
	private String name = null;
	
	public TransactionSampleResult(long start){
		setStartTime(start);
		setEndTime(System.currentTimeMillis());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
