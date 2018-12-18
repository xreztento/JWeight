package org.weight.jmeter.timers;

import java.beans.PropertyDescriptor;
import org.apache.jmeter.testbeans.BeanInfoSupport;

public class DistributedSyncTimerBeanInfo extends BeanInfoSupport {

    public DistributedSyncTimerBeanInfo() {
        super(DistributedSyncTimer.class);

        createPropertyGroup("grouping", new String[] { "groupSize", "timeoutInMs" });
        createPropertyGroup("subscribedServer", new String[] { "subscribedServerIpAddress", "subscribedServerIpPort", "channelName" });

        PropertyDescriptor p = property("groupSize");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, Integer.valueOf(0));

        p = property("timeoutInMs");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, Long.valueOf(0));

        p = property("subscribedServerIpAddress");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, String.valueOf("127.0.0.1"));

        p = property("subscribedServerIpPort");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, Integer.valueOf(6379));

        p = property("channelName");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, String.valueOf("default"));
    }

}