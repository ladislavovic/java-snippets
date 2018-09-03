package cz.kul.snippets.agent;

import java.util.function.Function;

public class AgentManager {

    private static final ThreadLocal<Agents> agents = new ThreadLocal<Agents>() {
        @Override
        protected Agents initialValue() {
            return new Agents();
        }
    };

    public static void addAgent(String name, Function<Object, Object> op) {
        getAgents().addAgent(name, op);
    }

    public static void executeAgent(String name, Object input) {
        getAgents().execute(name, input);
    }

    public static AgentLog getAgentLog(String name) {
        return getAgents().getAgent(name).getLog();
    }

    public static void clear() {
        agents.get().clear();
    }

    private static Agents getAgents() {
        return agents.get();
    }
}
