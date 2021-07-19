package cz.kul.snippets.hibernatesearch6.example09_typical_fields_with_bridge;

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
		personMapping.binder(new MyTypeBinder());
		personMapping
				.indexed()
				.index("person_09_index");

		personMapping
				.property("name")
				.fullTextField("name_fulltext");

		personMapping
				.property("name")
				.genericField("name_generic");

		personMapping
				.property("name")
				.keywordField("name_keyword");
	}

}
