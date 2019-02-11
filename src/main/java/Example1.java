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

/**
 * Assumptions
 * ===========
 * 1. As we dont have a GFS I am manually keeping all the files divided and are
 * available in the form of chunks
 *
 * Example 1: WordCount
 * ====================
 * For this mapReduce example i am using ebook which i have taken from the project gutenberg.
 */
public class Example1 {

    /**
     * a. Creating your first mapper function
     * =======================================
     * 1. To create Mapper function create static Mapper Class extend MapperBase[common.base.MapperBase] Class
     * 2. Override the base map function
     */
    public static class Mapper extends MapperBase<String, Integer> {
        @Override
        public void map(Tuple<String, Integer> t, Collector<String, Integer> out) {
            // For every row break into individual rows and add it to the collector
            if (t.getKey().trim().length() > 0) {
                String[] keys = t.getKey().trim().replaceAll("[-+.^:,'\"?!*#}]", "").split(" ");
                for (String key : keys) {
                    out.collect(new Tuple<String, Integer>(key, 1));
                }
            }
        }
    }

    /**
     * b. Creating your first reduce function
     * ======================================
     * 1. To create Reducer function create static Reducer Class extend MapperBase[common.base.ReducerBase] Class
     * 2. Override the base reduce function
     */
    public static class Reducer extends ReducerBase<String, Integer> {
        Map<String, Integer> output = new HashMap<>();

        @Override
        public void reduce(Map<String, InCollector<String, Integer>> input, OutCollector<String, Integer> out) {
            System.out.println("I'm your reducer");
        }
    }

    public static void main(String[] args) {

        ArrayList<String> filePath = new ArrayList<>();

        String helper = System.getProperty("user.dir") +"\\src\\input";
        File folder = new File(helper);
        String[] files = folder.list();

        for(String file : files){
            filePath.add(helper+"\\"+file);
//            System.out.println(helper+"\\"+file);
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
                "/arun/output",
                "Word count");

        /**
         * d. Executing the job
         * =====================
         * 1. Pass the job defintion to the executor function
         */
        Executor.start(job);
    }
}
