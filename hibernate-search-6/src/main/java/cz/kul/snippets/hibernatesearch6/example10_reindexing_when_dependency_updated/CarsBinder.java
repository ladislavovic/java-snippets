package cz.kul.snippets.hibernatesearch6.example10_reindexing_when_dependency_updated;

import cz.kul.snippets.hibernatesearch6.example06_ca_proof_of_concept.CAStringValue;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.mapper.pojo.bridge.TypeBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.TypeBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.TypeBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.TypeBridgeWriteContext;

import java.util.List;

public class CarsBinder implements TypeBinder {

	@Override
	public void bind(TypeBindingContext context) {
//		context.dependencies()
//				.fromOtherEntity(Car.class, "owners")
//				.use("carName");
//		context.dependencies()
//				.fromOtherEntity(Car.class, "owners")
//				.use("maker");

		context.dependencies().use("myCars.carName");


		IndexFieldReference<String> cars = context.indexSchemaElement()
				.field("cars", f -> f.asString())
				.multiValued()
				.toReference();

		context.bridge(Person.class, new Bridge(cars));
	}

	private static class Bridge implements TypeBridge<Person> {

		private final IndexFieldReference<String> cars;

		private Bridge(
				IndexFieldReference<String> cars
		) {
			this.cars = cars;
		}

		@Override
		public void write(DocumentElement target, Person person, TypeBridgeWriteContext context) {
//			PersonService personService = ApplicationContextProvider.getBean(PersonService.class);
//			List<Car> ownedCars = personService.getOwnedCars(person);
//			for (Car car : ownedCars) {
//				target.addValue(cars, car.getCarName());
//			}
			target.addValue(cars, RandomStringUtils.randomAlphabetic(5));
		}

	}

}
