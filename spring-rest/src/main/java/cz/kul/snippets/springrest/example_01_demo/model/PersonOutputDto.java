package cz.kul.snippets.springrest.example_01_demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "PersonOutput")
public class PersonOutputDto {

	private long id;

	@ApiModelProperty(
			value = "Name of the **person**.", // 'description' in the swagger
			name = "personName", // can set explicit name of the attribute
			allowableValues = "Monica,Rachel,Phoeboe",
			example = "Rachel"

	)
	private String name;

	@ApiModelProperty(notes = "Roles")
	private List<RoleOutputDto> roles;

	public PersonOutputDto() {
	}

	public PersonOutputDto(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RoleOutputDto> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleOutputDto> roles) {
		this.roles = roles;
	}

}
