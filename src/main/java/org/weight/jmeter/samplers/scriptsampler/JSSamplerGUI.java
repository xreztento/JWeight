package org.weight.jmeter.samplers.scriptsampler;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.apache.jmeter.gui.util.JSyntaxTextArea;
import org.apache.jmeter.gui.util.JTextScrollPane;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.weight.jmeter.JMeterPluginUtils;

public class JSSamplerGUI extends AbstractSamplerGui{
	
	private JSyntaxTextArea scriptField = null;
	
	public JSSamplerGUI(){
		init();	
	}
	
	@Override
    public void configure(TestElement element) {
		
		scriptField.setInitialText(element.getPropertyAsString(JSSampler.SCRIPT));
	    scriptField.setCaretPosition(0);
        super.configure(element);
        
	}
	
	private void init() {
        
		setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        JPanel panel = createScriptPanel();
        add(panel, BorderLayout.CENTER);
	}
	
	private JPanel createScriptPanel() {
        scriptField = new JSyntaxTextArea(20, 20);

        JLabel label = new JLabel("js_script");
        label.setLabelFor(scriptField);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(new JTextScrollPane(scriptField), BorderLayout.CENTER);
        
        JTextArea explain = new JTextArea(JMeterUtils.getResString("bsh_script_variables")); //$NON-NLS-1$
        explain.setLineWrap(true);
        explain.setEditable(false);
        explain.setBackground(this.getBackground());
        panel.add(explain, BorderLayout.SOUTH);

        return panel;
    }
	
	@Override
	public TestElement createTestElement() {
		// TODO Auto-generated method stub
		
		TestElement sampler = new JSSampler();
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
		if (sampler instanceof JSSampler) {
			JSSampler jsSampler = (JSSampler) sampler;
			jsSampler.setProperty(JSSampler.SCRIPT, scriptField.getText());
		}
	}
	
	@Override
	public String getStaticLabel() {
		// TODO Auto-generated method stub
		return JMeterPluginUtils.prefixLabel("JSSampler");
	}
	
	private void initFields(){
		scriptField.setText("");
	}
	
	@Override
    public void clearGui() {
		
        super.clearGui();
        initFields();
    }
}
