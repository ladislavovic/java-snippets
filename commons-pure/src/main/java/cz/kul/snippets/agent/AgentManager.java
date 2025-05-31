package cz.kul.snippets.agent;

import java.util.function.Function;

/**
 *  TODO make usage easier and docement it
 *  TODO make agents generic
 */
public class AgentManager {

    // TODO Do I need store that in TL? I have problem with quartz with that, because they run
    // in another thread.
    // TODO Yes, I definitelly must store it on another plce, it is not useable for any multithread code
    private static final ThreadLocal<Agents> agents = new ThreadLocal<Agents>() {
        @Override
        protected Agents initialValue() {
            return new Agents();
        }
    };

    public static void addAgent(String name, Function<Object, Object> op) {
        getAgents().addAgent(name, op);
    }
    
    public static void addAgentIfNotPresent(String name, Function<Object, Object> op) {
        for (String agent : getAgents().getAgents()) {
            if (agent.equals(name)) {
                return;
            }
        }
        addAgent(name, op);
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
