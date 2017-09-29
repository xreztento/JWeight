package org.weight.jmeter.samplers.scriptsampler;

import java.io.Serializable;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class JSTestElement extends AbstractTestElement implements Serializable, Cloneable {
	private transient volatile JSInterpreter interpreter = null;
    private static final Logger log = LoggingManager.getLoggerForClass();
    protected String script = null;
    public JSTestElement(){
    	super();
    	init();
    }
	
	private void init() {
		interpreter = new JSInterpreter();
	}
	
	protected JSInterpreter getJSInterpreter() {
		
		JMeterContext jmctx = JMeterContextService.getContext();
	    JMeterVariables vars = jmctx.getVariables();
	    interpreter.setLog(log);
	    interpreter.setProp(JMeterUtils.getJMeterProperties());
	    interpreter.setVars(vars);
	    return interpreter;
	}

	@Override
	public Object clone() {
		JSTestElement o = (JSTestElement) super.clone();
	    o.init();
	    return o;
	}
	
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	
}
