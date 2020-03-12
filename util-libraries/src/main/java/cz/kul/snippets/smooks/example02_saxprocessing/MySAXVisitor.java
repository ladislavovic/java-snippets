package cz.kul.snippets.smooks.example02_saxprocessing;

import cz.kul.snippets.agent.AgentManager;
import org.milyn.SmooksException;
import org.milyn.container.ExecutionContext;
import org.milyn.delivery.sax.SAXElement;
import org.milyn.delivery.sax.SAXVisitAfter;
import org.milyn.delivery.sax.SAXVisitBefore;

import java.io.IOException;

public class MySAXVisitor implements SAXVisitBefore, SAXVisitAfter {

    @Override
    public void visitAfter(SAXElement element, ExecutionContext executionContext) throws SmooksException, IOException {
        AgentManager.executeAgent(ExampleSAX.AGENT, element.getName());
    }

    @Override
    public void visitBefore(SAXElement element, ExecutionContext executionContext) throws SmooksException, IOException {
        AgentManager.executeAgent(ExampleSAX.AGENT, element.getName());
    }
}
