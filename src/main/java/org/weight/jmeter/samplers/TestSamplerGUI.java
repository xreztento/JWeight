package org.weight.jmeter.samplers;

import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.weight.jmeter.JMeterPluginUtils;


public class TestSamplerGUI extends AbstractSamplerGui{
	private JTextField nameTextField = null;
	private JTextField sleepTextField = null;
	public TestSamplerGUI(){
		init();	
	}
	
	@Override
    public void configure(TestElement element) {
		
        super.configure(element);
        nameTextField.setText(element.getPropertyAsString(TestSampler.NAME));
        sleepTextField.setText(element.getPropertyAsString(TestSampler.SLEEP));
	}
	
	private void init() {
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        nameTextField = new JTextField(20);
        sleepTextField = new JTextField(20);
        mainPanel.add(nameTextField);
        mainPanel.add(sleepTextField);
        add(mainPanel);
	}
	
	@Override
	public TestElement createTestElement() {
		// TODO Auto-generated method stub
		
		TestElement sampler = new TestSampler();
		sampler.setName(nameTextField.getText());
		modifyTestElement(sampler);
		return sampler;
	}

	@Override
	public String getLabelResource() {
		// TODO Auto-generated method stub
		return this.getClass().getSimpleName();
	}

	@Override
	public void modifyTestElement(TestElement sampler) {
		
		// TODO Auto-generated method stub
		super.configureTestElement(sampler);
		if (sampler instanceof TestSampler) {
			TestSampler sleepSampler = (TestSampler) sampler;
			sleepSampler.setProperty(TestSampler.NAME, nameTextField.getText());
			sleepSampler.setProperty(TestSampler.SLEEP, sleepTextField.getText());
		}
		
	}
	
	@Override
	public String getStaticLabel() {
		// TODO Auto-generated method stub
		return JMeterPluginUtils.prefixLabel("SleepTest");
	}
	
	private void initFields(){
		nameTextField.setText("");
		sleepTextField.setText("");
	}
	
	@Override
    public void clearGui() {
		
        super.clearGui();
        initFields();
    }

}

