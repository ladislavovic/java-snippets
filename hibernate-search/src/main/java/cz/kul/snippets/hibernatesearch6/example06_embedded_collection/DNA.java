package cz.kul.snippets.hibernatesearch6.example06_embedded_collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class DNA implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	private String code;

	private String type;

	public DNA(String code, String type) {
		this.code = code;
		this.type = type;
	}

	public DNA() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
