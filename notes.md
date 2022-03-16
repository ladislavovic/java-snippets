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

### Mapping
* It is a definition how the document fields are indexed and stored.
* Each type in index has its own mapping. So there can be several mappings per index.
* Fields with the same name in different mapping types in the same index must have the same mapping.


### Shards & Replicas
ES can split index into shards. It allows distributing one index over more nodes.
When you search in that index, ES performs the search on all shards and merge
result. It is fully transparent to client.
By default, the index is split to 5 shards. You must define number of shards at
the index creating time, you can not change it later.
You can also replicate one shard to more nodes to achieve high availability.

### Searching
* by default the search result contains "_source" field, which contain the whole
  found document. But you can specify which fields from _source you want to get.
* bool query has `filter`. It allows filter documents according criteria withou
  affecting result score.
  
### Misc
* there is versioning support. When you update a document the version is incremented.
  Or you can set explicit version number.
  
### Search Queries
```
{
    "query": {
        "match" : {
            "message" : "this is a test",
            "operator": "and",
            "analyzer": "analyzer_name"
        }
    }
}
```
  
### Stored fields vs _source
By default in elasticsearch, the _source (the document one indexed) is stored. This means when you search, you can get
the actual document source back. Moreover, elasticsearch will automatically extract fields/objects from the _source
and return them if you explicitly ask for it (as well as possibly use it in other components, like highlighting).

You can specify that a specific field is also stored. This means that the data for that field will be stored on its own.
Meaning that if you ask for field1 (which is stored), elasticsearch will identify that its stored, and load it from
the index instead of getting it from the _source (assuming _source is enabled).

When do you want to enable storing specific fields? Most times, you don't. Fetching the _source is fast and extracting
it is fast as well. If you have very large documents, where the cost of storing the _source, or the cost of parsing
the _source is high, you can explicitly map some fields to be stored instead.

Note, there is a cost of retrieving each stored field. So, for example, if you have a json with 10 fields with
reasonable size, and you map all of them as stored, and ask for all of them, this means loading each one (more disk
seeks), compared to just loading the _source (which is one field, possibly compressed).

Keep in mind that when you disable _source it will mean:
* You won’t be able to do partial updates
* You won’t be able to re-index your data from the JSON in your Elasticsearch cluster, you’ll have to re-index from
  the data source (which is usually a lot slower).

Useful ES API calls
--------------------------------
`GET /_cat/indices?v`
Get all indices

`PUT /customer?pretty`
Create an index 'customer'

```
PUT /customer/external/1?pretty
{
  "name": "John Doe"
}
```
Add item to the index. Index: customer, type: external, id: 1"
Id part is optional. If not specified ES generate random id.

`GET /com.cross_ni.cross.db.pojo.core.node/_search?q=*&pretty`
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

`GET /com.cross_ni.cross.db.pojo.core.node/_mapping`
Get the index mapping.

```
GET _analyze
{
  "analyzer" : "standard",
  "text" : "this is a test"
}
```
Analyze the given text according to the given analyzer.

```
GET _analyze
{
  "tokenizer" : "keyword",
  "filter" : ["lowercase"],
  "char_filter" : ["html_strip"],
  "text" : "this is a <b>test</b>"
}
```
Analyze the given text according to given char filter, tokenizer and token filter (token filters are in "filter"
property).


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
  
* at always reindex whole document. Does not matter how many indexed
  properties you change, it always create whole fulltext document and
  reindex it.

Kafka
====================
Core Concepts
-------------------
### Basic terms
**event** - an object which describes a change in a system. An event has a key,
value, timestamp, and optional metadata headers.
In Kafka world, it is called event. Not message or item but event.

**topic**
* Topic is am ordered collection of events stored in a durable way. 
So it is stored on disc. Topic can be small or large. They can remember 
data just for a little while, for an hour, day, year or forever.
* always multi producers and multi subscribers
* events are not deleted after consumption

**publish/subscribe** - write/read events to/from the topic

**Kafka broker** - ???

### Kafka Connect
Kafka Connect is a framework for connecting Kafka with external systems such
as databases, key-value stores, search indexes, and file systems,
using so-called Connectors.

Kafka Connectors are ready-to-use components, which can help us to import data
from external systems into Kafka topics and export data from Kafka topics into
external systems.

A source connector collects data from a system. Source systems can be entire
databases, streams tables, or message brokers. A source connector could also
collect metrics from application servers into Kafka topics, making the data
available for stream processing with low latency.

A sink connector delivers data from Kafka topics into other systems,
which might be indexes such as Elasticsearch, batch systems such as Hadoop,
or any kind of database.

In practise Kafka Connect is a standalone process. It has several connectors
and from configuration it knows their classes. The configuration is stored
locally for standalone approach. You can have also distributed Kafka Connect,
so several processes is executed. Then the configuration is stored in Kafka
topic.

### Topic partitioning
* events with the same id are always written to the same topic partition

### Guarantees
* events from topic partition are read in the same order as they were written

### Notes
* A common production setting is a replication factor of 3, i.e., there will  
always be three copies of your data.
* Kafka is not waiting for disk flush by default. The durability is realized
through replication.
* By default, Kafka acknowledge the message when acks=1. It is good for latency,
but it is dangerous for durability. For best durability use acks=all








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
  
Misc
--------------------
* use numeric fields (IntField, LongField, ...) only for attributes which
  represents quantity. Because Lucene create an additional data structures
  for them which allows sorting and reange queries. For example when you
  index an entity id, which is usually Long Java data type, use normal
  StringField for that.


Spring Boot
============================
* @SpringBootApplication - encapsulates three stuffs:
  * @Configuration - marks a class as a source of Spring beans,
  * @ComponentsScan - tells Spring Boot to scan the current package (with all sub-packages) for components,
  * @EnableAutoConfiguration - enables Spring Boot to auto-configure the application based on included jar files in the classpath.

* Starters - Starters are an official Spring Boot dependency descriptors created to facilitate extending the application with new functionalities. For example, if you want to integrate your Spring application with a non-relational database - MongoDB, simply include the spring-boot-starter-data-mongodb dependency in your project, and you are good to go.


todo: 
spring-boot-starter-actuator
