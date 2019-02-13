import common.Tuple;
import common.base.MapperBase;
import common.base.ReducerBase;
import common.collectors.Collector;
import common.collectors.InCollector;
import common.collectors.OutCollector;
import common.schedulars.Executor;
import common.schedulars.Job;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Example 2: Inverted Index
 */

public class Example2 {

    public static void main(String[] args) {

        ArrayList<String> filePath = new ArrayList<>();

        String helper = System.getProperty("user.dir") + "\\src\\input";
        File folder = new File(helper);
        String[] files = folder.list();

        for (String file : files) {
            filePath.add(helper + "\\" + file);
        }


        /**
         * c. Creating your first Map Reduce Job
         * =====================================
         * 1. Create Job defintion with the help of a Job class provided with in the package
         * 2. Attach Mapper Definition , Reducer Definition, All the individual file chunks array list
         *    to job defintion
         */
        Job job = new Job(Mapper.class
                , Reducer.class,
                filePath,
                "/arun/output2",
                "Inverted Index");

        /**
         * d. Executing the job
         * =====================
         * 1. Pass the job defintion to the executor function
         */
        Executor.start(job);
    }

    /**
     * a. Creating your first mapper function
     * =======================================
     * 1. To create Mapper function create static Mapper Class extend MapperBase[common.base.MapperBase] Class
     * 2. Override the base map function
     * 3. Each line wil be an input to the map reduce program
     */
    public static class Mapper extends MapperBase<String, String> {
        @Override
        public void map(Tuple<String, String> t, Collector<String, String> out) {
            if (t.getKey().trim().length() > 0) {
                // Triming every
                String[] keys = t.getKey().toLowerCase().trim().replaceAll("[-+.^:,'\"?!*#}]", "").split(" ");
                for (String key : keys) {
                    out.collect(new Tuple(key, out.getFileName()));
                }
            }
        }
    }

    /**
     * b. Creating your first reduce function
     * ======================================
     * 1. To create Reducer function create static Reducer Class extend MapperBase[common.base.ReducerBase] Class
     * 2. Override the base reduce function
     * 3. Write down on operation that has to be performed on the
     */
    public static class Reducer extends ReducerBase<String, String> {
        @Override
        public void reduce(Map<String, InCollector<String, String>> input, OutCollector<String, String> out) {
            Set<String> set = new HashSet<>();
            for (String key : input.keySet()) {
                Collector c1 = (Collector) input.get(key);
                for (int i = 0; i < c1.toList().size(); i++) {
                    Tuple t = (Tuple) c1.toList().get(i);
                    System.out.println(t.toString());
                    set.add((String) t.getValue());
                }
                out.collect(new Tuple(key, set.size())); // Adding unique document size to the final output
            }
        }
    }


}
