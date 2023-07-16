package org.simple.context;

import java.util.*;

public class ParallelWorkflowResult implements WorkflowResult {

    private final List<WorkflowResult> workflowResults;

    public ParallelWorkflowResult() {
        this(new ArrayList<>());
    }

    public ParallelWorkflowResult(List<WorkflowResult> workflowResults) {
        this.workflowResults = workflowResults;
    }

    public List<WorkflowResult> getWorkflowResults() {
        return this.workflowResults;
    }

    public void add(WorkflowResult workflowResult) {
        this.workflowResults.add(workflowResult);
    }

    public void addAll(List<WorkflowResult> workflowResults) {
        this.workflowResults.addAll(workflowResults);
    }

    @Override
    public WorkStatus getStatus() {
        // TODO 这里需要考虑下状态的处理
        // 默认全部成功才算成功
        for (WorkflowResult workflowResult : workflowResults) {
            if (workflowResult.getStatus() == WorkStatus.RUNNING) {
                return WorkStatus.RUNNING;
            } else if (workflowResult.getStatus() == WorkStatus.FAILED) {
                return WorkStatus.FAILED;
            }
        }

        for (WorkflowResult workflowResult : workflowResults) {
            if (workflowResult.getStatus() == WorkStatus.SUCCESS) {
                return WorkStatus.SUCCESS;
            }
        }

        return WorkStatus.INIT;
    }

    @Override
    public WorkflowContext getContext() {
        WorkflowContext workflowContext = new WorkflowContext();
        for (WorkflowResult workflowResult : workflowResults) {
            for (Map.Entry<String, Object> entry:  workflowResult.getContext().getEntrySet()) {
                workflowContext.put(entry.getKey(), entry.getValue());
            }
        }

        return workflowContext;
    }

    @Override
    public Throwable getError() {
        return workflowResults.stream()
                .filter(p -> p.getError() != null)
                .findFirst()
                .map(WorkflowResult::getError)
                .orElse(null);
    }
}
