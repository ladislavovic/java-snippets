package cz.kul.snippets.hibernatesearch6.example03_embedded_with_reindexing;

import org.hibernate.search.mapper.orm.mapping.HibernateOrmMappingConfigurationContext;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmSearchMappingConfigurer;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.ProgrammaticMappingConfigurationContext;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.TypeMappingStep;
import org.hibernate.search.mapper.pojo.model.path.PojoModelPath;

public class MappingConfigurer implements HibernateOrmSearchMappingConfigurer {

	@Override
	public void configure(HibernateOrmMappingConfigurationContext context) {
		ProgrammaticMappingConfigurationContext mapping = context.programmaticMapping();
		TypeMappingStep personMapping = mapping.type(Person.class);
		personMapping.indexed().index("person_index");
		personMapping.property("name").fullTextField();
		personMapping.property("phones").indexedEmbedded();

		TypeMappingStep phoneMapping = mapping.type(Phone.class);
		phoneMapping.property("num").keywordField();
	}

}
