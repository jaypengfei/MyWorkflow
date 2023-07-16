package org.simple.work.flow.impl;

import org.simple.context.WorkStatus;
import org.simple.context.WorkflowContext;
import org.simple.context.WorkflowResult;
import org.simple.work.Work;
import org.simple.work.flow.AbstractWorkflow;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SequentialWorkflow extends AbstractWorkflow {

    private final List<Work> works = new ArrayList<>();

    SequentialWorkflow(String name, List<Work> works) {
        super(name);
        this.works.addAll(works);
    }

    @Override
    public WorkflowResult execute(WorkflowContext workflowContext) {
        WorkflowResult workflowResult = null;

        for (Work work : works) {
            workflowResult = work.execute(workflowContext);
            if (workflowResult != null && WorkStatus.FAILED.equals(workflowResult.getStatus())) {
                break;
            }
        }

        return workflowResult;
    }

    public static class Builder {

        private Builder() {}

        public static NamedStep newSequentialWorkflow() {
            return new BuildSteps();
        }

        public interface NamedStep extends ExecuteStep {
            ExecuteStep named(String name);
        }

        public interface ExecuteStep {
            ThenStep execute(Work initWork);

            ThenStep execute(List<Work> initWorks);
        }

        public interface ThenStep {
            ThenStep then(Work nextWork);

            ThenStep then(List<Work> nextWorks);

            SequentialWorkflow build();
        }

        private static class BuildSteps implements NamedStep, ExecuteStep, ThenStep {

            private String name;
            private final List<Work> works;

            BuildSteps() {
                this.name = UUID.randomUUID().toString();
                this.works = new ArrayList<>();
            }

            @Override
            public ExecuteStep named(String name) {
                this.name = name;
                return this;
            }

            @Override
            public ThenStep execute(Work initWork) {
                this.works.add(initWork);
                return this;
            }

            @Override
            public ThenStep execute(List<Work> initWorks) {
                this.works.addAll(initWorks);
                return this;
            }

            @Override
            public ThenStep then(Work nextWork) {
                this.works.add(nextWork);
                return this;
            }

            @Override
            public ThenStep then(List<Work> nextWorks) {
                this.works.addAll(nextWorks);
                return this;
            }

            @Override
            public SequentialWorkflow build() {
                return new SequentialWorkflow(this.name, this.works);
            }
        }
    }
}
