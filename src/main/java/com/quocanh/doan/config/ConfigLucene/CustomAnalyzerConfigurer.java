package com.quocanh.doan.config.ConfigLucene;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.en.EnglishPossessiveFilterFactory;
import org.apache.lucene.analysis.en.PorterStemFilterFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;
import org.springframework.stereotype.Component;

@Component
public class CustomAnalyzerConfigurer implements LuceneAnalysisConfigurer {

    @Override
    public void configure(LuceneAnalysisConfigurationContext context) {
        context.analyzer("vietnameseAnalyzer").custom()
                .tokenizer(StandardTokenizerFactory.class)
                .tokenFilter(LowerCaseFilterFactory.class)
                .tokenFilter(ASCIIFoldingFilterFactory.class)
                .tokenFilter(StopFilterFactory.class)
                .param("words", "stopwords_vi.txt")
                .tokenFilter(WordDelimiterGraphFilterFactory.class);

        context.analyzer("englishAnalyzer").custom()
                .tokenizer(StandardTokenizerFactory.class)
                .tokenFilter(LowerCaseFilterFactory.class)
                .tokenFilter(StopFilterFactory.class)
                .tokenFilter(EnglishPossessiveFilterFactory.class)
                .tokenFilter(PorterStemFilterFactory.class);
    }
}
