package org.bpmnwithactiviti.common;

import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.activiti.engine.impl.util.LogUtil;
import org.junit.BeforeClass;
import org.slf4j.bridge.SLF4JBridgeHandler;

public abstract class AbstractTest {
	
	@BeforeClass
	public static void routeLoggingToSlf4j() {
		LogUtil.readJavaUtilLoggingConfigFromClasspath();
		Logger rootLogger = LogManager.getLogManager().getLogger("");  
		Handler[] handlers = rootLogger.getHandlers();  
		for (int i = 0; i < handlers.length; i++) {  
			rootLogger.removeHandler(handlers[i]);  
		}
		SLF4JBridgeHandler.install();
	}

}
