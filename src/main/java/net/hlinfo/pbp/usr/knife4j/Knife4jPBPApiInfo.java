package net.hlinfo.pbp.usr.knife4j;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import net.hlinfo.opt.Func;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;

@Configuration
@ConfigurationProperties(prefix="knife4j.apiinfo",ignoreInvalidFields=true,ignoreUnknownFields=true)
public class Knife4jPBPApiInfo {
	private String title;
    private String description;
    private String terms;
    private String name;
    private String url;
    private String email;
    private String version;
    
	public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title(Func.isBlank(title)?"项目API文档":title)
                .description(Func.isBlank(description)?"项目 RESTful APIs":description)
                .termsOfServiceUrl(terms)
                .contact(new Contact(name,url,email))
                .version(Func.isBlank(version)?"1.0":version)
                .build();
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
    
}
