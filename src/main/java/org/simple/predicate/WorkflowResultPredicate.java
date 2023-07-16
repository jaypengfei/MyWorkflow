package org.simple.predicate;

import org.simple.context.WorkStatus;
import org.simple.context.WorkflowResult;

import java.util.concurrent.atomic.AtomicInteger;

@FunctionalInterface
public interface WorkflowResultPredicate {

    boolean apply(WorkflowResult workflowResult);

    WorkflowResultPredicate WORK_RESULT_TRUE = workflowResult -> true;
    WorkflowResultPredicate WORK_RESULT_FALSE = workflowResult -> false;
    WorkflowResultPredicate WORK_RESULT_COMPLETED = workflowResult -> workflowResult.getStatus().equals(WorkStatus.SUCCESS);
    WorkflowResultPredicate WORK_RESULT_FAILED = workflowResult -> workflowResult.getStatus().equals(WorkStatus.FAILED);


    class TimesPredicate implements WorkflowResultPredicate {

        private final int times;
        private final AtomicInteger counter = new AtomicInteger();
        public TimesPredicate(int times) {
            this.times = times;
        }

        @Override
        public boolean apply(WorkflowResult workflowResult) {
            return counter.incrementAndGet() != times;
        }

        public static TimesPredicate times(int times) {
            return new TimesPredicate(times);
        }
    }
}
