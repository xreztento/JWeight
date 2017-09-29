package org.weight.jmeter.common;

public class AccessorSampler {
	private String url = null;
	private String username = null;
	private String password = null;
	
	public AccessorSampler(String url, String username, String password){
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public SampleResultObject sample(Accessor accessor){
		SampleResultObject resultObj = null;
		try {
			accessor.open(url, username, password);
			resultObj = (SampleResultObject)accessor.access();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				accessor.shutdown();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return resultObj;
	}
}
