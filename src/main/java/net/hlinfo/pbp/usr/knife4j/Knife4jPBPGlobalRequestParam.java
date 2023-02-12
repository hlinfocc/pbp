package net.hlinfo.pbp.usr.knife4j;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;


@Configuration
@ConfigurationProperties(prefix="knife4j.global",ignoreInvalidFields=true,ignoreUnknownFields=true)
@Component
public class Knife4jPBPGlobalRequestParam {
	private List<Knife4jPBPRequestParameter> param;

	public List<Knife4jPBPRequestParameter> getParam() {
		return param;
	}

	public void setParam(List<Knife4jPBPRequestParameter> param) {
		this.param = param;
	}

	public Knife4jPBPGlobalRequestParam() {}

	public Knife4jPBPGlobalRequestParam(List<Knife4jPBPRequestParameter> param) {
		this.param = param;
	}
	
	/**
     * 全局参数
     * @return
     */
	/**
	 * 生成全局通用参数 v3.x
	 * @return
	 */
    public List<RequestParameter> getGlobalRequestParameters() {
        List<RequestParameter> parameters = new ArrayList<>();
        if(param == null) {
        	Knife4jPBPRequestParameter rp = new Knife4jPBPRequestParameter();
        	rp.setName("token");
        	rp.setDescription("接口校验参数");
        	rp.setScalarType("string");
        	rp.setParameterType("header");
        	rp.setRequired(true);
        	param.add(rp);
        }
        param.forEach(param -> {
        	ParameterType pt = ParameterType.HEADER;
        	switch (param.getParameterType()) {
			case "query":
				pt = ParameterType.QUERY;
				break;
			case "header":
				pt = ParameterType.HEADER;
				break;
			case "path":
				pt = ParameterType.PATH;
				break;
			case "cookie":
				pt = ParameterType.COOKIE;
				break;
			case "form":
				pt = ParameterType.FORM;
				break;
			case "formData":
				pt = ParameterType.FORMDATA;
				break;
			case "body":
				pt = ParameterType.BODY;
				break;
			default:
				break;
			}
        	RequestParameterBuilder prb = new RequestParameterBuilder()
	            .name(param.getName())
	            .description(param.getDescription())
	            .required(param.isRequired())
	            .in(pt);
        	switch (param.getScalarType()) {
			case "string":
				prb.query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)));
				break;
			case "int":
				prb.query(q -> q.model(m -> m.scalarModel(ScalarType.INTEGER)));
				break;
			case "long":
				prb.query(q -> q.model(m -> m.scalarModel(ScalarType.LONG)));
				break;
			case "double":
				prb.query(q -> q.model(m -> m.scalarModel(ScalarType.DOUBLE)));
				break;
			case "float":
				prb.query(q -> q.model(m -> m.scalarModel(ScalarType.FLOAT)));
				break;
			case "bigint":
				prb.query(q -> q.model(m -> m.scalarModel(ScalarType.BIGINTEGER)));
				break;
			case "bigdecimal":
				prb.query(q -> q.model(m -> m.scalarModel(ScalarType.BIGDECIMAL)));
				break;
			default:
				break;
			}
        	parameters.add(prb.build());
        });
        return parameters;
    }
    
	
}
