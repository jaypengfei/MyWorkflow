package org.simple.core;

import org.simple.context.WorkflowContext;
import org.simple.context.WorkflowResult;
import org.simple.work.Work;

public class WorkflowEngineImpl implements WorkflowEngine {
    @Override
    public WorkflowResult run(Work work, WorkflowContext workflowContext) {
        return work.execute(workflowContext);
    }
}
