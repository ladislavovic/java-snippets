Smooks
====================

#### org.milyn.delivery.dom.DOMElementVisitor


#### SmooksResourceConfiguration

TODO
* java binding
* can I clean bean context during processing?
* ask how the orderItem beans wiring really works?
* if the visitor is not found smooks contiue. It is quite dangerous behaviour. Can I change that?
* report does not work
* TODO how to convert list of java objects to list of another java objects

NOTES
* execution context - one per "run"
* bean context -  one per execution context

A Smooks <b>Resource</b> is anything that can be used by Smooks in the process of analysing or
 * transforming a data stream.  They could be pieces
 * of Java logic (SAX or DOM element {@link Visitor} implementations), some text or script resource, or perhaps
 * simply a configuration parameter (see {@link org.milyn.cdr.ParameterAccessor}).
 