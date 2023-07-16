package org.simple.core;

import org.simple.context.WorkflowContext;
import org.simple.context.WorkflowResult;
import org.simple.work.Work;

public interface WorkflowEngine {

    /**
     * 执行当前的工作流
     *
     * @param work 当前工作流
     * @param workflowContext 工作流的上下文
     * @return 工作流执行结果
     */
    WorkflowResult run(Work work, WorkflowContext workflowContext);
}
