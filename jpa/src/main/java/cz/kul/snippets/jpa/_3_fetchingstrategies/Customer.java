package cz.kul.snippets.jpa._3_fetchingstrategies;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfile.FetchOverride;

@Entity
@FetchProfile(
		name = "profile1",
		fetchOverrides = {
			@FetchOverride(entity = Customer.class, association = "addresses", mode = FetchMode.JOIN)
		})
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;

	/**
	 * Mapped by contains the name of the property on
	 * the owning entity (Address.customer)
	 */
	@OneToMany(
			fetch = FetchType.LAZY, 
			cascade = CascadeType.ALL, 
			mappedBy = "customer") // name of the property on the other side (Address.customer)
	private List<Address> addresses = new ArrayList<Address>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + "]";
	}
	
}
