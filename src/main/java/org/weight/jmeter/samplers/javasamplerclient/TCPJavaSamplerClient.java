package org.weight.jmeter.samplers.javasamplerclient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class TCPJavaSamplerClient extends AbstractJavaSamplerClient{
	
	private static String host = null;
	private static int port;
	
	private PrintWriter out = null;
	private static ThreadLocal<PrintWriter> outHolder = new ThreadLocal<PrintWriter>();
	private static ThreadLocal<Socket> socketHolder= new ThreadLocal<Socket>() {
	
		
	protected Socket initialValue() {
		
		Socket socket = null;
		try {
			socket = new Socket();
			socket.setKeepAlive(true);
			socket.connect(new InetSocketAddress(host, port));
			if(socket.isConnected()){
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				outHolder.set(out);
			}
		} catch (IOException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return socket;
		    
		}
	};
	
	@Override
	public void setupTest(JavaSamplerContext context) {
		host = context.getParameter("host");
		port = context.getIntParameter("port");
		socketHolder.get();
		out = outHolder.get();
		System.out.println("setupTest:" + Thread.currentThread().getId());
	}
	
	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		// TODO Auto-generated method stub
		SampleResult result = getSampleResult();
		result.sampleStart();
		
		out.write(Thread.currentThread().getId()+ ":Hello JavaSamplerClient!");
		out.flush();
		result.setResponseData((Thread.currentThread().getId() + ":success!").getBytes());
		result.sampleEnd();
		result.setSuccessful(true);
		return result;
	}
	
	public SampleResult getSampleResult()
    {
        SampleResult result = new SampleResult();
        result.setSampleLabel(getLabel());
        return result;
    }
	
	public String getLabel()
    {
        return "TCPSampler" + Thread.currentThread().getId();
    }
	
	@Override
	public void teardownTest(JavaSamplerContext context) {
		
		Socket socket = socketHolder.get();
		try {
			out.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("teardownTest:" + Thread.currentThread().getId());

	}
	
}
