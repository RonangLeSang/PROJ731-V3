import java.util.ArrayList;
import java.util.HashMap;

public class Reducer extends Thread{

    private ArrayList<HashMap<String, Integer>> hashList = new ArrayList<HashMap<String, Integer>>();
    private HashMap<String, Integer> fullHashMap = new HashMap<>();

    public Reducer(){}

    public Reducer(ArrayList<HashMap<String, Integer>> hashList) {
        this.hashList = hashList;
    }

    public HashMap<String, Integer> getFullHashMap() {
        return fullHashMap;
    }

    public void addHashMap(HashMap<String, Integer> hashMap) {
        this.hashList.add(hashMap);
    }

    @Override
    public void run() {
        for (HashMap<String, Integer> hash : hashList) {
            hash.forEach((key, value) -> fullHashMap.merge(key, value, Integer::sum));
        }
    }

}