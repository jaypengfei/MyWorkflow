package org.simple.context;

public interface WorkflowResult {

    /**
     * 获取当前执行状态
     *
     * @return workStatus
     */
    WorkStatus getStatus();

    /**
     * 获取当前执行上下文
     *
     * @return workflowContext
     */
    WorkflowContext getContext();

    /**
     * 获取当前执行错误
     *
     * @return throwable
     */
    Throwable getError();
}
