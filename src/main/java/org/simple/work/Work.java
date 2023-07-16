package org.simple.work;

import org.simple.context.WorkflowContext;
import org.simple.context.WorkflowResult;

import java.util.UUID;

/**
 * 定义了一个任务
 */
public interface Work {

    default String getWorkName() {
        return UUID.randomUUID().toString();
    }

    /**
     * 执行当前的Work
     *
     * @param workflowContext 流程引擎上下文
     * @return 当前work执行结果
     */
    WorkflowResult execute(WorkflowContext workflowContext);
}
