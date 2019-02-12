# Map Reduce Saga 

## Introduction

Map reduce saga is a map reduce framework that can run map reduce jobs locally as well as across the multiple nodes after proper configuration of the the ip address and 
sockets of the respective nodes

## Solution

### Requirement 

* Java 8 
* IntelliJIDE
* Gradle 


### Project structure



### Components

Map reduce Saga has three major components 

##### Manager

* Is responsible for submitting jobs to the nodes in orchestration
* Manger has an heartbeat mechanism to find the availability of worker nodes 
* It is capable of tracking status of task assigned to the worker nodes
* Orchestrate the entire Job definition submitted by the user

##### Worker

* Looks for Manager node 
* Completes the task assigned by the master node
* It is capable of handling both map and reduce phase
* Capable of performing combining and partitioning

##### Client 
* Create a job definition
```$xslt

public class Example2 {
    public static void main(String[] args) {


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

}

```

* Allows you to create a job defintions for mapper and reduce task it is very much similar to the hadoop map reduce framework for carrying out the jobs

```$xslt
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
```
 
 * tracks the progress of the computation
 
 the above guidelines helps to create some of the great build map reduce jobs
 
 ### Limitations
 
 1. Assumes there is no distributed file system
 2. This project passes objects and reflection classes to the remote nodes
 3. Modification to any intermediate binary files manually would corrupt the program
    and causes the job to be failed and the solution is not capable of handling those kind of scenarios
 4. No fully functional logger framework is attached to the solution most of them are out put systems printed to the terminal
 5. Manager is a single point of contact if manager fails the whole job fails, I am not doing any leader allocation to choose the manager
 6. You can have only one manager in the orchestration and is not recoverable from serious errors
 7. Manager cannot is not capable of invoking new worker nodes
 8. the worker can perform only one task at a time, It doesn't have queue to maintain the list of jobs its is the job of manager to manage the tasks assigned to worker
 
 ### Capabilities 
 
 1. The manager is capable of handling multiple job requests at once
 2. The manager is capable of orchestrating tasks to the workers
 3. It can accept fault tolerance from worker nodes
 4. Worker node is capable of taking both and map reduce tasks and returns works status after completion of the task
 5. Jobs can be tracked on the job initiator
 
 
 
 ### Running the application
 
 For testing and debugging the entire environment can be implemented on a single localhost over multple sockets
 
 * All the worker nodes listens on port number 7777
 * Manager communications are made through port number 9080
 
 The above port numbers can be modified 
 
 #### Initiating the orchestration of nodes
 
* In the source code i have included `StartManager.java` to start the manager node and worker nodes
* Use `StartWorker.java` to start the worker nodes - you can start as many worker nodes as possible and delete during the job the manager is capable of handling the worker nodes

 Once the environment is available, the manager is ready to take the request from the Client
 
 * I have included a sample code ```Example1.java``` , which is a simple wordcount example
 
 #### Here are some of the logs i gathered while running a word-count job
 
 1. Client side logs
 
 ```$xslt
***************************
Welcome to Map-Reduce Saga
***************************

This project has been done as a part of Distributed Systems coursework
(C) 2019 Arun Nekkalapudi <anekkal@iu.edu>, Indiana University Bloomington.
Looking for a Manager
Connection established with manager node!
Job has been passed to Manager
```

Manager node is capable of generating some extensive logs 

