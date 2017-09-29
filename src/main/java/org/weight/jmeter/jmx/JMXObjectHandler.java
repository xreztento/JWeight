package org.weight.jmeter.jmx;

import java.lang.management.MemoryUsage;

import javax.management.openmbean.CompositeDataSupport;

public class JMXObjectHandler {

	public static long[] MemCompositeTypeObjHandle(Object object){
		long[] used = new long[3];
		MemoryUsage memUseage =  MemoryUsage.from((CompositeDataSupport)object);
		used[0] = memUseage.getMax();
		used[1] = memUseage.getUsed();
		used[2] = memUseage.getCommitted();
		return used;
	}
}
