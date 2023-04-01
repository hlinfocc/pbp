/**
 * 
 */
package net.hlinfo.pbp.etc;

import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;
import io.undertow.UndertowOptions;

/**
 * @author hlinfo
 *
 */
@Component
public class UndertowServerCustomizer implements WebServerFactoryCustomizer<UndertowServletWebServerFactory>  {
	/**
	 * 配置Undertow服务器url允许|[]等特殊字符
	 */
	@Override
	public void customize(UndertowServletWebServerFactory factory) {
		factory.addBuilderCustomizers(builder-> builder.setServerOption(UndertowOptions.ALLOW_UNESCAPED_CHARACTERS_IN_URL, Boolean.TRUE));
	}
}