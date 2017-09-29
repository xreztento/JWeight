package org.weight.jmeter.samplers.scriptsampler;

import java.util.Properties;

import org.apache.jmeter.threads.JMeterVariables;
import org.apache.log.Logger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class JSInterpreter {
	private JMeterVariables vars = null;
	private Properties props = null;
	private Logger log = null;
	
	public void setVars(JMeterVariables vars) {
		this.vars = vars;
	}


	public void setProp(Properties props) {
		this.props = props;
	}


	public void setLog(Logger log) {
		this.log = log;
	}

	public void eval(String script){
		Context ct = Context.enter(); 
		Scriptable scope = ct.initStandardObjects();
		Context.javaToJS(log, scope);
		Context.javaToJS(vars, scope);
		Context.javaToJS(props, scope);
		ScriptableObject.putProperty(scope, "log", log);
		ScriptableObject.putProperty(scope, "vars", vars); 
		ScriptableObject.putProperty(scope, "props", props); 
		Object result = ct.evaluateString(scope, script.replace("\n", "").replace("\r", ""), null, 1, null);
		System.out.println(result.toString());
	}
}
