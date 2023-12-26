import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Coordinator {

    String text = "";

    public static ArrayList<ArrayList<String>> splitList(ArrayList<String> listPhrases, int nbWorkers){
        ArrayList<ArrayList<String>> result = new ArrayList<>();

        int sublistSize = listPhrases.size() / nbWorkers;
        int remainder = listPhrases.size() % nbWorkers;

        int startIndex = 0;
        for (int i = 0; i < nbWorkers; i++) {
            int endIndex = startIndex + sublistSize + (i < remainder ? 1 : 0);
            result.add(new ArrayList<>(listPhrases.subList(startIndex, endIndex)));
            startIndex = endIndex;
        }
        return result;
    }

    public static ArrayList<HashMap<String, Integer>> shuffle(int nbReducer, HashMap<String, Integer> hashMapToSplit){
        ArrayList<HashMap<String, Integer>> res = new ArrayList<HashMap<String, Integer>>();
        for(int i = 0; i < nbReducer; i++){
            res.add(new HashMap<String, Integer>());
        }
        for (String key : hashMapToSplit.keySet()) {
            if(key != ""){
                int hash = 0;
                if(key.length() > 2){
                    hash = key.substring(0,2).hashCode();
                }else {
                    hash = key.hashCode();
                }
                res.get(hash % nbReducer).put(key, hashMapToSplit.get(key));
            }
        }
        return res;
    }

    public String read(String path) {
        try (FileReader fileReader = new FileReader(path);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                text += line;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

}
