Lucene
================

Core Concepts
-----------------
### Indexing
Lucene uses an "inverted indexing" - it maps keywords to pages. It is just like a glossary at the end of the book.

### Document
It is a set of fields. Index typically consists of collection of documents.

TODO: can I have documents with different structure in one index?

### Field
TODO

### Analysis
It is a process of converting text into smaller parts (tokens) which are used for fulltext search. The process
usually involve removing common words (a, and, the, ...), removing punctuation, converting to lowercase etc.

Analysis is quite complex process and consists from more steps. They can be classified to the three basic groups:
pre-tokenization, tokenization, post-tokenization.

Pre-tokenization can be for example removing of html markup.

tokenization is splitting text into separated parts. Basically according to spaces and interpunction.

Post-tokenization is probably the largest group and can contain:
* stemming - replacing the word by its stem (kořen). For example bikes -> bike. It depends on language.
* stop words removing - remove common language words, which are not usefull for searching. For example in english
  it can be the, a, and, ...
* normalization - in english for example accent stripping, in czech it is diacritics removing
* synonyms detection

This general process is implemented by Lucene this way:
* Tokenizer class is responsible for breaking the input text into tokens (tokenization phase)
* TokenFilter can modify token stream and tokens content (post-tokenization phase)
* CharFilter can modify Reader objects, whihc is an input for analyse (pre-pokenization phase)
* Analyzer - they do not process text, it is a factory, which create CharFilters, Tokenizers and
  TokenFilters pipeline
  
### Lucene Analyzers

#### StandardAnalyzer
A simple Analyzer, which use StandardTokenizer and LowerCaseFilter and StopWordsFilter.


### Queries
Lucene queries are implementations of Query interface. There are many implementations, for example
TermQuery, WildcardQuery, ... you can combine them by BooleanQuery.
Another way how to create query is to use QueryParser and parse query string. The parser convert
query string into Query objects.