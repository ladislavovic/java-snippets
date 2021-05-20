package cz.kul.snippets.hibernatesearch6.example07_embedded_by_bridge;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

import java.util.Set;

public class PersonClassBridge implements FieldBridge {

	@Override
	public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
		Person person = (Person) value;
		luceneOptions.addFieldToDocument("b_name", person.getName(), document);
		luceneOptions.addFieldToDocument("b_whole_name", person.getWholeName(), document);
		Set<Car> cars = person.getCars();
		if (cars != null) {
			for (Car car : cars) {
				luceneOptions.addFieldToDocument("b_car_model", car.getModel(), document);
			}
		}
	}

}
