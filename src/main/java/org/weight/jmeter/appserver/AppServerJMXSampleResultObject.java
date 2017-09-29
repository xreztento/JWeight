package org.weight.jmeter.appserver;

import java.util.HashMap;

import org.weight.jmeter.common.SampleResultObject;

public class AppServerJMXSampleResultObject extends SampleResultObject{
	private final static int MAX_OBJECT_NUM = 100;
	
	protected HashMap<String, HashMap<String, Object>> appServerJMXResultObject = new HashMap<String, HashMap<String, Object>>(MAX_OBJECT_NUM);
	
	@Override
	public HashMap<String, HashMap<String, Object>> get() {
		// TODO Auto-generated method stub
		return this.appServerJMXResultObject;
	}

	@Override
	public void set(Object object) {
		// TODO Auto-generated method stub
		if(object instanceof HashMap<?, ?>){
			this.appServerJMXResultObject = (HashMap<String, HashMap<String, Object>>)object;
		}
	}

	@Override
	public void init() {
		final String[] objectNames = new String[]{
					"java.lang:type=OperatingSystem",
					"java.lang:type=Runtime",
					"java.lang:type=Memory",
					"java.lang:type=MemoryPool,name=PS Eden Space",
					"java.lang:type=MemoryPool,name=PS Old Gen",
					"java.lang:type=MemoryPool,name=PS Survivor Space",
					"java.lang:type=MemoryPool,name=Code Cache",
					"java.lang:type=MemoryPool,name=Compressed Class Space",
					"java.lang:type=MemoryPool,name=Metaspace",
					"java.lang:type=ClassLoading",
					"java.lang:type=Threading",
					"java.lang:type=GarbageCollector,name=PS MarkSweep",
					"java.lang:type=GarbageCollector,name=PS Scavenge",
					
				};
				
		final String[][] objectAttrs = new String[][]{
				{
					"Name",
					"Arch",
					"AvailableProcessors",
					"SystemCpuLoad",
					"ProcessCpuLoad",
					"TotalPhysicalMemorySize",
					"FreePhysicalMemorySize",
					"TotalSwapSpaceSize",
					"FreeSwapSpaceSize"
				},
				{
					"VmName",
					"VmVendor",
					"VmVersion",
					"StartTime",
					"Uptime"
					
				},
				{
					"HeapMemoryUsage",
					"NonHeapMemoryUsage"
				},
				{
					"Usage"
				},
				{
					"Usage"
				},
				{
					"Usage"
				},
				{
					"Usage"
				},
				{
					"Usage"
				},
				{
					"Usage"
				},
				{
					"LoadedClassCount",
					"TotalLoadedClassCount",
					"LoadedClassCount"
				},
				{
					"ThreadCount"
				},
				{
					"CollectionTime",
					"CollectionCount"
				},
				{
					"CollectionTime",
					"CollectionCount"
				}
			};
		 
		 setKeys(objectNames, objectAttrs);
		
	}

	protected void setKeys(String[] objectNames, String[][] objectAttrs){
		 for(int i = 0; i < objectNames.length; i++){
				
				HashMap<String, Object> attrMap = new HashMap<String, Object>(objectAttrs[i].length);
				for(String attr : objectAttrs[i]){
					attrMap.put(attr, null);
				}
				appServerJMXResultObject.put(objectNames[i], attrMap);
			}
	}

}
