package common.schedulars;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Executor {
    public void Executor(Job job) throws IOException {
        // Check for files in the input folder
        StringBuffer sb = new StringBuffer();

        try{
            BufferedReader textFile = new BufferedReader(new FileReader(String.valueOf(job.getInput())));
            String line = null;
            while((line = textFile.readLine()) != null) {
                sb.append(line.trim()).append(" ");
            }
            textFile.close();
        } catch (IOException ex) {

        }


        // Create an output folder
        // Segregate files
        // Run Map Job and generate intermediate files
        // Run Reduce Job and consume intermediate files
        // Show the output
        // Upon close remove temporary files
    }


    public static void main(String[] args) {



        // After completion start a reduce job
        // Store the result
    }
}
