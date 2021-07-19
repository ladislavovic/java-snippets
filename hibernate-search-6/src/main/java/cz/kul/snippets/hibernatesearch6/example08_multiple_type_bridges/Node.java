package cz.kul.snippets.hibernatesearch6.example08_multiple_type_bridges;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Node {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	public Node() {
	}

	public Node(String name) {
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
