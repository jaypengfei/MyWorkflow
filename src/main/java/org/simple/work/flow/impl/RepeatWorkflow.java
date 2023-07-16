package org.simple.work.flow.impl;

import org.simple.context.WorkflowContext;
import org.simple.context.WorkflowResult;
import org.simple.predicate.WorkflowResultPredicate;
import org.simple.work.Work;
import org.simple.work.flow.AbstractWorkflow;

public class RepeatWorkflow extends AbstractWorkflow {

    private final Work work;
    private final WorkflowResultPredicate predicate;

    RepeatWorkflow(String name, Work work, WorkflowResultPredicate predicate) {
        super(name);
        this.work = work;
        this.predicate = predicate;
    }

    @Override
    public WorkflowResult execute(WorkflowContext workflowContext) {
        WorkflowResult workflowResult;
        do {
            workflowResult = work.execute(workflowContext);
        } while (predicate.apply(workflowResult));

        return workflowResult;
    }

    public static class Builder {
        private Builder() {}

        public static NamedStep newRepeatWorkflow() {
            return new BuildSteps();
        }

        public interface NamedStep extends RepeatStep {
            RepeatStep named(String name);
        }

        public interface RepeatStep {
            UntilStep repeat(Work work);
        }

        public interface UntilStep {

            /**
             * 终止条件
             *
             * @param predicate 条件判断
             * @return BuildStep
             */
            BuildStep until(WorkflowResultPredicate predicate);

            /**
             * 循环次数
             * @param times 循环次数
             * @return BuildStep
             */
            BuildStep times(int times);
        }

        public interface BuildStep {
            RepeatWorkflow build();
        }

        private static class BuildSteps implements NamedStep, RepeatStep, UntilStep, BuildStep {

            private String name;
            private Work work;
            private WorkflowResultPredicate predicate;

            @Override
            public RepeatStep named(String name) {
                this.name = name;
                return this;
            }

            @Override
            public UntilStep repeat(Work work) {
                this.work = work;
                return this;
            }

            @Override
            public BuildStep until(WorkflowResultPredicate predicate) {
                this.predicate = predicate;
                return this;
            }

            @Override
            public BuildStep times(int times) {
                until(WorkflowResultPredicate.TimesPredicate.times(times));
                return this;
            }

            @Override
            public RepeatWorkflow build() {
                return new RepeatWorkflow(name, work, predicate);
            }
        }


    }
}
