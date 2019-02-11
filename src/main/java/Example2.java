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
import java.util.HashMap;
import java.util.Map;

public class Example2 {
    public static void main(String[] args) {

        ArrayList<String> filePath = new ArrayList<>();

        String helper = System.getProperty("user.dir") + "\\src\\input";
        File folder = new File(helper);
        String[] files = folder.list();

        for (String file : files) {
            filePath.add(helper + "\\" + file);
//            System.out.println(helper+"\\"+file);
        }

        /**
         * c. Creating your first Map Reduce Job
         * =====================================
         * 1. Create Job defintion with the help of a Job class provided with in the package
         * 2. Attach Mapper Definition , Reducer Definition, All the individual file chunks array list
         *    to job defintion
         */
        Job job = new Job(Example1.Mapper.class
                , Example1.Reducer.class,
                filePath,
                "/arun/output",
                "Word count");

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
    public static class Mapper extends MapperBase<String, Integer> {
        @Override
        public void map(Tuple<String, Integer> t, Collector<String, Integer> out) {

        }
    }

    /**
     * b. Creating your first reduce function
     * ======================================
     * 1. To create Reducer function create static Reducer Class extend MapperBase[common.base.ReducerBase] Class
     * 2. Override the base reduce function
     * 3. Write down on operation that has to be performed on the
     */
    public static class Reducer extends ReducerBase<String, Integer> {
        Map<String, Integer> output = new HashMap<>();

        @Override
        public void reduce(Map<String, InCollector<String, Integer>> input, OutCollector<String, Integer> out) {

        }
    }

}
