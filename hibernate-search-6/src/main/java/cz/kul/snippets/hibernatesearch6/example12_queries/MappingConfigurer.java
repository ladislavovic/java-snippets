package cz.kul.snippets.hibernatesearch6.example12_queries;

import org.hibernate.search.engine.backend.types.Projectable;
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
				.fullTextField()
				.projectable(Projectable.YES);


		// Note: you can use the same mapping for Integer number. The only difference is the mapping type in
		// Elasticsearch is "integer" instead of "long". But you can use the same search query.
		personMapping
				.property("age")
				.genericField()
				.projectable(Projectable.YES);

		TypeMappingStep employeeMapping = mapping.type(Employee.class);
		employeeMapping.indexed();
		employeeMapping.property("name").fullTextField();
	}

}
