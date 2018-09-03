package cz.kul.snippets.agent;

import java.util.ArrayList;
import java.util.List;

public class AgentLog {

    private int callCount;
    private List<CallLog> results = new ArrayList<>();

    public AgentLog() {
    }

    void logExecution(Object result, int callIndex) {
        callCount++;
        results.add(new CallLog(result, callIndex));
    }

    public int getCallCount() {
        return callCount;
    }

    public Object getResult(int index) {
        return results.get(index).result;
    }

    public Object getFirstResult() {
        return getResult(0);
    }

    public int getCallIndex(int index) {
       return results.get(index).callIndex;
    }

    public int getFirstCallIndex() {
        return getCallIndex(0);
    }

    private static class CallLog {
        Object result;
        int callIndex;

        public CallLog(Object result, int callIndex) {
            this.result = result;
            this.callIndex = callIndex;
        }
    }

}
