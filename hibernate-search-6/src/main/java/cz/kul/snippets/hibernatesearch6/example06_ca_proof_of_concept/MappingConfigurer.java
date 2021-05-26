package cz.kul.snippets.hibernatesearch6.example06_ca_proof_of_concept;

import org.hibernate.search.mapper.orm.mapping.HibernateOrmMappingConfigurationContext;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmSearchMappingConfigurer;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.ProgrammaticMappingConfigurationContext;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.TypeMappingStep;

public class MappingConfigurer implements HibernateOrmSearchMappingConfigurer {

	@Override
	public void configure(HibernateOrmMappingConfigurationContext context) {
		ProgrammaticMappingConfigurationContext mapping = context.programmaticMapping();

		TypeMappingStep personMapping = mapping.type(Person.class);
		personMapping.indexed().index("person_index");
		personMapping.binder(new CATypeBinder());
		personMapping.property("firstName").fullTextField();
//		personMapping.property("customAttributes").indexedEmbedded().includeDepth(2);

//		TypeMappingStep caSetMapping = mapping.type(CASet.class);
//		caSetMapping.indexed();
//		caSetMapping.property("id").keywordField();
//		caSetMapping.property("values").indexedEmbedded();

//		TypeMappingStep caValueMapping = mapping.type(CAValue.class);
//		caValueMapping.indexed();
//		caValueMapping.property("value").keywordField();
//		caValueMapping.property("name").keywordField();
	}

}
