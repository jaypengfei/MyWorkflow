package org.simple.work.flow.impl;

import org.simple.context.WorkflowContext;
import org.simple.context.WorkflowResult;
import org.simple.predicate.WorkflowResultPredicate;
import org.simple.work.EmptyWork;
import org.simple.work.Work;
import org.simple.work.flow.AbstractWorkflow;

import java.util.UUID;

public class ConditionWorkflow extends AbstractWorkflow {

    private Work initWork;
    private Work nextWorkOnSuccess;
    private Work nextWorkOnFailure;
    private WorkflowResultPredicate predicate;


    public ConditionWorkflow(String workName, Work initWork, Work nextWorkOnSuccess, Work nextWorkOnFailure, WorkflowResultPredicate predicate) {
        super(workName);
        this.initWork = initWork;
        this.nextWorkOnSuccess = nextWorkOnSuccess;
        this.nextWorkOnFailure = nextWorkOnFailure;
        this.predicate = predicate;
    }

    @Override
    public WorkflowResult execute(WorkflowContext workflowContext) {
        WorkflowResult workflowResult = initWork.execute(workflowContext);
        return predicate.apply(workflowResult) ?
                nextWorkOnSuccess.execute(workflowContext) : nextWorkOnFailure.execute(workflowContext);
    }

    public static class Builder {

        private Builder() {}

        public static NamedStep newConditionWorkflow() {
            return new BuildSteps();
        }

        private static class BuildSteps implements NamedStep, ExecuteStep, WhenStep, ThenStep, OtherwiseStep, BuildStep {

            private String name;
            private Work initWork;
            private Work nextWorkOnSuccess;
            private Work nextWorkOnFailure;
            private WorkflowResultPredicate predicate;

            BuildSteps() {
                this.name = UUID.randomUUID().toString();
                this.initWork = new EmptyWork();
                this.nextWorkOnSuccess = new EmptyWork();
                this.nextWorkOnFailure = new EmptyWork();
                this.predicate = WorkflowResultPredicate.WORK_RESULT_FALSE;
            }

            @Override
            public WhenStep execute(Work initWork) {
                this.initWork = initWork;
                return this;
            }

            @Override
            public ExecuteStep named(String name) {
                this.name = name;
                return this;
            }

            @Override
            public ThenStep when(WorkflowResultPredicate predicate) {
                this.predicate = predicate;
                return this;
            }

            @Override
            public OtherwiseStep then(Work otherWork) {
                this.nextWorkOnSuccess = otherWork;
                return this;
            }

            @Override
            public BuildStep otherwise(Work otherWork) {
                this.nextWorkOnFailure = otherWork;
                return this;
            }

            @Override
            public ConditionWorkflow build() {
                return new ConditionWorkflow(
                        this.name,
                        this.initWork,
                        this.nextWorkOnSuccess,
                        this.nextWorkOnFailure,
                        this.predicate
                );
            }
        }

        public interface ExecuteStep {
            WhenStep execute(Work initWork);
        }

        public interface NamedStep {
            ExecuteStep named(String name);
        }

        public interface WhenStep {
            ThenStep when(WorkflowResultPredicate predicate);
        }

        public interface ThenStep {
            OtherwiseStep then(Work otherWork);
        }

        public interface OtherwiseStep {
            BuildStep otherwise(Work otherWork);
        }

        public interface BuildStep {
            ConditionWorkflow build();
        }
    }
}
