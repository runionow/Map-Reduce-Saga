package common.schedulars;

import common.base.MapperBase;
import common.base.ReducerBase;

import java.io.Serializable;
import java.util.ArrayList;

public class Job implements Serializable {

    protected Class<? extends MapperBase> mapper;
    protected Class<? extends ReducerBase> reducer;
    protected ArrayList<String> input;
    protected String output;
    protected final String jobName;

    public Job(String jobName) {
        this.jobName = jobName;
    }

    public Job(Class<? extends MapperBase> mapper,
               Class<? extends ReducerBase> reducer,
               ArrayList<String> input, String output,
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

    public ArrayList<String> getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getJobName() {
        return jobName;
    }



}

