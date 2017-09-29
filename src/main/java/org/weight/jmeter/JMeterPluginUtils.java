package org.weight.jmeter;

import java.text.DecimalFormat;

public class JMeterPluginUtils {
	
	private static String PLUGINS_PREFIX = "jweight - ";
	private static boolean prefixPlugins = true;
	
	
	public static String prefixLabel(String label) {
        return prefixPlugins ? PLUGINS_PREFIX + label : label;
    }
	
	public static String formatToPercent(double num){
		DecimalFormat df = new DecimalFormat("##.##%");
		return df.format(num);
		
	}
	
	public static String formatToStdNum(long num){
		DecimalFormat df = new DecimalFormat(",###");
		return df.format(num);
	}
	
}
