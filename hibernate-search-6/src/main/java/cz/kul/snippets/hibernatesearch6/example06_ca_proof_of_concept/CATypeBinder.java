package cz.kul.snippets.hibernatesearch6.example06_ca_proof_of_concept;

import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.mapper.pojo.bridge.TypeBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.TypeBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.TypeBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.TypeBridgeWriteContext;

public class CATypeBinder implements TypeBinder {

	@Override
	public void bind(TypeBindingContext context) {
		context.dependencies()
				.fromOtherEntity(CAStringValue.class, "caSet.person")
				.use("stringValue");
		context.dependencies()
				.fromOtherEntity(CAIntegerValue.class, "caSet.person")
				.use("integerValue");

		IndexFieldReference<String> customAttributes = context.indexSchemaElement()
				.field("customAttributesField", f -> f.asString())
				.multiValued()
				.toReference();

		IndexFieldReference<String> ca_attr1 = context.indexSchemaElement()
				.field("ca_attr1", f -> f.asString())
				.toReference();

		IndexFieldReference<String> ca_attr2 = context.indexSchemaElement()
				.field("ca_attr2", f -> f.asString())
				.toReference();

		context.bridge(Person.class, new Bridge(customAttributes, ca_attr1, ca_attr2));
	}

	private static class Bridge implements TypeBridge<Person> {

		private final IndexFieldReference<String> customAttributes;
		private final IndexFieldReference<String> attr1;
		private final IndexFieldReference<String> attr2;

		private Bridge(
				IndexFieldReference<String> customAttributes,
				IndexFieldReference<String> attr1,
				IndexFieldReference<String> attr2
		) {
			this.customAttributes = customAttributes;
			this.attr1 = attr1;
			this.attr2 = attr2;
		}

		@Override
		public void write(DocumentElement target, Person person, TypeBridgeWriteContext context) {
			for (CAValue caValue : person.getCustomAttributes().getValues()) {
				Object value = caValue.getValue();
				if (value != null) {
					target.addValue(customAttributes, value.toString());
				}
			}
			target.addValue(attr1, "hardcoded");
		}

	}

}
