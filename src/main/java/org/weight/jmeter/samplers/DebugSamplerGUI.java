package org.weight.jmeter.samplers;

import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.weight.jmeter.JMeterPluginUtils;
public class DebugSamplerGUI extends AbstractSamplerGui{

    @Override
    public TestElement createTestElement() {
        // TODO Auto-generated method stub
        TestSampler sampler = new TestSampler();
        modifyTestElement(sampler);
        return sampler;
    }

    @Override
    public String getLabelResource() {
        // TODO Auto-generated method stub
        return this.getClass().getName();
    }

    @Override
    public String getStaticLabel() {
        // TODO Auto-generated method stub
        return JMeterPluginUtils.prefixLabel("debug");
    }

    @Override
    public void modifyTestElement(TestElement sampler) {
        // TODO Auto-generated method stub
        sampler.clear();
        this.configureTestElement(sampler);
    }

    @Override
    public void clearGui() {
        super.clearGui();
    }

}
