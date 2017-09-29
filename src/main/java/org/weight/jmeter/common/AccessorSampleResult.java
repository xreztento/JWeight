package org.weight.jmeter.common;

import org.apache.jmeter.samplers.SampleResult;

public class AccessorSampleResult extends SampleResult{
	
	protected SampleResultObject resultObj = null;
	protected final long ts;
	
	public AccessorSampleResult(){
		ts = System.currentTimeMillis();
	}
	
	public SampleResultObject getSampleResultObject() {
		return this.resultObj;
	}

	public void setSampleResultObject(SampleResultObject resultObj) {
		this.resultObj = resultObj;
		setStartTime(ts);
	}

}
