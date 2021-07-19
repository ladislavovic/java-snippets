package cz.kul.snippets.hibernatesearch6.example08_multiple_type_bridges;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Link {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	public Link() {
	}

	public Link(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
