package org.weight.jmeter.samplers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.Interruptible;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.ThreadListener;
import org.weight.jmeter.config.CommonConfigGUI;
public class DebugSampler extends AbstractSampler implements ThreadListener, Interruptible{

    public static final String TestConfigGUIName = "TestSampler.TestConfigGUI";

    private static final Set<String> APPLIABLE_CONFIG_CLASSES = new HashSet<String>(
            Arrays.asList(new String[]{
                    "org.apache.jmeter.config.gui.LoginConfigGui",
                    "org.apache.jmeter.config.gui.SimpleConfigGui",
                    "org.weight.jmeter.config.TestConfigGUI"}));


    @Override
    public SampleResult sample(Entry e) {
        // TODO Auto-generated method stub

        SampleResult res = new SampleResult();
        System.out.println(getConfigGuiName());
        System.out.println(getUsername());
        System.out.println(getPassword());

        res.sampleStart();

        res.sampleEnd();
        res.setSuccessful(true);
        return res;
    }

    @Override
    public boolean interrupt() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void threadFinished() {
        // TODO Auto-generated method stub

    }

    @Override
    public void threadStarted() {
        // TODO Auto-generated method stub

    }

    public void setConfigGuiName(String name) {
        this.setProperty(TestConfigGUIName, name);
    }

    public String getUsername() {
        return getPropertyAsString(ConfigTestElement.USERNAME);
    }

    public String getPassword() {
        return getPropertyAsString(ConfigTestElement.PASSWORD);
    }

    public String getConfigGuiName() {
        return getPropertyAsString(TestConfigGUIName);
    }



    @Override
    public boolean applies(ConfigTestElement configElement) {
        String guiClass = configElement.getProperty(TestElement.GUI_CLASS).getStringValue();
        return APPLIABLE_CONFIG_CLASSES.contains(guiClass);
    }

}
