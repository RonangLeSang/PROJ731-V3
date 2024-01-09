import java.util.ArrayList;
import java.util.HashMap;
import java.time.Duration;
import java.time.Instant;
public class Main {

    public static ArrayList<Mapper> createWorkers(int nbWorkers, ArrayList<Reducer> reducers){
        ArrayList<Mapper> workers = new ArrayList<Mapper>();
        for(int i = 0; i < nbWorkers; i++){
            workers.add(new Mapper(reducers));
        }
        return workers;
    }

    public static void startWorkers(ArrayList<Mapper> workers, ArrayList<ArrayList<String>> splittedWork){
        for(int i = 0; i < workers.size(); i++){
            workers.get(i).setText(splittedWork.get(i));
            workers.get(i).start();
        }
    }

    public static void isWorkerFinished(ArrayList<Mapper> workers){
        try{
            for(Mapper worker : workers){
                worker.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void isReducerFinished(ArrayList<Reducer> reducers){
        try{
            for(Reducer reducer : reducers){
                reducer.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Reducer> createReducer(int nbReducer){
        ArrayList<Reducer> reducers = new ArrayList<Reducer>();
        for(int i = 0; i < nbReducer; i++){
            reducers.add(new Reducer());
        }
        return reducers;
    }

    public static void startReducers(ArrayList<Reducer> reducers){
        for(Reducer reducer : reducers){
            reducer.start();
        }
    }

    public static HashMap<String, Integer> join(ArrayList<Reducer> reducers){
        HashMap<String, Integer> res = new HashMap<String, Integer>();
        for(Reducer reducer : reducers){
            res.putAll(reducer.getFullHashMap());
        }
        return res;
    }

    public static void main(String[] args) {

        int nbWorkers = 1;
        int nbReducer = 1;

        System.out.println("pretreatment of text");
        long timer_text_treatment_start = System.currentTimeMillis();

        Coordinator coordinator = new Coordinator();
        String text = coordinator.read("data/bible.txt");
        Splitter splitter = new Splitter();
        long timer_text_treatment_end = System.currentTimeMillis();


        ArrayList<String> splittedSentences = splitter.splitPhrases(splitter.normalization(text));

        ArrayList<Reducer> reducers = createReducer(nbReducer);

        ArrayList<Mapper> workers = Main.createWorkers(nbWorkers, reducers);

        ArrayList<ArrayList<String>> splittedWork = Coordinator.splitList(splittedSentences, nbWorkers);

        long timer_total_process_start = System.currentTimeMillis();
        long timer_single_process_worker_start = System.currentTimeMillis();

        startWorkers(workers, splittedWork);

        isWorkerFinished(workers);

        long timer_single_process_worker_end = System.currentTimeMillis();
        long timer_single_process_reducer_start = System.currentTimeMillis();

        startReducers(reducers);

        isReducerFinished(reducers);

        HashMap<String, Integer> res = join(reducers);

        long timer_single_process_reducer_end = System.currentTimeMillis();
        long timer_total_process_end = System.currentTimeMillis();

        System.out.println(res);

        System.out.println("Text pretreatment: " + (timer_text_treatment_end - timer_text_treatment_start) + " ms");
        System.out.println("Workers timer: " + (timer_single_process_worker_end - timer_single_process_worker_start) + " ms");
        System.out.println("Reducers timer: " + (timer_single_process_reducer_end - timer_single_process_reducer_start) + " ms");
        System.out.println("Total time of the map reduce: " + (timer_total_process_end - timer_total_process_start) + " ms");

    }
}
