import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static ArrayList<Mapper> createWorkers(int nbWorkers){
        ArrayList<Mapper> workers = new ArrayList<Mapper>();
        for(int i = 0; i < nbWorkers; i++){
            workers.add(new Mapper());
        }
        return workers;
    }

    public static void startWorkers(ArrayList<Mapper> workers, ArrayList<ArrayList<String>> splittedWork, int nbReducer){
        for(int i = 0; i < workers.size(); i++){
            workers.get(i).setNbReducer(nbReducer);
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

    public static void setReducers(ArrayList<Mapper> workers, ArrayList<Reducer> reducers, int nbReducer){
        for(int i = 0; i < workers.size(); i++){
            for(int j = 0; j < nbReducer; j++){
                reducers.get(j).addHashMap(workers.get(i).getShuffledResult().get(j));
            }
        }
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

        int nbWorkers = 3;
        int nbReducer = 2;

        Coordinator coordinator = new Coordinator();
        String text = coordinator.read("data/bible.txt");
        Splitter splitter = new Splitter();
        ArrayList<String> splittedSentences = splitter.splitPhrases(splitter.normalization(text));

        ArrayList<Mapper> workers = Main.createWorkers(nbWorkers);

        ArrayList<ArrayList<String>> splittedWork = Coordinator.splitList(splittedSentences, nbWorkers);

        startWorkers(workers, splittedWork, nbReducer);

        isWorkerFinished(workers);

        ArrayList<Reducer> reducers = createReducer(nbReducer);
        setReducers(workers, reducers, nbReducer);
        startReducers(reducers);

        isReducerFinished(reducers);

        HashMap<String, Integer> res = join(reducers);
        System.out.println(res);

    }
}
