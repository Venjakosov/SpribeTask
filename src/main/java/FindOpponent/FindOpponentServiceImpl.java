package FindOpponent;

import java.util.List;

public class FindOpponentServiceImpl implements FindOpponentService{

    private static class FOInstance {
        static final FindOpponentServiceImpl INSTANCE = new FindOpponentServiceImpl();
    }

    private FindOpponentServiceImpl(){}

    public static FindOpponentServiceImpl getInstance(){
        return FOInstance.INSTANCE;
    }

    private List<Player> readyToPlayList;

    public void sendReadyToPlayList(List<Player> readyToPlayList) {
        this.readyToPlayList = readyToPlayList;
    }

    public Player findOpponent(Player player1) throws PlayerNotFoundException{
        if(readyToPlayList == null || readyToPlayList.isEmpty()){
            throw new PlayerNotFoundException("Нет игроков готовых к игре!");
        }
        double delta = Double.MAX_VALUE;
        double temp;
        Player result;
        int resultIndex = 0;
        for (int i = 0; i < readyToPlayList.size(); i++) {
            if(readyToPlayList.get(i).getRate() > player1.getRate()){
                temp = readyToPlayList.get(i).getRate() - player1.getRate();
            }else{
                temp = player1.getRate() - readyToPlayList.get(i).getRate();
            }
            if(delta > temp){
                delta = temp;
                resultIndex = i;
            }
        }
        result = readyToPlayList.get(resultIndex);
        readyToPlayList.remove(resultIndex);
        return result;
    }

    public List<Player> getReadyToPlayList() {
        return readyToPlayList;
    }
}
