package cz.kul.snippets.hibernatesearch6.example06_embedded_collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class Car implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	String make;

	@Column(nullable = false)
	String model;

	@ManyToOne(fetch = FetchType.LAZY)
	Person owner;

	public Car() {
	}

	public Car(String make, String model) {
		this.make = make;
		this.model = model;
	}

	public Car(String make, String model, Person owner) {
		this.make = make;
		this.model = model;
		this.owner = owner;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}
}
