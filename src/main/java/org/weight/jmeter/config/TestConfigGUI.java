package org.weight.jmeter.config;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.StringProperty;
import org.weight.jmeter.JMeterPluginUtils;
import org.weight.jmeter.samplers.DebugSampler;

public class TestConfigGUI extends AbstractConfigGui{
	
	private boolean displayName = true;
	
	public TestConfigGUI(){
		this(true);
		init();
	}

	public TestConfigGUI(boolean displayName){
		this.displayName = displayName;
        init();
	}
	
	@Override
	public TestElement createTestElement() {
		// TODO Auto-generated method stub
		ConfigTestElement element = new ConfigTestElement();
        modifyTestElement(element);
        return element;
	}
	
	 @Override
	 public void configure(TestElement element) {
	    super.configure(element);
	 }
	
	
	@Override
	public String getLabelResource() {
		// TODO Auto-generated method stub
		return this.getClass().getName();
	}
	
	@Override
	public String getStaticLabel() {
		// TODO Auto-generated method stub
		return JMeterPluginUtils.prefixLabel("test");
	}

	@Override
	public void modifyTestElement(TestElement element) {
		// TODO Auto-generated method stub
		configureTestElement(element);
		element.setProperty(new StringProperty(DebugSampler.TestConfigGUIName, "TestConfig"));
	}
	
	@Override
    public void clearGui() {
        super.clearGui();
	}
	
	private void init(){
		
	}

}
