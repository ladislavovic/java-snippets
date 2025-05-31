package cz.kul.snippets.agent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

class Agents {

    private Map<String, Agent> agents = new HashMap<>();

    public void addAgent(String name, Function<Object, Object> fnc) {
        agents.put(name, new Agent(name, fnc));
    }

    public void clear() {
        agents.clear();
    }

    public Agent getAgent(String name) {
        return agents.get(name);
    }

    public void execute(String name, Object input) {
        Agent agent = getAgent(name);
        agent.execute(input);
    }
    
    public Set<String> getAgents() {
        return agents.keySet();
    }

}
