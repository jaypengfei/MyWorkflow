package org.simple.util;

import org.simple.context.WorkflowContext;
import org.simple.context.WorkflowResult;
import org.simple.work.Work;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class ParallelWorkflowExecutor {

    private final ExecutorService workExecutor;

    public ParallelWorkflowExecutor(ExecutorService executorService) {
        this.workExecutor = executorService;
    }

    public List<WorkflowResult> executeInParallel(List<Work> works, WorkflowContext workflowContext) {
        List<WorkflowResult> result = new ArrayList<>();
        if (works == null || works.size() == 0) {
            return result;
        }

        int workSize = works.size();
        Map<String, CompletableFuture<WorkflowResult>> completableFutureMap = new HashMap<>(workSize);
        for (Work work : works) {
            completableFutureMap.put(work.getWorkName(),
                    CompletableFuture.supplyAsync(() -> work.execute(workflowContext), workExecutor));
        }

        List<CompletableFuture<WorkflowResult>> completableFutureList = new ArrayList<>();
        completableFutureMap.forEach((key, value) -> completableFutureList.add(value));

        CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0]));
        try {
            for (Map.Entry<String, CompletableFuture<WorkflowResult>> entry : completableFutureMap.entrySet()) {
                result.add(entry.getValue().get());
            }
        } catch (Exception ex) {
            throw new RuntimeException("execute in parallel error.", ex);
        }

        return result;
    }
}
