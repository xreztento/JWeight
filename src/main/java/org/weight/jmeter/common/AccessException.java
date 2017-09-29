package org.weight.jmeter.common;
public class AccessException extends Exception{
	@Override
	public String getMessage(){
		return "accessor has been closed!";
	}
}
