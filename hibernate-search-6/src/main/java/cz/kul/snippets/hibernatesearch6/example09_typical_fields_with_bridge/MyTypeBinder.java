package cz.kul.snippets.hibernatesearch6.example09_typical_fields_with_bridge;

import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.mapper.pojo.bridge.TypeBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.TypeBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.TypeBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.TypeBridgeWriteContext;

public class MyTypeBinder implements TypeBinder {

	@Override
	public void bind(TypeBindingContext context) {
		context.dependencies().useRootOnly();

		IndexFieldReference<String> bridgeField1 = context.indexSchemaElement()
				.field("keywordBridgeField", f -> f.asString())
				.toReference();

		IndexFieldReference<String> bridgeField2 = context.indexSchemaElement()
				.field("analyzedBridgeField", f -> f.asString().analyzer("standard"))
				.toReference();

		context.bridge(Person.class, new Bridge(bridgeField1, bridgeField2));
	}

	private static class Bridge implements TypeBridge<Person> {

		private final IndexFieldReference<String> bridgeField1;
		private final IndexFieldReference<String> bridgeField2;

		private Bridge(
				IndexFieldReference<String> bridgeField1,
				IndexFieldReference<String> bridgeField2
		) {
			this.bridgeField1 = bridgeField1;
			this.bridgeField2 = bridgeField2;
		}

		@Override
		public void write(DocumentElement target, Person person, TypeBridgeWriteContext context) {
			target.addValue(bridgeField1, "f1val");
			target.addValue(bridgeField2, "f2val");
		}

	}

}
