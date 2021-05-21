package cz.kul.snippets.hibernatesearch6.example04_type_bridge;

import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.mapper.pojo.bridge.TypeBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.TypeBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.TypeBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.TypeBridgeWriteContext;

public class AttributeBinder implements TypeBinder {

	@Override
	public void bind(TypeBindingContext context) {
		context.dependencies()
				.use("attributeSet");

		IndexFieldReference<String> attributes = context.indexSchemaElement()
				.field("attributes", f -> f.asString())
				.multiValued()
				.toReference();

		context.bridge(Person.class, new Bridge(attributes));
	}

	private static class Bridge implements TypeBridge<Person> {

		private final IndexFieldReference<String> attributes;

		private Bridge(IndexFieldReference<String> attributes) {
			this.attributes = attributes;
		}

		@Override
		public void write(DocumentElement target, Person person, TypeBridgeWriteContext context) {
			for (Attribute attribute : person.getAttributeSet().getAttributes()) {
				String value = attribute.getValue();
				target.addValue(attributes, value);
			}
		}

	}

}
