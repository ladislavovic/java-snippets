package cz.kul.snippets.hibernatesearch6.example07_node_alias_PoC;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.HashSet;
import java.util.Set;

@Entity
public class NodeAlias {

	@Id
	@GeneratedValue
	private Long id;

	private String alias;

	@ManyToOne(fetch = FetchType.LAZY)
	private Node node;

	public NodeAlias() {
	}

	public NodeAlias(String alias, Node node) {
		this.alias = alias;
		this.node = node;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Node getPerson() {
		return node;
	}

	public void setPerson(Node node) {
		this.node = node;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

}
