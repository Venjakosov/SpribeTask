package FindOpponent;

import java.util.List;

public interface FindOpponentService {

    public void sendReadyToPlayList(List<Player> readyToPlayList);
    public Player findOpponent(Player player) throws PlayerNotFoundException;

}
