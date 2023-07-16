package org.simple.core;

import org.simple.work.Work;

public class WorkflowEngineBuilder {

    private WorkflowEngineBuilder() {}

    public static WorkflowEngineBuilder newWorkflowEngine() {
        return new WorkflowEngineBuilder();
    }

    public WorkflowEngineImpl build() {
        return new WorkflowEngineImpl();
    }
}
