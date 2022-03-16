package cz.kul.snippets.hibernatesearch6.example11_range_search;

import org.hibernate.search.mapper.orm.mapping.HibernateOrmMappingConfigurationContext;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmSearchMappingConfigurer;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.ProgrammaticMappingConfigurationContext;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.TypeMappingStep;

public class MappingConfigurer implements HibernateOrmSearchMappingConfigurer {

	@Override
	public void configure(HibernateOrmMappingConfigurationContext context) {
		ProgrammaticMappingConfigurationContext mapping = context.programmaticMapping();
		TypeMappingStep personMapping = mapping.type(Person.class);
		personMapping
				.indexed()
				.index("person_index");
		personMapping
				.property( "name" )
				.fullTextField();

		// Note: you can use the same mapping for Integer number. The only difference is the mapping type in
		// Elasticsearch is "integer" instead of "long". But you can use the same search query.
		personMapping
				.property("age")
				.genericField();
	}

}
