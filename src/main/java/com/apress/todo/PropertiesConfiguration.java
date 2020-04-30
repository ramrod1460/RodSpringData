package com.apress.todo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("swaggerto")
public class PropertiesConfiguration {
	public String swaggermessage;

	public String getSwaggermessage() {
		return swaggermessage;
	}

	public void setSwaggermessage(String swaggermessage) {
		this.swaggermessage = swaggermessage;
	}
	
}
