package org.weight.jmeter.samplers;

import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.weight.jmeter.JMeterPluginUtils;


public class TransactionBeginSamplerGUI extends AbstractSamplerGui{
	private JTextField nameTextField = null;
	public TransactionBeginSamplerGUI(){
		init();	
	}
	
	@Override
    public void configure(TestElement element) {
		
        super.configure(element);
        nameTextField.setText(element.getPropertyAsString(TransactionBeginSampler.TRANS_NAME));
	}
	
	private void init() {
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        nameTextField = new JTextField(20);
        mainPanel.add(nameTextField);
        add(mainPanel);
	}
	
	@Override
	public TestElement createTestElement() {
		// TODO Auto-generated method stub
		
		TestElement sampler = new TransactionBeginSampler();
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
		if (sampler instanceof TransactionBeginSampler) {
			TransactionBeginSampler tSampler = (TransactionBeginSampler) sampler;
			tSampler.setProperty(TransactionBeginSampler.TRANS_NAME, nameTextField.getText());
		}
		
	}
	
	@Override
	public String getStaticLabel() {
		// TODO Auto-generated method stub
		return JMeterPluginUtils.prefixLabel("TransactionBegin");
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

