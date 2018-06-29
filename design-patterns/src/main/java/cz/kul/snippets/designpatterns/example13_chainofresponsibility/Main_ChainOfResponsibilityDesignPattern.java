package cz.kul.snippets.designpatterns.example13_chainofresponsibility;

/**
 * Client send a request to chain of objects. It does not know, which object will handle
 * the request. Object can handle the request, send request to another object in the chain
 * or both.
 * 
 * Examples:
 * <ul>
 * <li>Servlet filters. Here the filter does not have reference to its successor as usual,
 * but it gets FilterChain object, which have information about chain.</li>
 * <li>Objects in GUI. For example when I click on element in GUI, not every object can
 * handle the click, but it can send the request to parent object.</li>
 * </ul>
 * 
 * @author kulhalad
 * @since 7.4.5
 */
public class Main_ChainOfResponsibilityDesignPattern {

}
