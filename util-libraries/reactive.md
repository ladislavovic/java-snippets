Reactor
===============

* reactive streams are push based -  A Publisher can push new values to its Subscriber
 (by calling onNext) but can also signal an error (by calling onError) 
 or completion (by calling onComplete).
* backpressure
  * streams are tot clearly push based. Subscriber can send feedback to publisher than
    it is ready to gen another message (hybrid pull-push stragegy)
* hot vs cold
  * cold: starts anew for each subscriber
  * cold: If no subscription is created, data never gets generated. (Think of an HTTP request: Each new subscriber triggers an HTTP call, but no call is made if no one is interested in the result.)
  * hot: late subscribers receive signals emited after they subscribed
* onNext method of a Subscriber must not be called in parallel. So you can not have
  one subscriber for more producers.
* Unless specified, the topmost operator (the source) itself runs on the Thread in which the subscribe() call was made. 


TODO
*  it is based on Reactive Streams Specification. What is that?
