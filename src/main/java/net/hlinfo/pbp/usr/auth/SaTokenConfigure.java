package net.hlinfo.pbp.usr.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.dev33.satoken.interceptor.SaInterceptor;

@Configuration
public class SaTokenConfigure  implements WebMvcConfigurer {
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
    	SaInterceptor interceptor = new SaInterceptor();
    	List<String> patterns = new ArrayList<String>();
    	patterns.add("/**");
        registry.addInterceptor(interceptor).addPathPatterns(patterns);
    }
}
