package cz.kul.snippets.hibernatesearch6.example08_multiple_type_bridges;

import org.hibernate.search.mapper.orm.mapping.HibernateOrmMappingConfigurationContext;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmSearchMappingConfigurer;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.ProgrammaticMappingConfigurationContext;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.TypeMappingStep;

public class MappingConfigurer implements HibernateOrmSearchMappingConfigurer {

	@Override
	public void configure(HibernateOrmMappingConfigurationContext context) {
		ProgrammaticMappingConfigurationContext mapping = context.programmaticMapping();

		TypeMappingStep nodeMapping = mapping.type(Node.class);
		nodeMapping.indexed();
		nodeMapping.binder(new BinderEntityType());
		nodeMapping.binder(new BinderIndexingTimestamp());
		nodeMapping.property("name").fullTextField();

		TypeMappingStep linkMapping = mapping.type(Link.class);
		linkMapping.indexed();
		linkMapping.binder(new BinderEntityType());
		linkMapping.binder(new BinderIndexingTimestamp());
		linkMapping.property("name").fullTextField();

	}

}
