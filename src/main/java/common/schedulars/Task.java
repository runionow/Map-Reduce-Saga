package common.schedulars;

import common.base.MapperBase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.TreeMap;

public class Task {

    private final String inputFilePath;
    private final Class<? extends MapperBase> mapper;
    private final TreeMap<String, Integer> map = new TreeMap<>();

    public Task(String inputFilePath, Class< ? extends MapperBase> mapper) {
        this.inputFilePath = inputFilePath;
        this.mapper = mapper;
    }


    public void runMapper() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor cons = mapper.getConstructor();
        MapperBase mapper = (MapperBase) cons.newInstance();

        // TODO For each line in the input text, run a mapper
        try {
            BufferedReader bf = new BufferedReader(new FileReader(inputFilePath));
            String line;
            int count = 0;
            while ((line = bf.readLine()) != null) {
                if(line.trim().length() > 0 ) {
                    String[] keys = line.trim().replaceAll("[-+.^:,'\"?!*#}]","").split(" ");
                    for (String key : keys) {
                        // TODO Call map function every single time from the above reflection
                        if (!map.containsKey(key)) {
                            map.put(key, 1);
                        } else {
                            map.put(key, map.get(key) + 1);
                        }
                    }
                }
            }

            for(String key : map.keySet()) {
                System.out.println(key + ":" + map.get(key) );
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




}
