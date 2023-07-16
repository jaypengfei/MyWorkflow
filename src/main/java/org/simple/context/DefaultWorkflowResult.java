package org.simple.context;

public class DefaultWorkflowResult implements WorkflowResult {

    private final WorkStatus workStatus;
    private final WorkflowContext workflowContext;
    private Throwable error;

    public DefaultWorkflowResult(WorkStatus workStatus, WorkflowContext workflowContext) {
        this.workStatus = workStatus;
        this.workflowContext = workflowContext;
    }

    public DefaultWorkflowResult(WorkStatus workStatus, WorkflowContext workflowContext, Throwable error) {
        this(workStatus, workflowContext);
        this.error = error;
    }

    @Override
    public WorkStatus getStatus() {
        return workStatus;
    }

    @Override
    public WorkflowContext getContext() {
        return workflowContext;
    }

    @Override
    public Throwable getError() {
        return error;
    }
}
