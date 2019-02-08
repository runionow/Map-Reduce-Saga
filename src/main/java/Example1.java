import common.Tuple;
import common.base.MapperBase;
import common.base.ReducerBase;
import common.collectors.InCollector;
import common.collectors.OutCollector;
import common.schedulars.Executor;
import common.schedulars.Job;
import common.schedulars.Task;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Assumptions
 * ===========
 * 1. As we dont have a GFS I am manually keeping all the files divided and are
 * available in the form of chunks
 *
 * Example 1: WordCount
 * ====================
 * For this mapReduce I am using ebook which i have taken from the project gutenberg.
 */
public class Example1 {

    /**
     * a. Creating your first mapper function
     * =======================================
     * 1. To create Mapper function create static Mapper Class extend MapperBase[common.base.MapperBase] Class
     * 2. Override the base map function
     */
    public static class Mapper extends MapperBase{
        @Override
        public void map(Tuple t, OutCollector out) {
            System.out.println("I am your Mapper");
        }
    }

    /**
     * b. Creating your first reduce function
     * ======================================
     * 1. To create Reducer function create static Reducer Class extend MapperBase[common.base.ReducerBase] Class
     * 2. Override the base reduce function
     */
    public static class Reducer extends ReducerBase {
        @Override
        public void reduce(InCollector in, OutCollector out) {
            System.out.println("I am your Reducer");
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
                "/input",
                "/arun/output",
                "First MR Job");

        /**
         * d. Executing the job
         * =====================
         * 1. Pass the job defintion to the executor function
         */
        Executor.start(job);


        // For each file start a map job
        Task mapTask = new Task(filePath.get(0), Mapper.class);

        try {
            mapTask.runMapper();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }
}
