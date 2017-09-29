package org.weight.jmeter.jmx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.weight.jmeter.common.AccessException;
import org.weight.jmeter.common.Accessor;
import org.weight.jmeter.common.SampleResultObject;

public abstract class JMXAccessor implements Accessor{
	
	private JMXConnector connector = null;
	
	@Override
	public String getConnectorName() {
		
		return JMXConnector.class.getSimpleName();
	}
	
	@Override
	public void open(String url, String username, String password) throws IOException {
		
		Map<String, String[]> environment = new HashMap<String, String[]>();
		String[] credentials = new String[] { username, password };
		
		environment.put(JMXConnector.CREDENTIALS, credentials);
		JMXServiceURL address = null;
		address = new JMXServiceURL(url);
		connector = JMXConnectorFactory.connect(address, environment);
	}
	
	@Override
	public Object access() throws IOException, AccessException {
		MBeanServerConnection mbsc = null;
		if(connector != null){
			mbsc = connector.getMBeanServerConnection();
		} else {
			throw (new AccessException());
		}
		
		return handle(mbsc);
	}
	
	@Override
	public void shutdown() throws IOException, AccessException {

		if(connector != null){			
			connector.close();
		} else {
			throw (new AccessException());
		}
	}
	
	protected abstract SampleResultObject handle(MBeanServerConnection mbsc);

}
