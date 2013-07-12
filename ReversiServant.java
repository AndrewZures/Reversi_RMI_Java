import java.rmi.Naming;

public class ReversiServant{
    public ReversiServant(){
        try {
            ReversiBoard game = new ReversiBoard();
            Naming.rebind("reversi", game);
            System.out.println("reversi server bound");
        } catch (Exception e) {
            System.err.println("Problem occurred: " + e.getMessage());
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        ReversiServant revServ = new ReversiServant();
    }
}
