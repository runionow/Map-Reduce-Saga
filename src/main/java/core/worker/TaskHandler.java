package core.worker;

import common.Status;
import common.base.MapperBase;
import common.base.ReducerBase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;

public class TaskHandler {

    private final Socket socket;
    private final String input;
    private final String output;
    private final Class<? extends MapperBase> mapRunner;
    private final Class<? extends ReducerBase> reduceRunner;


    public TaskHandler(Socket socket, Class<? extends MapperBase> mapper, Class<? extends ReducerBase> reducer, String input, String output) {
        this.socket = socket;
        this.mapRunner = mapper;
        this.reduceRunner = reducer;
        this.input = input;
        this.output = output;
    }

    public Status mapExecute() {
        try {
            BufferedReader bf = new BufferedReader(new FileReader(input));
            String line;

            while ((line = bf.readLine()) != null) {
                if (line.trim().length() > 0) {
                    String[] keys = line.trim().replaceAll("[-+.^:,'\"?!*#}]", "").split(" ");
                    for (String key : keys) {
                        // TODO Call map function every single time from the above reflection
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

    public Status reduceExecute() {
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
