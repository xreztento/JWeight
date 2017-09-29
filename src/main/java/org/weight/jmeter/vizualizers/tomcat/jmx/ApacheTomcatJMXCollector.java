package org.weight.jmeter.vizualizers.tomcat.jmx;

import java.io.IOException;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.weight.jmeter.common.AccessorSampleResult;
import org.weight.jmeter.common.AccessorSampler;
import org.weight.jmeter.common.SampleResultObject;

public class ApacheTomcatJMXCollector extends ResultCollector implements Runnable{
	
	private Thread thread = null;
	private final static String JMX_URL = "url";
	private final static String JMX_USERNAME = "username";
	private final static String JMX_PASSWORD = "password";
	
	@Override
    public void testStarted(){
		super.testStarted();
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
    public void testStarted(String host){
		super.testStarted();
		thread = new Thread(this);
		thread.start();
	}
	
	
	@Override
    public void testEnded() {
		thread.interrupt();
		super.testEnded();
	}
	
	@Override
    public void testEnded(String host) {
		thread.interrupt();
		super.testEnded(host);
	}
	
	
	@Override
    public void sampleOccurred(SampleEvent event) {
    }

    protected void jmxMonSampleOccurred(SampleEvent event) {
        super.sampleOccurred(event);
    }
     
    public void generateSample(ApacheTomcatJMXAccessor accessor, AccessorSampler sampler) throws IOException {
    	AccessorSampleResult res = new AccessorSampleResult();
    	SampleResultObject resultObj = sampler.sample(accessor);
    	
    	res.setSampleResultObject(resultObj);
		res.setSuccessful(true);
        SampleEvent e = new SampleEvent(res, "Apache Tomcat JMX");
        jmxMonSampleOccurred(e);
    }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String url = getPropertyAsString(JMX_URL);
		String username = getPropertyAsString(JMX_USERNAME);
		String password = getPropertyAsString(JMX_PASSWORD);
		
		ApacheTomcatJMXAccessor accessor = new ApacheTomcatJMXAccessor();
		AccessorSampler sampler = new AccessorSampler(url, username, password);
		try{
			
			while(true){
				
				try {
					
					synchronized(this){
						
						this.wait(getPropertyAsLong("interval"));
					}
					generateSample(accessor, sampler);
					
				} catch (InterruptedException e) {
					break;
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} finally {
		}
	}

}

