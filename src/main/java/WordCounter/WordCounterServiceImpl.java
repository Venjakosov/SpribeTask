package WordCounter;

import java.util.concurrent.ConcurrentHashMap;

public class WordCounterServiceImpl implements WordCounterService {

    private static class WCInstance {
        static final WordCounterServiceImpl INSTANCE = new WordCounterServiceImpl();
    }

    private WordCounterServiceImpl() {

    }

    public static WordCounterServiceImpl getInstance() {
        return WCInstance.INSTANCE;
    }

    private ConcurrentHashMap<String, Integer> wordCounter = new ConcurrentHashMap<String, Integer>();

    public int getWordCount(String word) {
        word = word.toLowerCase();
        Integer count = wordCounter.get(word);
        if (count == null) {
            return 0;
        } else return count;
    }

    public synchronized void sendWord(String word) {
        word = word.toLowerCase();
        Integer count = getWordCount(word);
        wordCounter.put(word, ++count);
    }
}
