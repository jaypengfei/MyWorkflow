package org.simple.work.flow;

import org.simple.context.DefaultWorkflowResult;
import org.simple.context.WorkStatus;
import org.simple.context.WorkflowContext;
import org.simple.context.WorkflowResult;
import org.simple.core.WorkflowEngineBuilder;
import org.simple.work.Work;
import org.simple.work.flow.impl.ConditionWorkflow;
import org.simple.work.flow.impl.ParallelWorkflow;
import org.simple.work.flow.impl.RepeatWorkflow;
import org.simple.work.flow.impl.SequentialWorkflow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final ExecutorService execute = Executors.newFixedThreadPool(10);
    public static void main(String[] args) {
        // conditionTest();
        // parallelTest();
        // repeatTest();
        sequentialTest();
    }

    private static void sequentialTest() {

        List<Work> works = new ArrayList<>();
        works.add(new MyFirstWork());
        works.add(new MySecondWork());
        works.add(new MyThirdWork());

        Workflow workflow = SequentialWorkflow.Builder.newSequentialWorkflow()
                .named("Sequence flow Test")
                .execute(works)
                .then(new EndWork())
                .build();

        WorkflowContext workflowContext = new WorkflowContext();
        workflowContext.put("date", new Date());
        workflowContext.put("count", 10);


        WorkflowResult workflowResult = WorkflowEngineBuilder
                .newWorkflowEngine()
                .build()
                .run(workflow, workflowContext);

        System.out.println(workflowResult);
    }

    private static void repeatTest() {
        Workflow workflow = RepeatWorkflow.Builder.newRepeatWorkflow()
                .named("repeat flow test")
                .repeat(new MyThirdWork())
                .times(5)
                .build();


        WorkflowContext workflowContext = new WorkflowContext();
        workflowContext.put("date", new Date());
        workflowContext.put("count", 10);


        WorkflowResult workflowResult = WorkflowEngineBuilder
                .newWorkflowEngine()
                .build()
                .run(workflow, workflowContext);

        System.out.println(workflowResult);
    }

    private static void conditionTest() {
        Workflow workflow = ConditionWorkflow.Builder.newConditionWorkflow()
                .named("Condition flow test")
                .execute(new MyFirstWork())
                .when(workflowResult -> workflowResult.getStatus().equals(WorkStatus.SUCCESS))
                .then(new MySecondWork())
                .otherwise(new MyThirdWork())
                .build();

        WorkflowContext workflowContext = new WorkflowContext();
        workflowContext.put("date", new Date());
        workflowContext.put("count", 10);

        WorkflowResult workflowResult = WorkflowEngineBuilder
                .newWorkflowEngine()
                .build()
                .run(workflow, workflowContext);

        System.out.println(workflowResult);
    }

    private static void parallelTest() {
        List<Work> works = new ArrayList<>();
        works.add(new MyFirstWork());
        works.add(new MySecondWork());
        works.add(new MyThirdWork());
        works.add(new MyFouthWork());
        works.add(new MyFifthWork());

        WorkflowContext workflowContext = new WorkflowContext();
        workflowContext.put("param", "test");

        ParallelWorkflow parallelWorkflow = ParallelWorkflow.Builder
                .newParallelFlow()
                .named("Parallel Test")
                .execute(works.toArray(new Work[0]))
                .with(execute)
                .build();


        WorkflowResult workflowResult = WorkflowEngineBuilder.newWorkflowEngine()
                .build()
                .run(parallelWorkflow, workflowContext);

        System.out.println(String.format("status: %s", workflowResult.getStatus()));
    }



    private static class MyFirstWork implements Work {
        @Override
        public WorkflowResult execute(WorkflowContext workflowContext) {
            DefaultWorkflowResult result = new DefaultWorkflowResult(WorkStatus.SUCCESS, workflowContext);
            workflowContext.put("a", 1);
            System.out.println("One done: " + System.currentTimeMillis());
            return result;
        }
    }


    private static class MySecondWork implements Work {
        @Override
        public WorkflowResult execute(WorkflowContext workflowContext) {
            DefaultWorkflowResult result = new DefaultWorkflowResult(WorkStatus.SUCCESS, workflowContext);
            workflowContext.put("b", 2);
            System.out.println("Two done: " + System.currentTimeMillis());
            return result;
        }
    }


    private static class MyThirdWork implements Work {
        @Override
        public WorkflowResult execute(WorkflowContext workflowContext) {
            DefaultWorkflowResult result = new DefaultWorkflowResult(WorkStatus.SUCCESS, workflowContext);
            workflowContext.put("c", 3);
            System.out.println("Three done: " + System.currentTimeMillis());
            return result;
        }
    }


    private static class MyFouthWork implements Work {
        @Override
        public WorkflowResult execute(WorkflowContext workflowContext) {
            DefaultWorkflowResult result = new DefaultWorkflowResult(WorkStatus.SUCCESS, workflowContext);
            workflowContext.put("d", 4);
            System.out.println("Four done: " + System.currentTimeMillis());
            return result;
        }
    }


    private static class MyFifthWork implements Work {
        @Override
        public WorkflowResult execute(WorkflowContext workflowContext) {
            DefaultWorkflowResult result = new DefaultWorkflowResult(WorkStatus.SUCCESS, workflowContext);
            workflowContext.put("e", 5);
            System.out.println("Five done: " + System.currentTimeMillis());
            return result;
        }
    }

    private static class EndWork implements Work {
        @Override
        public WorkflowResult execute(WorkflowContext workflowContext) {
            DefaultWorkflowResult result = new DefaultWorkflowResult(WorkStatus.SUCCESS, workflowContext);
            workflowContext.put("last", true);
            System.out.println("Last work done: " + System.currentTimeMillis());
            return result;
        }
    }
}