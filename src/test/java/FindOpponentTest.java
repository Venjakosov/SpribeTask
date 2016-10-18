import FindOpponent.FindOpponentServiceImpl;
import FindOpponent.Player;
import FindOpponent.PlayerNotFoundException;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FindOpponentTest extends Assert {

    private static final FindOpponentServiceImpl fos = FindOpponentServiceImpl.getInstance();

    @Before
    public void setUp() throws Exception {
        List<Player> readyToPlayList = new ArrayList<Player>();
        readyToPlayList.add(new Player("petia", 3.3));
        readyToPlayList.add(new Player("venja", 2.1));
        readyToPlayList.add(new Player("vasia", 4.5));
        readyToPlayList.add(new Player("pupkin", 3.0));
        readyToPlayList.add(new Player("katya", 6.0));
        fos.sendReadyToPlayList(readyToPlayList);
    }

    @After
    public void tearDown() throws Exception {
        fos.sendReadyToPlayList(Collections.<Player>emptyList());
    }

    @Test
    public void testFindOpponent() throws Exception {
        Player testPlayer1 = new Player("Robot", 4.6);
        Player testPlayer2 = new Player("Bomba", 1.0);
        Player testPlayer3 = new Player("Koka", 5.5);

        Player exceptedPlayer1 = new Player("vasia", 4.5);
        Player exceptedPlayer2 = new Player("venja", 2.1);
        Player exceptedPlayer3 = new Player("katya", 6.0);

        Player actualPlayer1 = fos.findOpponent(testPlayer1);
        Player actualPlayer2 = fos.findOpponent(testPlayer2);
        Player actualPlayer3 = fos.findOpponent(testPlayer3);

        Assert.assertEquals(exceptedPlayer1, actualPlayer1);
        Assert.assertEquals(exceptedPlayer2, actualPlayer2);
        Assert.assertEquals(exceptedPlayer3, actualPlayer3);

        //After finding 3 opponents size might be 2
        int exceptedListSize = 2;
        int actualListSize = fos.getReadyToPlayList().size();

        Assert.assertEquals(exceptedListSize, actualListSize);
    }

    @Test(expected = PlayerNotFoundException.class)
    public void testFOException() throws PlayerNotFoundException{
        fos.sendReadyToPlayList(null);
        fos.findOpponent(new Player("Test", 1.0));
    }
}
