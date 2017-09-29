package org.weight.jmeter.appserver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Iterator;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.weight.jmeter.common.SampleResultObject;
import org.weight.jmeter.jmx.JMXAccessor;

public class AppServerJMXAccessor extends JMXAccessor{

	@Override
	protected SampleResultObject handle(MBeanServerConnection mbsc) {
		// TODO Auto-generated method stub
		AppServerJMXSampleResultObject resultObj = new AppServerJMXSampleResultObject();
		resultObj.init();
		setValues(mbsc, resultObj);
		return resultObj;
	}
	
	protected void setValues(MBeanServerConnection mbsc, AppServerJMXSampleResultObject resultObj){
		HashMap<String, HashMap<String, Object>> appServerJMXResultObject = resultObj.get();
		
		Iterator<Entry<String, HashMap<String, Object>>> iterName = appServerJMXResultObject.entrySet().iterator();
		
		while(iterName.hasNext()){
			Entry<String, HashMap<String, Object>> entryName = iterName.next();
			try {
				
				ObjectName objName = new ObjectName(entryName.getKey());
				Iterator<Entry<String, Object>> iterAttr = entryName.getValue().entrySet().iterator();
				while(iterAttr.hasNext()){
					Entry<String, Object> entryAtt =  iterAttr.next();
					Object obj = mbsc.getAttribute(objName, entryAtt.getKey());
					entryAtt.setValue(obj);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
