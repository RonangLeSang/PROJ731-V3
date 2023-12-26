import java.util.ArrayList;

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

    public static void isFinished(ArrayList<Mapper> workers){
        try{
            for(Mapper worker : workers){
                worker.join();
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

    public static void startReducer(ArrayList<Mapper> workers, ArrayList<Reducer> reducers){

    }

    public static void main(String[] args) {

        int nbWorkers = 3;
        int nbReducer = 2;

        Coordinator coordinator = new Coordinator();
        String text = coordinator.read("data/text_AnewYou.txt");
        Splitter splitter = new Splitter();
        ArrayList<String> splittedSentences = splitter.splitPhrases(splitter.normalization(text));

        ArrayList<Mapper> workers = Main.createWorkers(nbWorkers);

        ArrayList<ArrayList<String>> splittedWork = Coordinator.splitList(splittedSentences, nbWorkers);

        startWorkers(workers, splittedWork, nbReducer);

        isFinished(workers);

//        ArrayList<HashMap<String, Integer>> hashList = new ArrayList<HashMap<String, Integer>>();
//        hashList.add(mapper1.getWordCount());
//        hashList.add(mapper2.getWordCount());
//
//        Reducer reduce = new Reducer(hashList);
//        System.out.println(reduce.testMerge());
    }
}
