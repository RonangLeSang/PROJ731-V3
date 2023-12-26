import java.util.ArrayList;
import java.util.HashMap;

public class Mapper extends Thread{

    private WordCounter counter;
    private HashMap<String, Integer> wordCount = null;
    private ArrayList<HashMap<String, Integer>> shuffledResult = null;

    public Mapper(ArrayList<String> text, int nbReducer) {
        this.counter = new WordCounter(text, nbReducer);
    }

    public Mapper() {
        this.counter = new WordCounter();
    }

    public void setText(ArrayList<String> text){
        this.counter.setText(text);
    }

    public void setNbReducer(int nbReducer) {
        this.counter.setNbReducer(nbReducer);
    }

    public HashMap<String, Integer> getWordCount() {
        return wordCount;
    }

    public ArrayList<HashMap<String, Integer>> getShuffledResult() {
        return shuffledResult;
    }

    private void shuffle(){
        shuffledResult = Coordinator.shuffle(counter.getNbReducer(), wordCount);
    }

    @Override
    public void run() {
        this.wordCount = counter.count();
        shuffle();
    }
}
