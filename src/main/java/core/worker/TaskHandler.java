package core.worker;

import common.Status;
import common.TaskStatus;
import common.Tuple;
import common.base.MapperBase;
import common.base.ReducerBase;
import common.collectors.Collector;
import common.schedulars.Task;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class TaskHandler<K, V> {

    private final Socket socket;
    private final String input;
    private final String output;
    private final Class<? extends MapperBase> mapRunner;
    private final Class<? extends ReducerBase> reduceRunner;
    private final Status status;
    private static String DATA_SEPARATOR = ":";
    private int taskNumber = 78888;
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
        this.taskNumber = task.getTask_num();
    }

    public TaskStatus runJob() {

        if (this.status == Status.MAP_READY) {
            try {
                System.out.println("I  am in  map");
                return this.mapExecute();
            } catch (IOException e) {
                System.out.println("Fail to read file");
                e.printStackTrace();
            }
        } else if (this.status == Status.REDUCE_READY) {
            System.out.println("Hello i am here");
            this.reduceExecute(this.output);
            return new TaskStatus(taskNumber, Status.REDUCE_SUCCESS);
        }
        System.out.println("Unable to get the status");
        return new TaskStatus(taskNumber, Status.JOB_FAIL);
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
                reduce = (ReducerBase) conRed.newInstance();
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

    private TaskStatus mapExecute() throws IOException {

        MapperBase mapMan = this.getMapperInstance();
        try {
            System.out.println("Recieved input file at : " + input);
            BufferedReader bf = new BufferedReader(new FileReader(input));
            String line;

            while ((line = bf.readLine()) != null) {
                mapMan.map(new Tuple(line, 1), mapMan.getOut());
            }

            System.out.println("Total number of records processed " + mapMan.getOut().count());
            mapMan.setIntermediate(mapMan.getOut().intermediateCollectors());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new TaskStatus(taskNumber, Status.MAP_FAIL);
        } catch (IOException e) {
            e.printStackTrace();
            return new TaskStatus(taskNumber, Status.MAP_FAIL);
        }

        Tuple t = (Tuple) mapMan.getOut().toList().get(0);
        String obj_path = writeFiles(mapMan.getOut());

        return new TaskStatus(taskNumber, Status.MAP_SUCCESS);
    }

    private String writeFiles(Collector c) {

        File directory = new File(this.output + "//temp//");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String[] input_break = this.input.split("\\\\");
        String filePath = directory.getAbsolutePath() + "\\" + input_break[input_break.length - 1] + taskNumber;
        String filePath_obj = directory.getAbsolutePath() + "\\" + input_break[input_break.length - 1] + taskNumber + "-obj";

        Writer writer = null;
        FileOutputStream f = null;

        try {
            f = new FileOutputStream(filePath_obj);
            writer = Files.newBufferedWriter(Paths.get(filePath));
            ObjectOutputStream objectOut = new ObjectOutputStream(f);
            objectOut.writeObject(c);
            objectOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < c.count(); i++) {
            try {
                writer.write(c.toList().get(i).toString() + System.lineSeparator());
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }
        try {
            writer.close();
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("The Object  was succesfully written to a file: " + filePath);
        return filePath_obj;
    }

    List<String> objFile(String directory) {
        List<String> textFiles = new ArrayList<String>();
        File dir = new File(directory + "\\temp");
        System.out.println(dir.toString());
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(("-obj"))) {
                textFiles.add(file.toString());
            }
        }

        return textFiles;
    }

    private Collector getCollectorFromFile(String filePath) {
        try {
            FileInputStream file_input = new FileInputStream(filePath);
            ObjectInputStream objectIn = new ObjectInputStream(file_input);

            Collector obj = (Collector) objectIn.readObject();
            System.out.println("Collector now available");
            objectIn.close();

            return obj;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public TaskStatus reduceExecute(String filePath) {
        System.out.println("I am here");
        Collector final_output = new Collector();

        // [START] Shuffling
        List<String> file_path = objFile(filePath);
        Collector c1 = new Collector();
        for (String file : file_path) {
            System.out.println(file);
            c1.add(getCollectorFromFile(file).toList());
        }

        TreeMap<?, ?> shuffling = (TreeMap<?, ?>) c1.intermediateCollectors();
        System.out.println(shuffling.size());
        for (Object key : shuffling.keySet()) {
            System.out.println(key);
        }
        // [END] Shuffling

        ReducerBase reduce = getReducerInstance();
        reduce.reduce(shuffling, final_output);
        writeFile(final_output);
        return new TaskStatus(1, Status.REDUCE_SUCCESS);
    }

    private void writeFile(Collector out) {
        File directory = new File(this.output);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = directory.getAbsolutePath() + "\\" + "output_" + taskNumber;
        System.out.println("REducer :" + filePath);
        Writer writer1 = null;

        try {
            writer1 = Files.newBufferedWriter(Paths.get(filePath));
            for (int j = 0; j < out.count(); j++) {
                try {
                    writer1.write(out.toList().get(j).toString() + System.lineSeparator());
                } catch (IOException ex) {
                    throw new UncheckedIOException(ex);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writer1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("The Object  was succesfully written to a file: " + filePath);
    }


}