```$xslt
"C:\Program Files\Java\jdk1.8.0_181\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2018.3.4\lib\idea_rt.jar=54083:C:\Program Files\JetBrains\IntelliJ IDEA 2018.3.4\bin" -Dfile.encoding=UTF-8 -classpath "C:\Program Files\Java\jdk1.8.0_181\jre\lib\charsets.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\deploy.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\access-bridge-64.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\cldrdata.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\dnsns.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\jaccess.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\jfxrt.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\localedata.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\nashorn.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\sunec.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\sunjce_provider.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\sunmscapi.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\sunpkcs11.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\zipfs.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\javaws.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\jce.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\jfr.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\jfxswt.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\jsse.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\management-agent.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\plugin.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\resources.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\rt.jar;D:\DS\Project MR\out\production\classes;C:\Users\Arun Nekkalapudi\.gradle\caches\modules-2\files-2.1\io.grpc\grpc-netty-shaded\1.18.0\e202aa0b36800e60ca87ec05a2ce4b240f69de09\grpc-netty-shaded-1.18.0.jar;C:\Users\Arun Nekkalapudi\.gradle\caches\modules-2\files-2.1\io.grpc\grpc-protobuf\1.18.0\74d794cf9b90b620e0ad698008abc4f55c1ca5e2\grpc-protobuf-1.18.0.jar;C:\Users\Arun Nekkalapudi\.gradle\caches\modules-2\files-2.1\io.grpc\grpc-stub\1.18.0\5e4dbf944814d49499e3cbd9846ef58f629b5f32\grpc-stub-1.18.0.jar;C:\Users\Arun Nekkalapudi\.gradle\caches\modules-2\files-2.1\org.apache.logging.log4j\log4j-core\2.11.1\592a48674c926b01a9a747c7831bcd82a9e6d6e4\log4j-core-2.11.1.jar;C:\Users\Arun Nekkalapudi\.gradle\caches\modules-2\files-2.1\org.apache.logging.log4j\log4j-api\2.11.1\268f0fe4df3eefe052b57c87ec48517d64fb2a10\log4j-api-2.11.1.jar;C:\Users\Arun Nekkalapudi\.gradle\caches\modules-2\files-2.1\io.grpc\grpc-protobuf-lite\1.18.0\4ce979e12da19aaef862c1f48385cb5cf69d61d7\grpc-protobuf-lite-1.18.0.jar;C:\Users\Arun Nekkalapudi\.gradle\caches\modules-2\files-2.1\io.grpc\grpc-core\1.18.0\e21b343bba2006bac31bb16b7438701cddfbf564\grpc-core-1.18.0.jar;C:\Users\Arun Nekkalapudi\.gradle\caches\modules-2\files-2.1\com.google.protobuf\protobuf-java\3.5.1\8c3492f7662fa1cbf8ca76a0f5eb1146f7725acd\protobuf-java-3.5.1.jar;C:\Users\Arun Nekkalapudi\.gradle\caches\modules-2\files-2.1\org.checkerframework\checker-compat-qual\2.5.2\dc0b20906c9e4b9724af29d11604efa574066892\checker-compat-qual-2.5.2.jar;C:\Users\Arun Nekkalapudi\.gradle\caches\modules-2\files-2.1\com.google.guava\guava\25.1-android\bdaab946ca5ad20253502d873ba0c3313d141036\guava-25.1-android.jar;C:\Users\Arun Nekkalapudi\.gradle\caches\modules-2\files-2.1\com.google.api.grpc\proto-google-common-protos\1.12.0\1140cc74df039deb044ed0e320035e674dc13062\proto-google-common-protos-1.12.0.jar;C:\Users\Arun Nekkalapudi\.gradle\caches\modules-2\files-2.1\io.grpc\grpc-context\1.18.0\c63e8b86af0fb16b5696480dc14f48e6eaa7193b\grpc-context-1.18.0.jar;C:\Users\Arun Nekkalapudi\.gradle\caches\modules-2\files-2.1\com.google.code.gson\gson\2.7\751f548c85fa49f330cecbb1875893f971b33c4e\gson-2.7.jar;C:\Users\Arun Nekkalapudi\.gradle\caches\modules-2\files-2.1\com.google.errorprone\error_prone_annotations\2.2.0\88e3c593e9b3586e1c6177f89267da6fc6986f0c\error_prone_annotations-2.2.0.jar;C:\Users\Arun Nekkalapudi\.gradle\caches\modules-2\files-2.1\com.google.code.findbugs\jsr305\3.0.2\25ea2e8b0c338a877313bd4672d3fe056ea78f0d\jsr305-3.0.2.jar;C:\Users\Arun Nekkalapudi\.gradle\caches\modules-2\files-2.1\org.codehaus.mojo\animal-sniffer-annotations\1.17\f97ce6decaea32b36101e37979f8b647f00681fb\animal-sniffer-annotations-1.17.jar;C:\Users\Arun Nekkalapudi\.gradle\caches\modules-2\files-2.1\io.opencensus\opencensus-contrib-grpc-metrics\0.18.0\8e90fab2930b6a0e67dab48911b9c936470d43dd\opencensus-contrib-grpc-metrics-0.18.0.jar;C:\Users\Arun Nekkalapudi\.gradle\caches\modules-2\files-2.1\io.opencensus\opencensus-api\0.18.0\b89a8f8dfd1e1e0d68d83c82a855624814b19a6e\opencensus-api-0.18.0.jar;C:\Users\Arun Nekkalapudi\.gradle\caches\modules-2\files-2.1\com.google.j2objc\j2objc-annotations\1.1\ed28ded51a8b1c6b112568def5f4b455e6809019\j2objc-annotations-1.1.jar" StartManager
***************************
Welcome to Map-Reduce Saga
***************************

This project has been done as a part of Distributed Systems coursework
(C) 2019 Arun Nekkalapudi <anekkal@iu.edu>, Indiana University Bloomington.

Manager Node awaiting for jobs...

Waiting for worker nodes to join the Manager....
New worker Node joined from port :54094 Hostname :127.0.0.1
Total 1 node(s) in the orchestration
New worker Node joined from port :54104 Hostname :127.0.0.1
Total 2 node(s) in the orchestration
New worker Node joined from port :54113 Hostname :127.0.0.1
Total 3 node(s) in the orchestration
Received job from client Socket[addr=/127.0.0.1,port=54123,localport=7777]!
Recieved Job Defintion : Word count
New job has been added to the task_handler
New job has been added to the task_handler
New job has been added to the task_handler
Disconnected Worker Node at Socket[addr=127.0.0.1/127.0.0.1,port=54113,localport=9080]!
Total 2 node(s) in the orchestration
Disconnected Worker Node at Socket[addr=127.0.0.1/127.0.0.1,port=54104,localport=9080]!
Total 1 node(s) in the orchestration
Disconnected Worker Node at Socket[addr=127.0.0.1/127.0.0.1,port=54094,localport=9080]!
Total 0 node(s) in the orchestration
Waiting for nodes to complete the task and looking for new nodes to join the orchestration
New worker Node joined from port :54127 Hostname :127.0.0.1
Total 1 node(s) in the orchestration
New worker Node joined from port :54128 Hostname :127.0.0.1
Total 2 node(s) in the orchestration
New worker Node joined from port :54129 Hostname :127.0.0.1
Total 3 node(s) in the orchestration
Waiting for nodes to complete the task and looking for new nodes to join the orchestration
New job has been added to the task_handler
Map tasks are now completed
Disconnected Worker Node at Socket[addr=127.0.0.1/127.0.0.1,port=54128,localport=9080]!
Total 2 node(s) in the orchestration
New worker Node joined from port :54130 Hostname :127.0.0.1
Total 3 node(s) in the orchestration
Starting the reduce task
54130
New job has been added to the task_handler
Reduce task has been completed
Disconnected Worker Node at Socket[addr=127.0.0.1/127.0.0.1,port=54130,localport=9080]!
Total 2 node(s) in the orchestration

```
Scenario where client completed a job and succesfully restarted and waiting for the new jobs
```$xslt
 ***************************
 Welcome to Map-Reduce Saga
 ***************************
 
 This project has been done as a part of Distributed Systems coursework
 (C) 2019 Arun Nekkalapudi <anekkal@iu.edu>, Indiana University Bloomington.
 
 Looking for a Manager
 Joined Manager Node :9080 Hostname :localhost
 recieved Task
 I  am in  map
 Recieved input file at : D:\DS\Project MR\src\input\input_2
 Total number of records processed 12513
 The Object  was succesfully written to a file: D:\arun\output\temp\input_21
 Sending the status to Manager Node : MAP_SUCCESS
 Manager Disconnected
 Restarting the node to let know the manager for new jobs
 
 Looking for a Manager
 Joined Manager Node :9080 Hostname :localhost

```
 
 #### Challenges
 
 - Dynamically assigning tasks to the worker nodes
 - Network programming as i ma new to network programmming and design paradigms i have spent considerable time to understand the concepts
 - Multi-threading and concurrency- If you look at some of the tasks in the manager are being performed concurrently (Hearbeat, assigning tasks and listening to new worker nodes on the network)
 - Designing the intital map reduce solution that can fit all the varieties of data types both custom and built in
 
 
 



