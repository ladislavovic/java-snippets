package cz.kul.snippets.springmvc._03.forms;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class User {

	@NotEmpty
	private String username;

	@Size(min = 5, max = 10)
	private String password;

	@NotEmpty
	@Email
	private String email;

	private Date birthDate;
	private String profession;
	
	private String select;
	
	private List<String> multipleSelect;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public List<String> getMultipleSelect() {
		return multipleSelect;
	}

	public void setMultipleSelect(List<String> multipleSelect) {
		this.multipleSelect = multipleSelect;
	}

}
