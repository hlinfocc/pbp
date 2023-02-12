package net.hlinfo.pbp.etc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RestController;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;

import cn.hutool.core.collection.CollectionUtil;
import net.hlinfo.pbp.usr.knife4j.Knife4jPBPApiInfo;
import net.hlinfo.pbp.usr.knife4j.Knife4jPBPGlobalRequestParam;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;


//@EnableKnife4j
//@EnableOpenApi
//@Import(BeanValidatorPluginsConfiguration.class)
@Configuration
@Profile({"dev", "test"})
public class Knife4jPbpConfiguration {
	@Value("${knife4j.enable:false}")
    private boolean enable;
	
	@Autowired
	private Knife4jPBPApiInfo apiInfo;
	@Autowired
	private Knife4jPBPGlobalRequestParam global;
	
	@Bean(value = "defaultPBPApiAll")
    public Docket defaultPBPApiAll() {
        Docket docket=new Docket(DocumentationType.OAS_30)
        		.enable(enable)
                .apiInfo(apiInfo.apiInfo())
                  //分组名称
                .groupName("All")
                .select()
                 //这里指定扫描所有Controller包路径
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .globalRequestParameters(global.getGlobalRequestParameters());
        return docket;
    }
	
	@Bean(value = "defaultPBPModel")
    public Docket defaultPBPModel() {
        Docket docket=new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo.apiInfo())
                  //分组名称
                .groupName("PBP模块")
                .select()
                   //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("net.hlinfo.pbp.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalRequestParameters(global.getGlobalRequestParameters());
        return docket;
    }
		
	 
	private ApiKey apiKey() {
        return new ApiKey("BearerToken", "Authorization", "header");
    }
    private ApiKey apiKey1() {
        return new ApiKey("BearerToken1", "Authorization-x", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }
    private SecurityContext securityContext1() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth1())
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return CollectionUtil.newArrayList(new SecurityReference("BearerToken", authorizationScopes));
    }
    List<SecurityReference> defaultAuth1() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return CollectionUtil.newArrayList(new SecurityReference("BearerToken1", authorizationScopes));
    }
    
}
