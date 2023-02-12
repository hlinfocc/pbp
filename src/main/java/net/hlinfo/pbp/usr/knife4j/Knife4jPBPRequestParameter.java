package net.hlinfo.pbp.usr.knife4j;

public class Knife4jPBPRequestParameter {
	private String name;
    private String description;
    private String scalarType;
    private boolean required;
    private String parameterType;
	public Knife4jPBPRequestParameter() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Knife4jPBPRequestParameter(String name, String description, String scalarType, boolean required,
			String parameterType) {
		super();
		this.name = name;
		this.description = description;
		this.scalarType = scalarType;
		this.required = required;
		this.parameterType = parameterType;
	}

	public String getScalarType() {
		return scalarType;
	}

	public void setScalarType(String scalarType) {
		this.scalarType = scalarType;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public String getParameterType() {
		return parameterType;
	}
	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}
    
}
