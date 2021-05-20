package cz.kul.snippets.hibernatesearch6.example08_collection_indexing;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

import java.util.Arrays;
import java.util.List;

public class StringListBridge implements TwoWayFieldBridge, FieldBridge {

	@Override
	public void set(String fieldName, Object value, Document document, LuceneOptions luceneOptions) {
		List<String> list = (List<String>) value;
		if (list != null) {
			for (String s : list) {
				luceneOptions.addFieldToDocument(fieldName, s, document);
			}
		}
	}

	@Override
	public Object get(String fieldName, Document document) {
		String[] values = document.getValues(fieldName);
		return Arrays.asList(values);
	}

	@Override
	public String objectToString(Object object) {
		return null;
	}
}
