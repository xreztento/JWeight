package org.weight.jmeter.vizualizers.tomcat.jmx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.weight.jmeter.appserver.AppServerJMXAccessor;
import org.weight.jmeter.appserver.AppServerJMXSampleResultObject;
import org.weight.jmeter.common.AccessorSampleResult;
import org.weight.jmeter.common.SampleResultObject;

public class ApacheTomcatJMXAccessor extends AppServerJMXAccessor{
	@Override
	protected SampleResultObject handle(MBeanServerConnection mbsc) {
		AppServerJMXSampleResultObject resultObj = (AppServerJMXSampleResultObject)super.handle(mbsc);
		setThreadPoolValues(mbsc, resultObj);
		return resultObj;
	}
	
	
	private void setThreadPoolValues(MBeanServerConnection mbsc, AppServerJMXSampleResultObject resultObj){
		
		try {
			ObjectName threadpoolObjName = new ObjectName("Catalina:type=ThreadPool,*");
			Set<ObjectName> set = mbsc.queryNames(threadpoolObjName, null);
			HashMap<String,HashMap<String, Object>> threadPoolMap = new HashMap<String,HashMap<String, Object>>(set.size());
			
	        for (ObjectName obj : set){
	        	
	        	HashMap<String, Object> map = new HashMap<String, Object>(3);
	        	String name = obj.getKeyProperty("name");
	        	ObjectName attrName = new ObjectName(obj.getCanonicalName());
	        	map.put("maxThreads", mbsc.getAttribute( attrName, "maxThreads"));
	        	map.put("currentThreadCount", mbsc.getAttribute( attrName, "currentThreadCount"));
	        	map.put("currentThreadsBusy", mbsc.getAttribute( attrName, "currentThreadsBusy"));
	        	threadPoolMap.put(name, map);
	        	
	        }
	        resultObj.get().putAll(threadPoolMap);
	          
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
