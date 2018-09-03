package cz.kul.snippets.agent;

import java.util.function.Function;

public class Agent {

    private static int callIndex = 0;

    String name;
    Function<Object, Object> operation;
    AgentLog log;

    public Agent(String name, Function<Object, Object> operation) {
        this.name = name;
        this.operation = operation;
        this.log = new AgentLog();
    }

    public String getName() {
        return name;
    }

    public void execute(Object input) {
        Object result = operation.apply(input);
        log.logExecution(result, getNextCallIndex());
    }

    public AgentLog getLog() {
        return log;
    }

    synchronized private int getNextCallIndex() {
        return callIndex++;
    }
}
