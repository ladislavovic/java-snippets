package cz.kul.snippets.hibernatesearch6.example10_reindexing_when_dependency_updated;

import org.hibernate.search.mapper.orm.mapping.HibernateOrmMappingConfigurationContext;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmSearchMappingConfigurer;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.ProgrammaticMappingConfigurationContext;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.TypeMappingStep;

public class MappingConfigurer implements HibernateOrmSearchMappingConfigurer {

	@Override
	public void configure(HibernateOrmMappingConfigurationContext context) {
		ProgrammaticMappingConfigurationContext mapping = context.programmaticMapping();
		TypeMappingStep personMapping = mapping.type(Person.class);
		personMapping.binder(new CarsBinder());
		personMapping
				.indexed()
				.index("person_10_index");

		personMapping
				.property("name")
				.fullTextField("name");
	}

}
