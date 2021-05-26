package cz.kul.snippets.hibernatesearch6.example07_node_alias_PoC;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SecondaryTable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Node {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "node")
	private Set<NodeAlias> aliases = new HashSet<>();

	public Node() {
	}

	public Node(String name) {
		this.name = name;
	}

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

	public Set<NodeAlias> getAliases() {
		return aliases;
	}

	public void setAliases(Set<NodeAlias> aliases) {
		this.aliases = aliases;
	}

	public Set<String> getAliasesStr() {
		return aliases.stream().map(NodeAlias::getAlias).collect(Collectors.toSet());
	}

}
