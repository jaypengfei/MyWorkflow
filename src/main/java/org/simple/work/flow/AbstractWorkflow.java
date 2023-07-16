package org.simple.work.flow;

public abstract class AbstractWorkflow implements Workflow {

    private final String workName;

    public AbstractWorkflow(String workName) {
        this.workName = workName;
    }

    @Override
    public String getWorkName() {
        return workName;
    }
}
