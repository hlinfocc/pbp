package net.hlinfo.pbp.etc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="spring.profiles",ignoreInvalidFields=true, ignoreUnknownFields=true)
public class EnvConfig {
	//
	private String active;

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}
	
	public boolean isprod() {
		if(this.active=="prod" || "prod".equals(active)) {
			return true;
		}
		return false;
	}

	public boolean isdev() {
		if(this.active=="dev" || "dev".equals(active)) {
			return true;
		}
		return false;
	}
	
	public boolean istest() {
		if(this.active=="test" || "test".equals(active)) {
			return true;
		}
		return false;
	}
	
}
