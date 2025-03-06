package cz.kul.snippets.jpa.example05_onPersistEventListener.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "ORDER_TABLE")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String invoicetext;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="order", cascade = CascadeType.PERSIST)
	private Set<Item> items = new HashSet<>();

	public Order() {
	}

	public Order(String invoicetext) {
		this.invoicetext = invoicetext;
	}

	public void addItem(Item item) {
		item.setOrder(this);
		items.add(item);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInvoicetext() {
		return invoicetext;
	}

	public void setInvoicetext(String invoicetext) {
		this.invoicetext = invoicetext;
	}

	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", invoicetext=" + invoicetext + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Order order = (Order) o;
		return Objects.equals(id, order.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
