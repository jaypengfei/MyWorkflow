package org.simple.work.flow.impl;

import org.simple.context.ParallelWorkflowResult;
import org.simple.context.WorkflowContext;
import org.simple.context.WorkflowResult;
import org.simple.util.ParallelWorkflowExecutor;
import org.simple.work.Work;
import org.simple.work.flow.AbstractWorkflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

public class ParallelWorkflow extends AbstractWorkflow {

    /**
     * 执行任务集合
     */
    private final List<Work> works = new ArrayList<>();

    /**
     * 任务执行器
     */
    private final ParallelWorkflowExecutor parallelWorkflowExecutor;

    public ParallelWorkflow(String name, List<Work> works, ParallelWorkflowExecutor parallelWorkflowExecutor) {
        super(name);
        this.works.addAll(works);
        this.parallelWorkflowExecutor = parallelWorkflowExecutor;
    }


    @Override
    public WorkflowResult execute(WorkflowContext workflowContext) {
        ParallelWorkflowResult result = new ParallelWorkflowResult();
        List<WorkflowResult> workflowResults = parallelWorkflowExecutor.executeInParallel(this.works, workflowContext);
        result.addAll(workflowResults);

        return result;
    }

    public static class Builder {

        private Builder() {}

        public static NamedStep newParallelFlow() {
            return new BuildSteps();
        }

        public interface NamedStep extends ExecuteStep {
            ExecuteStep named(String name);
        }

        public interface ExecuteStep {
            WithStep execute(Work... works);
        }

        public interface WithStep {
            BuildStep with(ExecutorService executorService);
        }

        public interface BuildStep {
            ParallelWorkflow build();
        }

        private static class BuildSteps implements NamedStep, ExecuteStep, WithStep, BuildStep {

            private String name;
            private final List<Work> works;
            private ExecutorService executorService;


            public BuildSteps() {
                this.name = UUID.randomUUID().toString();
                this.works = new ArrayList<>();
            }

            @Override
            public ExecuteStep named(String name) {
                this.name = name;
                return this;
            }

            @Override
            public WithStep execute(Work... works) {
                this.works.addAll(Arrays.asList(works));
                return this;
            }

            @Override
            public BuildStep with(ExecutorService executorService) {
                if (executorService != null) {
                    this.executorService = executorService;
                }

                return this;
            }

            @Override
            public ParallelWorkflow build() {
                return new ParallelWorkflow(
                        this.name,
                        this.works,
                        new ParallelWorkflowExecutor(this.executorService));
            }
        }

    }
}
