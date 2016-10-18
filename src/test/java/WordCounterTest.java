import WordCounter.WordCounterServiceImpl;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WordCounterTest extends Assert {

    private static final WordCounterServiceImpl wcs = WordCounterServiceImpl.getInstance();

    @Before
    public void setUp() throws Exception {
        wcs.sendWord("petia");
        wcs.sendWord("pupkin");
        wcs.sendWord("katya");

        wcs.sendWord("vasia");

        wcs.sendWord("venja");
        wcs.sendWord("VENJA");
        wcs.sendWord("VeNjA");
    }

    @After
    public void tearDown(){
        wcs.getWordCounter().clear();
    }

    @Test
    public void testWordCount(){
        final int exceptedVenja = 3;
        final int exceptedVasia = 1;
        final int exceptedLera = 0;

        final int actualVenja = wcs.getWordCount("Venja");
        final int actualVasia = wcs.getWordCount("Vasia");
        final int actualLera = wcs.getWordCount("Lera");

        assertEquals(exceptedVenja, actualVenja);
        assertEquals(exceptedVasia, actualVasia);
        assertEquals(exceptedLera, actualLera);
    }

    @Test
    public void testThreadSafe(){
        wcs.getWordCounter().clear();
        final int threadCount = 100;

        for (int i = 0; i < threadCount; i++) {
            Thread th = new Thread(new Runnable() {
                public void run() {
                    wcs.sendWord("Venja");
                    wcs.getWordCount("sometext");

                    // randomly access to service
                    if(System.currentTimeMillis() % 2 == 0){
                        wcs.sendWord("barsuk");
                        wcs.getWordCount("venja");
                    }else{
                        wcs.sendWord("lera");
                    }
                }
            });
            th.start();
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int actualCount = wcs.getWordCount("venja");
        Assert.assertEquals(threadCount, actualCount);
    }
    /*You should always start with these primitive tests before writing complicated tests*/
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
