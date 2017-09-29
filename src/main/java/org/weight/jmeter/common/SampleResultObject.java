package org.weight.jmeter.common;

public abstract class SampleResultObject {
	protected Object resultObj = null;
	
	public abstract Object get();
	public abstract void init();
	
	public void set(Object object){//��ֵ
		this.resultObj = object;
	}
	
}
