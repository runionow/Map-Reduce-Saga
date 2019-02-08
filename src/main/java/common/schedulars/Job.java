package common.schedulars;

import common.base.MapperBase;
import common.base.ReducerBase;

import java.nio.file.Path;

public class Job {

    protected Class<? extends MapperBase> mapper;
    protected Class<? extends ReducerBase> reducer;
    protected Path input;
    protected Path output;
    protected final String jobName;

    public Job(String jobName) {
        this.jobName = jobName;
    }

    public Job(Class<? extends MapperBase> mapper,
               Class<? extends ReducerBase> reducer,
               Path input, Path output,
               String jobName) {
        this.jobName = jobName;
        this.mapper = mapper;
        this.reducer = reducer;
        this.input = input;
        this.output = output;
    }

    public Class<? extends MapperBase> getMapper() {
        return mapper;
    }

    public Class<? extends ReducerBase> getReducer() {
        return reducer;
    }

    public Path getInput() {
        return input;
    }

    public Path getOutput() {
        return output;
    }

    public void setOutput(Path output) {
        this.output = output;
    }

    public String getJobName() {
        return jobName;
    }


    public void start() {
//        TODO 1. Verify the job details
//        TODO 2. Submit the job to the Manager




    }
}

