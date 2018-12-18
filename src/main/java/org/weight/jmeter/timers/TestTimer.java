package org.weight.jmeter.timers;

import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.timers.Timer;

import java.io.Serializable;
import java.security.SecureRandom;

public class TestTimer extends AbstractTestElement implements Timer, Serializable, TestBean{

	private long thinkTime;
	private int randomTime;


	@Override
	public long delay() {
		SecureRandom random = new SecureRandom();
		return thinkTime + random.nextInt(randomTime);
	}

	public long getThinkTime() {
		return thinkTime;
	}

	public void setThinkTime(long thinkTime) {
		this.thinkTime = thinkTime;
	}

	public int getRandomTime() {
		return randomTime;
	}

	public void setRandomTime(int randomTime) {
		this.randomTime = randomTime;
	}
}
