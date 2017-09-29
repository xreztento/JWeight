package org.weight.jmeter.samplers.scriptsampler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.util.ConfigMergabilityIndicator;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.Interruptible;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class JSSampler extends JSTestElement implements Sampler, Interruptible, ConfigMergabilityIndicator{
	private static final Set<String> APPLIABLE_CONFIG_CLASSES = new HashSet<String>(
            Arrays.asList(new String[]{
                    "org.apache.jmeter.config.gui.SimpleConfigGui"}));
	
    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String SCRIPT = "JSSampler.query";
	@Override
	public boolean interrupt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SampleResult sample(Entry entry) {
		// TODO Auto-generated method stub
		SampleResult res = new SampleResult();
        res.setSampleLabel(getName());
        res.sampleStart();
        final JSInterpreter interpreter = getJSInterpreter();
        interpreter.eval(this.getPropertyAsString(SCRIPT));
        res.setSuccessful(true);
        res.sampleEnd();
		return res;
	}
	
	@Override
    public boolean applies(ConfigTestElement configElement) {
        String guiClass = configElement.getProperty(TestElement.GUI_CLASS).getStringValue();
        return APPLIABLE_CONFIG_CLASSES.contains(guiClass);
    }

}
