package org.simple.work;

import org.simple.context.DefaultWorkflowResult;
import org.simple.context.WorkStatus;
import org.simple.context.WorkflowContext;
import org.simple.context.WorkflowResult;

public class EmptyWork implements Work {

    @Override
    public WorkflowResult execute(WorkflowContext workflowContext) {
        return new DefaultWorkflowResult(WorkStatus.SUCCESS, workflowContext);
    }
}
