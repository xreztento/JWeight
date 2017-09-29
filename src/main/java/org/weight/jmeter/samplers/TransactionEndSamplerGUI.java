package org.weight.jmeter.samplers;

import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.weight.jmeter.JMeterPluginUtils;


public class TransactionEndSamplerGUI extends AbstractSamplerGui{
	private JTextField nameTextField = null;
	public TransactionEndSamplerGUI(){
		init();	
	}
	
	@Override
    public void configure(TestElement element) {
		
        super.configure(element);
        nameTextField.setText(element.getPropertyAsString(TransactionEndSampler.TRANS_NAME));
	}
	
	private void init() {
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        nameTextField = new JTextField(20);
        mainPanel.add(nameTextField);
        add(mainPanel);
	}
	
	@Override
	public TestElement createTestElement() {
		
		TestElement sampler = new TransactionEndSampler();
		sampler.setName(nameTextField.getText());
		modifyTestElement(sampler);
		return sampler;
	}

	@Override
	public String getLabelResource() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void modifyTestElement(TestElement sampler) {
		
		super.configureTestElement(sampler);
		if (sampler instanceof TransactionEndSampler) {
			TransactionEndSampler tSampler = (TransactionEndSampler) sampler;
			tSampler.setProperty(TransactionEndSampler.TRANS_NAME, nameTextField.getText());
		}
		
	}
	
	@Override
	public String getStaticLabel() {
		return JMeterPluginUtils.prefixLabel("TransactionEnd");
	}
	
	private void initFields(){
		nameTextField.setText("");
	}
	
	@Override
    public void clearGui() {
		
        super.clearGui();
        initFields();
    }

}

