package org.weight.jmeter.extractor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import org.apache.jmeter.protocol.http.control.Cookie;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.processor.PostProcessor;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractScopedTestElement;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class VcodeExtractor extends AbstractScopedTestElement implements PostProcessor, Serializable{
	private static final Logger log = LoggingManager.getLoggerForClass();
	@Override
	public void process() {
		// TODO Auto-generated method stub
		JMeterContext context = getThreadContext();
        SampleResult previousResult = context.getPreviousResult();
        if (previousResult == null) {
            return;
        }
        if(context.getPreviousSampler() instanceof HTTPSampler){
        	HTTPSampler sampler = (HTTPSampler)context.getPreviousSampler();
        	//org.apache.jmeter.protocol.http.control.Cookie.Cookie(String name, 
        	//String value, String domain, String path, boolean secure, long expires)
        	sampler.getCookieManager().add(new Cookie("name", "value", "domain", "path", false, 0));
        	return;
        	
        }
        
        log.debug("VcodeExtractor processing result");
        
        String status = previousResult.getResponseCode();
        int id = context.getThreadNum();
        String imageName = id + ".jpg";
        
        if(status.equals("200")){
        	byte[] buffer = previousResult.getResponseData();
        	FileOutputStream out = null;
        	File file = null;
        	try {
        		file = new File(imageName);
      			out = new FileOutputStream(file);
      			out.write(buffer);
      			out.flush();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if(out != null){
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
        	/*
        	String[][] maps = {
    				{"]" , "J"},
    				{"|\\/|", "M"},
    				{"|\\|", "N"}
    		};
    		
    		int size = maps.length;
    		*/
            try {   
                String vcode = new OCR("D://Program Files (x86)//Tesseract-OCR").recognizeText(file, "jpg"); 
                vcode = vcode.replace(" ", "").trim();
                /*
                for(int i = 0; i < size; i++){
                	valCode = valCode.replace(maps[i][0], maps[i][1]);
                }
                */
                JMeterVariables var = context.getVariables();
                var.put("vcode", vcode);
                var.put("vuser", String.valueOf(id));
                //System.out.println(valCode);
            	} catch (Exception e) {
            		e.printStackTrace();
            	}    
    	}
        	
	}
	
}
