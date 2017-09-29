package org.weight.jmeter.http;

import java.io.IOException;

import javax.management.MBeanServerConnection;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.weight.jmeter.common.AccessException;
import org.weight.jmeter.common.Accessor;
import org.weight.jmeter.common.SampleResultObject;

public abstract class HTTPAccessor implements Accessor{
	
	private CloseableHttpClient httpclient = null;
	private HttpGet httpget = null;
	
	@Override
	public String getConnectorName() {
		
		return this.getClass().getSimpleName();
	}

	@Override
	public void open(String url, String username, String password) {
		HttpClientBuilder hcb = HttpClientBuilder.create();
		
		AuthScope authscope =
				new AuthScope(AuthScope.ANY_HOST,AuthScope.ANY_PORT);
		UsernamePasswordCredentials credentials = 
				new UsernamePasswordCredentials(username, password);
		
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(authscope, credentials);
		
		hcb.setDefaultCredentialsProvider(credentialsProvider);
		
	    httpclient = hcb.build();
		httpget = new HttpGet(url);
	}

	@Override
	public void shutdown() throws IOException, AccessException {

		if(httpclient != null){			
			httpclient.close();
		} else {
			throw (new AccessException());
		}
	}

	@Override
	public Object access() throws ClientProtocolException, IOException, AccessException {
		HttpEntity entity = null;
		if(httpclient != null){	
			CloseableHttpResponse response = httpclient.execute(httpget);
			entity = response.getEntity();
		} else {
			throw (new AccessException());
		}
		return handler(entity);
	}
	protected abstract SampleResultObject handler(HttpEntity entity);

}
