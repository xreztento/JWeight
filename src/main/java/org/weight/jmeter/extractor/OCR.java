package org.weight.jmeter.extractor;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class OCR {
	private final String LANG_OPTION = "-l";
	private final String EOL = System.getProperty("line.separator");
	private String tessPath = "";
	
	public OCR(String tessPath){
		this.tessPath = tessPath;
	}
	
	public String recognizeText(File imageFile,String imageFormat) {
		File tempImage = ImageIOHelper.createImage(imageFile,imageFormat);
		File outputFile = new File(imageFile.getParentFile(),"output" + imageFile.getName());
		StringBuffer sb = new StringBuffer();
		List<String> cmd = new ArrayList<String>();
		
		cmd.add(tessPath+"//tesseract");
		cmd.add("");
		cmd.add(outputFile.getName());
		cmd.add(LANG_OPTION);
		cmd.add("eng");		
		ProcessBuilder pb = new ProcessBuilder();
		pb.directory(imageFile.getParentFile());
		
		cmd.set(1, tempImage.getName());
		pb.command(cmd);
		pb.redirectErrorStream(true);
		
		Process process = null;
		BufferedReader in = null;
		int wait;
		try {
			process = pb.start();
			//tesseract.exe xxx.tif 1 -l eng
			wait = process.waitFor();
			if(wait == 0){
				in = new BufferedReader(new InputStreamReader(new FileInputStream(outputFile.getAbsolutePath()+".txt"),"UTF-8"));
				String str;
				while((str = in.readLine())!=null){
					sb.append(str).append(EOL);
				}
				in.close();
				
			}else{
				
				tempImage.delete();
			}
			new File(outputFile.getAbsolutePath()+".txt").delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		tempImage.delete();
		return sb.toString();
	}
}
