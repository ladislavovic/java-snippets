package cz.kul.snippets.hibernatesearch6.example05_embedded_with_routing_bridge;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Attribute {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String value;

	public Attribute() {
	}

	public Attribute(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private AttributeSet attributeSet;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public AttributeSet getAttributeSet() {
		return attributeSet;
	}

	public void setAttributeSet(AttributeSet attributeSet) {
		this.attributeSet = attributeSet;
	}
}
