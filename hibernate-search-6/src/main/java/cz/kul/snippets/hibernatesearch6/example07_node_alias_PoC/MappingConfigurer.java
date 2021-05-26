package cz.kul.snippets.hibernatesearch6.example07_node_alias_PoC;

import cz.kul.snippets.hibernatesearch6.example06_ca_proof_of_concept.CATypeBinder;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmMappingConfigurationContext;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmSearchMappingConfigurer;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.ProgrammaticMappingConfigurationContext;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.TypeMappingStep;
import org.hibernate.search.mapper.pojo.model.path.PojoModelPath;

public class MappingConfigurer implements HibernateOrmSearchMappingConfigurer {

	@Override
	public void configure(HibernateOrmMappingConfigurationContext context) {
		ProgrammaticMappingConfigurationContext mapping = context.programmaticMapping();

		TypeMappingStep nodeMapping = mapping.type(Node.class);
		nodeMapping.indexed().index("node_index");
		nodeMapping.property("name").fullTextField();
		nodeMapping
				.property("aliasesStr")
				.fullTextField()
				.indexingDependency()
				.derivedFrom(PojoModelPath.parse("aliases.alias"));
	}

}
