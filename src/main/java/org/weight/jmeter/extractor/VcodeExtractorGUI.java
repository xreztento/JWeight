package org.weight.jmeter.extractor;

import org.apache.jmeter.processor.gui.AbstractPostProcessorGui;
import org.apache.jmeter.testelement.TestElement;
import org.weight.jmeter.JMeterPluginUtils;

public class VcodeExtractorGUI extends AbstractPostProcessorGui{

	@Override
	public TestElement createTestElement() {
		VcodeExtractor extractor = new VcodeExtractor();
        modifyTestElement(extractor);
        return extractor;
	}

	@Override
	public String getLabelResource() {
		return this.getClass().getName();
	}
	
	@Override
	public String getStaticLabel() {
		//return JMeterPluginUtils.prefixLabel("vcode");
		return "VcodeExtractor";
	}

	@Override
	public void modifyTestElement(TestElement extractor) {
		super.configureTestElement(extractor);
        
	}

}
