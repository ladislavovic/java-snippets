package cz.kul.snippets.hibernatesearch6.example05_elasticsearch;

import org.hibernate.search.elasticsearch.analyzer.definition.ElasticsearchAnalysisDefinitionProvider;
import org.hibernate.search.elasticsearch.analyzer.definition.ElasticsearchAnalysisDefinitionRegistryBuilder;

public class AnalyzerDefinitionProvider implements ElasticsearchAnalysisDefinitionProvider {

	@Override
	public void register(ElasticsearchAnalysisDefinitionRegistryBuilder builder) {
		builder
				.analyzer("MY_DEFAULT_ANALYZER")
				.withTokenizer("standard")
				.withTokenFilters("asciifolding");
	}

}