package org.weight.jmeter.vizualizers.tomcat.jmx;

import java.awt.Image;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;

import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.visualizers.GraphListener;
import org.apache.jmeter.visualizers.ImageVisualizer;
import org.apache.jmeter.visualizers.Sample;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.weight.jmeter.common.AccessorSampleResult;
import org.weight.jmeter.gui.TimeSeriesChartPanel;
import org.weight.jmeter.jmx.JMXObjectHandler;
import org.weight.jmeter.JMeterPluginUtils;


public class ApacheTomcatJMXVizualizer extends AbstractVisualizer implements Clearable, GraphListener, ImageVisualizer {
	TimeSeries series = new TimeSeries("Memory");
	public ApacheTomcatJMXVizualizer(){
		super();
		init();
	}
	
	public void init(){
		
		TimeSeriesChartPanel chart = new TimeSeriesChartPanel("Memory", series, null, "Time", "Usage(MB)");
		add(chart);
	}
	
	@Override
	public void add(SampleResult res) {

		AccessorSampleResult asr = (AccessorSampleResult)res;
		HashMap<String, HashMap<String, Object>> appServerJMXResultObject = (HashMap<String, HashMap<String, Object>>)asr.getSampleResultObject().get();		
		Iterator<Entry<String, HashMap<String, Object>>> iterName = appServerJMXResultObject.entrySet().iterator();
		
		while(iterName.hasNext()){
			Entry<String, HashMap<String, Object>> entryName = iterName.next();
			
			if(entryName.getKey().equals("java.lang:type=Memory")){
				Iterator<Entry<String, Object>> iterAttr = entryName.getValue().entrySet().iterator();
				while(iterAttr.hasNext()){
					Entry<String, Object> entryAttr =  iterAttr.next();
					if(entryAttr.getKey().equals("HeapMemoryUsage")){
						series.add(new Millisecond(), JMXObjectHandler.MemCompositeTypeObjHandle(entryAttr.getValue())[1] / (float)(1024 * 1024));
					}
				}
			}
		}

	}
	
	@Override
    public TestElement createTestElement() {
		
        TestElement te = new ApacheTomcatJMXCollector();
        modifyTestElement(te);
        te.setComment("ApacheTomcat");
        return te;
    }
	
	@Override
    public void modifyTestElement(TestElement te) {
		
        super.modifyTestElement(te);
       

        if (te instanceof ApacheTomcatJMXCollector) {
        	ApacheTomcatJMXCollector atjc = (ApacheTomcatJMXCollector) te;
        	
        }
        super.configureTestElement(te);
    }

    @Override
    public void configure(TestElement te) {
    	
        super.configure(te);
        ApacheTomcatJMXCollector atjc = (ApacheTomcatJMXCollector) te;
        atjc.setProperty("url", "service:jmx:rmi:///jndi/rmi://127.0.0.1:8999/jmxrmi");
        atjc.setProperty("username", "jmx");
        atjc.setProperty("password", "tomcat");
        atjc.setProperty("interval", 2000);
    }


	@Override
	public void clearData() {

	}

	@Override
	public String getLabelResource() {
		return this.getClass().getSimpleName();
	}

	@Override
	public String getStaticLabel() {//������ʾ����
		return JMeterPluginUtils.prefixLabel("Apache Tomcat JMX PerfMon");
	}
	
	@Override
	public Image getImage() {
		return null;
	}

	@Override
	public void updateGui() {
		
	}

	@Override
	public void updateGui(Sample sample) {
		
	}

}

