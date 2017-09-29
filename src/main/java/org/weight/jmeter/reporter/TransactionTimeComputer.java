package org.weight.jmeter.reporter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class TransactionTimeComputer {
	
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	private Map<String, Vector<Long>> result = new HashMap<String, Vector<Long>>();

	public Map<String, Vector<Long>> getResult() {
		return result;
	}
	
	public void reset(){
		result.clear();
	}
	
	public TransactionTimeResult compute(String transName){
		TransactionTimeResult ttr = new TransactionTimeResult();
		Vector<Long> times = result.get(transName);
		ttr.setLast(times.get(times.size() - 1));
		ttr.setAvg(sum(times) / times.size());
		Object[] sort = times.toArray();
		Arrays.sort(sort);
		ttr.setMin(Long.valueOf(sort[0].toString()));
		ttr.setMax(Long.valueOf(sort[sort.length - 1].toString()));
		ttr.setTime90(Long.valueOf(sort[(int)Math.floor(sort.length * 0.9)].toString()));
		
		return ttr;
	}
	
	private long sum(Vector<Long> times){
		long value = 0;
		for(long time : times){
			value += time;
		}
		return value;
	}
	
	public void setResult(Map<String, Vector<Long>> result) {

		pcs.firePropertyChange("result", null, result);
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl)
	{
		pcs.addPropertyChangeListener(pcl);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener pcl)
	{
		pcs.removePropertyChangeListener(pcl);
	}
	
}
