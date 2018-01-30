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

@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	/**
	 * Mapped by contains the name of the property on
	 * the owning entity (Address.customer)
	 */
	@OneToMany(
			fetch = FetchType.EAGER, 
			cascade = CascadeType.ALL, 
			mappedBy = "customer") // name of the property on the other side (Address.customer)
	private List<Address> addresses = new ArrayList<Address>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Customer [id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

}
