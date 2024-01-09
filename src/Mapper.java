import java.util.ArrayList;
import java.util.HashMap;

public class Mapper extends Thread{

    private WordCounter counter;
    private HashMap<String, Integer> wordCount = null;
    private ArrayList<HashMap<String, Integer>> shuffledResult = null;
    private int nbReducer;
    private ArrayList<Reducer> reducers;


    public Mapper(ArrayList<Reducer> reducers) {
        nbReducer = reducers.size();
        this.counter = new WordCounter();
        this.reducers = reducers;
    }

    public void setText(ArrayList<String> text){
        this.counter.setText(text);
    }

    public HashMap<String, Integer> getWordCount() {
        return wordCount;
    }

    public ArrayList<HashMap<String, Integer>> getShuffledResult() {
        return shuffledResult;
    }

    private void shuffle(){
        shuffledResult = Coordinator.shuffle(nbReducer, wordCount);
    }

    public void initReducers(){
        for(int j = 0; j < nbReducer; j++){
            reducers.get(j).addHashMap(this.getShuffledResult().get(j));
        }
    }

    @Override
    public void run() {
        this.wordCount = counter.count();
        shuffle();
        initReducers();
    }
}
