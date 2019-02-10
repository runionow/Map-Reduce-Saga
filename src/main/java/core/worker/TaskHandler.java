package core.worker;

import common.Status;
import common.base.MapperBase;
import common.base.ReducerBase;
import common.schedulars.Task;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class TaskHandler {

    private final Socket socket;
    private final String input;
    private final String output;
    private final Class<? extends MapperBase> mapRunner;
    private final Class<? extends ReducerBase> reduceRunner;
    private final Status status;

    private MapperBase map = null;
    private ReducerBase reduce = null;


    public TaskHandler(Socket socket, Class<? extends MapperBase> mapper, Class<? extends ReducerBase> reducer, String input, String output, Status status) {
        this.socket = socket;
        this.mapRunner = mapper;
        this.reduceRunner = reducer;
        this.input = input;
        this.output = output;
        this.status = status;
    }

    public TaskHandler(Socket socket, Task task) {
        this.socket = socket;
        this.mapRunner = task.getMapper();
        this.reduceRunner = task.getReducer();
        this.input = task.getInputFilePath();
        this.output = task.getOutputFilePath();
        this.status = task.getStatus();
    }

    public Status runJob() {
        if (this.status == Status.MAP_READY) {
            this.mapExecute();
            return Status.MAP_SUCCESS;
        } else if (this.status == Status.REDUCE_READY) {
            this.reduceExecute();
            return Status.REDUCE_SUCCESS;
        }
        System.out.println("Unable to get the status");
        return Status.JOB_FAIL;

    }

    private MapperBase getMapperInstance() {
        if (map == null) {
            try {
                Constructor conMap = this.mapRunner.getConstructor();
                map = (MapperBase) conMap.newInstance();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    private ReducerBase getReducerInstance() {
        if (reduce == null) {
            try {
                Constructor conRed = this.reduceRunner.getConstructor();
                map = (MapperBase) conRed.newInstance();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return reduce;
    }

    private Status mapExecute() {
        try {
            BufferedReader bf = new BufferedReader(new FileReader(input));
            String line;

            while ((line = bf.readLine()) != null) {
                if (line.trim().length() > 0) {
                    String[] keys = line.trim().replaceAll("[-+.^:,'\"?!*#}]", "").split(" ");
                    for (String key : keys) {
                        // TODO Call map function every single time from the above reflection
                        System.out.println(key);
                    }
                }
            }

            // TODO Call reduce function for local reduction


            // TODO Write Intermediate files to the disk

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return Status.MAP_FAIL;
        } catch (IOException e) {
            e.printStackTrace();
            return Status.MAP_FAIL;
        }

        return Status.MAP_SUCCESS;
    }

    private Status reduceExecute() {
        try {
            BufferedReader bf = new BufferedReader(new FileReader(input));
            String line;

            while ((line = bf.readLine()) != null) {
                if (line.trim().length() > 0) {

                }
            }

            // TODO Call reduce function here

            // TODO Write files to the disk

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return Status.REDUCE_FAIL;
        } catch (IOException e) {
            e.printStackTrace();
            return Status.REDUCE_FAIL;
        }

        return Status.REDUCE_SUCCESS;
    }

}
