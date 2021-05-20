package cz.kul.snippets.hibernatesearch6.example02_method_property;

import org.hibernate.search.mapper.orm.mapping.HibernateOrmMappingConfigurationContext;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmSearchMappingConfigurer;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.ProgrammaticMappingConfigurationContext;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.TypeMappingStep;
import org.hibernate.search.mapper.pojo.model.path.PojoModelPath;

public class MappingConfigurer implements HibernateOrmSearchMappingConfigurer {

	@Override
	public void configure(HibernateOrmMappingConfigurationContext context) {
		ProgrammaticMappingConfigurationContext mapping = context.programmaticMapping();
		TypeMappingStep personMapping = mapping.type(Person.class);
		personMapping
				.indexed()
				.index("person_index");

		// wholeName value is not get from the field but from the getter. Then HS6 require
		// definition what properties it depends on. Changing any of dependency trigger
		// reindexing.
		//
		// This configuration is incorrect, because "wholeName" depends also on "surname". But it allows
		// to test when you change "surname" the index is not updated.
		personMapping
				.property("wholeName")
				.fullTextField()
				.indexingDependency().derivedFrom(PojoModelPath.parse("name"));
	}

}
