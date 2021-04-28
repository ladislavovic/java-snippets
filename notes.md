Notes
==================
This document contains aditional information to the snippets. Not all knowledge
can be written in snippets form so this document is for information like that.


Elasticsearch
========================

Terms
-------------------

### Index
Collection of documents of the same structure. For example you can have an
index for Customer Data, another one for Orders etc.

Index name must be lowercase.

Hibernate Search: HS creates one index per JPA entity

Lucene: ES index is composed from shards (TODO one shard per node?) and shard is
Lucene index. So ES index is collection of Lucene indexes.

### Type
You can define N types withing an index. A type is a logical category in the index.
Type vs another index: you must decide, if you store entities to one index and
distinguish them by type or create an index for each entity type. Both is possible.
Generally the one big index is more efficient than many small indexes so if you
can use type instead of another index. But use type, only when you store similar
data. If you have two entities with different structure it is better to use
another index. It is similar to deciding it to store data to one DB table and
distinguish them by a column called "type" or to use two separated DB tables.

Hibernate Search: TODO

Lucene: type is defined by the field with the name "_type". So it is just
another field in the document, nothing special.

### Shards & Replicas
ES can split index into shards. It allows distributing one index over more nodes.
When you search in that index, ES performs the search on all shards and merge
result. It is fully transparent to client.
By default, the index is split to 5 shards. You must define number of shards at
the index creating time, you can not change it later.
You can also replicate one shard to more nodes to achieve high availability.

Useful ES queries
--------------------------------
`GET /_cat/indices?v`
Get all indices

`/com.cross_ni.cross.db.pojo.core.node/_search?q=*&pretty`
Search

`DELETE /myindex?pretty`
Delete index myindex.

`curl -XDELETE localhost:9200/*`
Delete all indices

`PUT /myindex?pretty`
Create index "myindex"

`PUT /myindex/mytype/1?pretty
{
name: "John"
}`
Create a document in index myindex with type mytype and with ID 1.


Hibernate Search
==========================
* when you index more siblings, there is one index per each sibling. Example:
  You have a class "AbstractPerson" and two siblings "PhysicalPerson" and
  "ArtificialPerson". When you index both siblings then there are two
  indices in the elasticsearch cluster - one per each sibling.
  
* When @IndexedEmbedded points to an entity, the association has to be
  bi-directional and the other side has to be annotated with @ContainedIn.
  If not, Hibernate Search has no way to update the root index when the
  associated entity is updated


Lucene
================

Features
----------------
* simultaneous searching and index update
* multiple-index searching with merged results

Core Concepts
-----------------
### Indexing
Lucene uses an "inverted indexing" - it maps keywords to pages. It is just like a glossary at the end of the book.
Typically index size is about 20 - 30% of the indexed text. On modern hardware (year 2021) the indexing speed is
about 150 GB/s.

### Document
It is a set of fields. Index typically consists of collection of documents.

Document has its ID. But it is an internal value, you can not set its value or even read it (maybe it is possible some hack/low level code, but not by standard way).

Documents in the same index does not have to have the same fields. So there is no
field structure which all documents must have, it is not like database table. Anyway
usually document usually have very similar fields structure.

### Field
TODO

### Analysis
It is a process of converting text into smaller parts (tokens) which are used for fulltext search. The process
usually involve removing common words (a, and, the, ...), removing punctuation, converting to lowercase etc.

Analysis is quite complex process and consists from more steps. They can be classified to the three basic groups:

1. pre-tokenization
Pre-tokenization can be for example removing of html markup.

2. tokenization
It is a splitting text into separated parts. Basically according to spaces and interpunction.

3. post-tokenization
It is probably the largest group and can contain:
    * stemming - replacing the word by its stem (kořen). For example bikes -> bike. It depends on language.
    * stop words removing - remove common language words, which are not usefull for searching. For example in english
      it can be the, a, and, ...
    * normalization - in english for example accent stripping, in czech it is diacritics removing
    * synonyms detection

This general process is implemented by Lucene this way:
* Tokenizer class is responsible for breaking the input text into tokens (tokenization phase)
* TokenFilter can modify token stream and tokens content (post-tokenization phase)
* CharFilter can modify Reader objects, which is an input for analyse (pre-pokenization phase)
* Analyzer - they do not process text, it is a factory, which create CharFilters, Tokenizers and
  TokenFilters pipeline

Lucene Analyzers
--------------------------

### StandardAnalyzer
A simple Analyzer, which use StandardTokenizer and LowerCaseFilter and StopWordsFilter.

Queries
------------------
* Lucene queries are Java objects implementing Query interface. There are many
implementations, for example
TermQuery, WildcardQuery, ... you can combine them by BooleanQuery.
* Another way how to create query is to use QueryParser and parse query string. The parser convert
query string into Query objects.
* QueryParser vs creating query programmatically by API: If you are programmatically generating a query string
  and then parsing it with the query parser then you should seriously consider building your queries directly
  with the query API. In other words, the query parser is designed for human-entered text, 
  not for program-generated text.
* QueryParser specify "default field" (it is a constructor param). If you do not specify field in the
  query parser expression then the default field is used.
