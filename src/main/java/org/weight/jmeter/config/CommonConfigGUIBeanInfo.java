package org.weight.jmeter.config;

import java.beans.PropertyDescriptor;

import org.apache.jmeter.testbeans.BeanInfoSupport;

public class CommonConfigGUIBeanInfo extends BeanInfoSupport {
	private static final String FILENAME = "filename";
	

	protected CommonConfigGUIBeanInfo() {
		super(CommonConfigGUI.class);
		createPropertyGroup("common_data",
                new String[] { FILENAME } );
		
		PropertyDescriptor p = property(FILENAME);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");
        p.setValue(NOT_EXPRESSION, Boolean.TRUE);
        
	}

}
