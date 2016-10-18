package WordCounter;

import java.util.concurrent.ConcurrentHashMap;

public class WordCounterServiceImpl implements WordCounterService {

    private static class WCInstance {
        static final WordCounterServiceImpl INSTANCE = new WordCounterServiceImpl();
    }

    private WordCounterServiceImpl(){

    }

    public static WordCounterServiceImpl getInstance(){
        return WCInstance.INSTANCE;
    }

    private ConcurrentHashMap<String, Integer> wordCounter = new ConcurrentHashMap<String, Integer>();

    public int getWordCount(String word) {
        word = word.toLowerCase();
        Integer count = wordCounter.get(word);
        if (count == null){
            return 0;
        }else return count;
    }

    public void sendWord(String word) {
        word = word.toLowerCase();
        int count = getWordCount(word);
        if(count == 0){
            wordCounter.put(word, count + 1);
        }else{
            wordCounter.put(word, ++count);
        }
    }

    public ConcurrentHashMap<String, Integer> getWordCounter() {
        return wordCounter;
    }

}
