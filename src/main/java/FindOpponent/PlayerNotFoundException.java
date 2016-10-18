package FindOpponent;

public class PlayerNotFoundException extends Exception {

    private static final long serialVersionUID = -5301232657724261675L;

    public PlayerNotFoundException() {}

    public PlayerNotFoundException(String message) {super(message);}

    public PlayerNotFoundException(Exception exception) {super(exception);}
}
