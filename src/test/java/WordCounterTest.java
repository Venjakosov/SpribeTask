import WordCounter.WordCounterServiceImpl;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

}
