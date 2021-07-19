package cz.kul.snippets.hibernatesearch6.example08_multiple_type_bridges;

import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.mapper.pojo.bridge.TypeBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.TypeBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.TypeBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.TypeBridgeWriteContext;

public class BinderEntityType implements TypeBinder {

	@Override
	public void bind(TypeBindingContext context) {
		context.dependencies().useRootOnly();

		IndexFieldReference<String> entityType = context.indexSchemaElement()
				.field("cross_entity_type", f -> f.asString())
				.toReference();

		context.bridge(Object.class, new Bridge(entityType));
	}

	private static class Bridge implements TypeBridge<Object> {

		private final IndexFieldReference<String> entityType;

		private Bridge(IndexFieldReference<String> entityType) {
			this.entityType = entityType;
		}

		@Override
		public void write(DocumentElement target, Object entity, TypeBridgeWriteContext context) {
			target.addValue(entityType, entity.getClass().getSimpleName());
		}

	}

}
