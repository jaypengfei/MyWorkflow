package org.simple.context;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WorkflowContext {

    private final Map<String, Object> context = new ConcurrentHashMap<>();

    public void put(String key, Object value) {
        context.put(key, value);
    }

    public Object get(String key) {
        return context.get(key);
    }

    public Object getOrDefault(String key, Object obj) {
        return context.getOrDefault(key, obj);
    }

    public boolean containsKey(String key) {
        return context.containsKey(key);
    }

    public Set<Map.Entry<String, Object>> getEntrySet() {
        return context.entrySet();
    }

    @Override
    public String toString() {
        return "DefaultWorkflowContext{" +
                "context=" + context +
                '}';
    }
}
