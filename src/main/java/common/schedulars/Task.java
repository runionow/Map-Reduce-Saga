package common.schedulars;

import common.Status;
import common.base.MapperBase;
import common.base.ReducerBase;

import java.io.Serializable;
import java.util.TreeMap;

public class Task implements Serializable {

    private final Class<? extends MapperBase> mapper;
    private final Class<? extends ReducerBase> reducer;
    private final TreeMap<String, Integer> map = new TreeMap<>();
    private final String inputFilePath;
    private final String outputFilePath;
    private Status status = Status.MAP_READY;

    public Task(String inputFilePath, String outputFilePath, Class<? extends MapperBase> mapper, Class<? extends ReducerBase> reducer) {
        this.inputFilePath = inputFilePath;
        this.mapper = mapper;
        this.reducer = reducer;
        this.outputFilePath = outputFilePath;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Class<? extends ReducerBase> getReducer() {
        return reducer;
    }

    public Class<? extends MapperBase> getMapper() {
        return mapper;
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void runMapper() {
//        Constructor cons = mapper.getConstructor();
//        MapperBase mapper = (MapperBase) cons.newInstance();
        // TODO For each line in the input text, run a mapper
//        try {
//            BufferedReader bf = new BufferedReader(new FileReader(inputFilePath));
//            String line;
//            int count = 0;
//            while ((line = bf.readLine()) != null) {
//                if(line.trim().length() > 0 ) {
//                    String[] keys = line.trim().replaceAll("[-+.^:,'\"?!*#}]","").split(" ");
//                    for (String key : keys) {
//                        // TODO Call map function every single time from the above reflection
//                        if (!map.containsKey(key)) {
//                            map.put(key, 1);
//                        } else {
//                            map.put(key, map.get(key) + 1);
//                        }
//                    }
//                }
//            }
//
//            for(String key : map.keySet()) {
//                System.out.println(key + ":" + map.get(key) );
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

}
