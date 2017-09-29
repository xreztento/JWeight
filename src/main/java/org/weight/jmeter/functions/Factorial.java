package org.weight.jmeter.functions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class Factorial extends AbstractFunction {
	
	private static final Logger log = LoggingManager.getLoggerForClass();
    private static final List<String> desc = new LinkedList<String>();
    private static final String KEY = "__factorial";
    private Object[] values = null;
    
    static {
        desc.add(JMeterUtils.getResString("factorial_value"));
    }
	@Override
	public List<String> getArgumentDesc() {
		// TODO Auto-generated method stub
		return desc;
	}
	@Override
	public String execute(SampleResult previousResult, Sampler currentSampler)
			throws InvalidVariableException {
		// TODO Auto-generated method stub
		String numberString = ((CompoundVariable) values[0]).execute().trim();
		int num;
		try{
			num = Integer.valueOf(numberString);
		} catch (Exception e){
			return null;
		}
		
		return String.valueOf(factorial(num));
	}
	@Override
	public String getReferenceKey() {
		// TODO Auto-generated method stub
		return KEY;
	}
	@Override
	public void setParameters(Collection<CompoundVariable> parameters)
			throws InvalidVariableException {
		// TODO Auto-generated method stub
		//checkMinParameterCount(parameters, 1);
		checkParameterCount(parameters, 1, 1);
        values = parameters.toArray();
	}
	
	private int factorial(int num){
		int result = 1;
		if(num == 0){
			result = 1;
		} else {
			for(int i = num; i > 0; i--){
				result *= i;
			}
		}
		
		return result;
	}
}