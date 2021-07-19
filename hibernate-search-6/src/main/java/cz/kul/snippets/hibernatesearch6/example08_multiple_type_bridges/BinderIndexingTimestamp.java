package cz.kul.snippets.hibernatesearch6.example08_multiple_type_bridges;

import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.mapper.pojo.bridge.TypeBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.TypeBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.TypeBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.TypeBridgeWriteContext;

public class BinderIndexingTimestamp implements TypeBinder {

	@Override
	public void bind(TypeBindingContext context) {
		context.dependencies().useRootOnly();

		IndexFieldReference<Long> indexingTimestamp = context.indexSchemaElement()
				.field("cross_indexing_timestamp", f -> f.asLong())
				.toReference();

		context.bridge(Object.class, new Bridge(indexingTimestamp));
	}

	private static class Bridge implements TypeBridge<Object> {

		private final IndexFieldReference<Long> indexingTimestamp;

		private Bridge(IndexFieldReference<Long> indexingTimestamp) {
			this.indexingTimestamp = indexingTimestamp;
		}

		@Override
		public void write(DocumentElement target, Object entity, TypeBridgeWriteContext context) {
			target.addValue(indexingTimestamp, System.currentTimeMillis());
		}

	}

}
