package org.weight.jmeter.reporter;

public class TransactionTimeResult {
	private long min;
	private long max;
	private long time90;
	private long avg;
	private long last;
	
	public long getMin() {
		return min;
	}
	public void setMin(long min) {
		this.min = min;
	}
	public long getMax() {
		return max;
	}
	public void setMax(long max) {
		this.max = max;
	}
	public long getTime90() {
		return time90;
	}
	public void setTime90(long time90) {
		this.time90 = time90;
	}
	public long getAvg() {
		return avg;
	}
	public void setAvg(long avg) {
		this.avg = avg;
	}
	public long getLast() {
		return last;
	}
	public void setLast(long last) {
		this.last = last;
	}

}
