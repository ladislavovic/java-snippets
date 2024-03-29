package cz.kul.snippets.java.java8features;

import java.util.Optional;

public class User {

	private InvoiceData invoiceData;

	public InvoiceData getInvoiceData() {
		return invoiceData;
	}

	public void setInvoiceData(InvoiceData invoiceData) {
		this.invoiceData = invoiceData;
	}

	public Optional<InvoiceData> getInvoiceDataOpt() {
		return Optional.ofNullable(invoiceData);
	}

}

class InvoiceData {

	private Address address;

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}

class Address {

	private String addressString;

	public String getAddressString() {
		return addressString;
	}

	public void setAddressString(String addressString) {
		this.addressString = addressString;
	}

}
