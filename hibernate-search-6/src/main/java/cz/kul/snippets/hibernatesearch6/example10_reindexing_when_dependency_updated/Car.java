package cz.kul.snippets.hibernatesearch6.example10_reindexing_when_dependency_updated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.List;

@Entity
public class Car {

	@Id
	@GeneratedValue
	private Long id;

	private Long ownerPersonId;

	private String carName;

	private String maker;

	@Version
	private long optlckVersion;

	public Car() {
	}

	public Car(Long ownerPersonId, String carName, String maker) {
		this.ownerPersonId = ownerPersonId;
		this.carName = carName;
		this.maker = maker;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOwnerPersonId() {
		return ownerPersonId;
	}

	public void setOwnerPersonId(Long ownerPersonId) {
		this.ownerPersonId = ownerPersonId;
	}

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public List<Person> getOwners() {
		PersonService personService = ApplicationContextProvider.getBean(PersonService.class);
		List<Person> owner = personService.getCarOwner(this);
		return owner;
	}

}
