package common;

public class TaskStatus {

    private final int task_num;
    private final Status status;

    public TaskStatus(int task_num, Status status) {
        this.task_num = task_num;
        this.status = status;
    }

    public int getTask_num() {
        return task_num;
    }

    public Status getStatus() {
        return status;
    }

}
