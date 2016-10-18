import WordCounter.WordCounterServiceImpl;
import junit.framework.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WordCounterTest extends Assert {

    private static final WordCounterServiceImpl wcs = WordCounterServiceImpl.getInstance();

    @Test
    public void shouldReturnThreeWherWordWasAddedThreeTime() {
        final String wordToAddThreeTime = "word_3_times";
        for (int i = 0; i < 3; i++) {
            wcs.sendWord(wordToAddThreeTime);
        }
        int wordToAddThreeTimeCount = wcs.getWordCount(wordToAddThreeTime);
        assertEquals(3, wordToAddThreeTimeCount);

    }

    @Test
    public void shouldReturnZeroWhenWordWasNeverAdded() {
        int wordThatWasNeverAddedCount = wcs.getWordCount("word_that_was_never_added");

        assertEquals(0, wordThatWasNeverAddedCount);
    }

    @Test
    public void shouldReturnOneWhenWordWasAddedOneTime() {
        final String wordToAddOneTime = "wordToAddOneTime";
        wcs.sendWord(wordToAddOneTime);
        int wordToAddOneTimeCount = wcs.getWordCount(wordToAddOneTime);

        assertEquals(1, wordToAddOneTimeCount);
    }

    @Test
    public void correctConcurrencyTest() throws InterruptedException {
        final int n = 1000;
        final int numberOfThreads = 1000;
        final String word = "word";
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        Runnable addWordNTimesTask = new Runnable() {
            public void run() {
                for (int i = 0; i < n; i++) {
                    wcs.sendWord(word);
                }
            }
        };
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(addWordNTimesTask);
        }
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        assertEquals(n * numberOfThreads, wcs.getWordCount(word));
    }
}
