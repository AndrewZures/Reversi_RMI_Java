import java.rmi.Naming;

public class ReversiServant{
    public ReversiServant(){
        try {
            ReversiBoard game = new ReversiBoard();
            //1099 is default rmi port and doesn't need to be specified
            //localhost is default server and doesn't need to be specified
            //	    Naming.rebind("//localhost:1099/tic-tac-toe", game);
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
