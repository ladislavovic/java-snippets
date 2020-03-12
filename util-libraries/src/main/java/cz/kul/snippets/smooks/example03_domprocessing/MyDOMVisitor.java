package cz.kul.snippets.smooks.example03_domprocessing;

import cz.kul.snippets.agent.AgentManager;
import org.milyn.SmooksException;
import org.milyn.container.ExecutionContext;
import org.milyn.delivery.dom.DOMElementVisitor;
import org.w3c.dom.Element;

public class MyDOMVisitor implements DOMElementVisitor {

    @Override
    public void visitBefore(Element element, ExecutionContext executionContext) throws SmooksException {
        AgentManager.executeAgent(ExampleDOM.AGENT, element.getTagName());
    }
    
    @Override
    public void visitAfter(Element element, ExecutionContext executionContext) throws SmooksException {
        AgentManager.executeAgent(ExampleDOM.AGENT, element.getTagName());
    }
    
}
