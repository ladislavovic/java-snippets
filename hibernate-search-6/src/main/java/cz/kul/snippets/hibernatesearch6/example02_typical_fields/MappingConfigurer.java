package cz.kul.snippets.hibernatesearch6.example02_typical_fields;

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

		// 1. Normal fulltext field
		//
		// It is just common String field which you want to use for search
		personMapping
				.property("name")
				.fullTextField();

		// 2. Keyword field
		//
		// It is a field which is stored in fulltext index but it is not analyzed text. It can be
		// number, string constants, boolean, ...
		personMapping
				.property("age")
				.genericField();

		// 3. Property derived from another fields, indexed during change
		//
		// wholeName value is not get from the field but from the getter. Then HS6 require
		// definition what properties it depends on. Changing any of dependency trigger
		// reindexing.
		//
		// This configuration is incorrect, because "wholeName" depends also on "surname". But it allows
		// you testing when you change "surname" the index is not updated.
		personMapping
				.property("wholeName")
				.fullTextField()
				.indexingDependency()
				.derivedFrom(PojoModelPath.parse("name"));

		// 4. Method not derived from another fields
		//
		// Sometimes you have a method which generate some value for fulltext but
		// you can not just simply derive it from another fields or you do not
		// want to reindex it because of performance reasons (ManyToOne relation)
		personMapping
				.property("currentDate")
				.fullTextField()
				.indexingDependency()
				.reindexOnUpdate(ReindexOnUpdate.NO);

	}

}
