package cz.kul.snippets.hibernatesearch6.example05_embedded_with_routing_bridge;

import org.hibernate.search.mapper.orm.mapping.HibernateOrmMappingConfigurationContext;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmSearchMappingConfigurer;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.ProgrammaticMappingConfigurationContext;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.TypeMappingStep;

public class MappingConfigurer implements HibernateOrmSearchMappingConfigurer {

	@Override
	public void configure(HibernateOrmMappingConfigurationContext context) {
		ProgrammaticMappingConfigurationContext mapping = context.programmaticMapping();

		TypeMappingStep attributeSetMapping = mapping.type(AttributeSet.class);
		attributeSetMapping.indexed();
		attributeSetMapping.property("attributes").indexedEmbedded();

		TypeMappingStep attributeMapping = mapping.type(Attribute.class);
		attributeMapping.indexed().index("attribute_index").routingBinder(new AttributeRoutingBinder());
		attributeMapping.property("name").keywordField();
		attributeMapping.property("value").fullTextField();
	}

}
