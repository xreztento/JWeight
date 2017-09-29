package org.weight.jmeter.reporter;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.visualizers.gui.AbstractListenerGui;
import org.weight.jmeter.JMeterPluginUtils;

public class SamplerResultReporter extends AbstractListenerGui{

	@Override
	public TestElement createTestElement() {
		SamplerResultCollector collector = new SamplerResultCollector();
		modifyTestElement(collector);
		return collector;
	}

	@Override
	public String getLabelResource() {
		// TODO Auto-generated method stub
		return this.getClass().getSimpleName();
	}
	
	@Override
	public String getStaticLabel() {
		// TODO Auto-generated method stub
		return JMeterPluginUtils.prefixLabel("SamplerResultReport");
	}

	@Override
	public void modifyTestElement(TestElement element) {
		super.configureTestElement(element);
		
	}
	
	@Override
    public void configure(TestElement element) {
        super.configure(element);
    }

}
