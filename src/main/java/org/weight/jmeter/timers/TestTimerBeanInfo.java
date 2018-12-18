package org.weight.jmeter.timers;

import org.apache.jmeter.testbeans.BeanInfoSupport;

import java.beans.PropertyDescriptor;

public class TestTimerBeanInfo extends BeanInfoSupport {

    public TestTimerBeanInfo() {
        super(TestTimer.class);

        createPropertyGroup("thinkTime", new String[] { "thinkTime" });
        createPropertyGroup("randomTime", new String[] { "randomTime" });

        PropertyDescriptor p = property("thinkTime");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, Long.valueOf(0));

        p = property("randomTime");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, Integer.valueOf(0));

    }

}