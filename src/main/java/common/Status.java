package common;

public enum Status {
    MAP_SUCCESS(1),
    MAP_FAIL(2),
    REDUCE_SUCCESS(3),
    REDUCE_FAIL(4),
    WORKER_FAIL(5),
    JOB_FAIL(6),
    MAP_TASK(7),
    REDUCE_TASK(8);

    int error_code = 0;

    Status(int i) {
        error_code = i;
    }
}
