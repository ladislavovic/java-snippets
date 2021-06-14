package cz.kul.snippets.hibernatesearch6.example06_ca_proof_of_concept;

import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.mapper.pojo.bridge.TypeBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.TypeBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.TypeBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.TypeBridgeWriteContext;
import org.hibernate.search.mapper.pojo.extractor.builtin.BuiltinContainerExtractors;
import org.hibernate.search.mapper.pojo.model.path.PojoModelPath;

public class CAValueTypeBinder implements TypeBinder {

	@Override
	public void bind(TypeBindingContext context) {
		context.dependencies()
				.use("name")
				.use("value");

		context.dependencies()
				.use( PojoModelPath.builder()
						.property( "priceByEdition" )
						.value( BuiltinContainerExtractors.MAP_KEY )
						.property( "label" )
						.toValuePath() );

		context.bridge(CAValue.class, new Bridge());
	}

	private static class Bridge implements TypeBridge<CAValue> {

		private Bridge() {
		}

		@Override
		public void write(DocumentElement target, CAValue person, TypeBridgeWriteContext context) {
			;
		}

	}

}
