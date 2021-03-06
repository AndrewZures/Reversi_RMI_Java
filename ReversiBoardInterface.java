import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ReversiBoardInterface extends Remote {
    public ArrayList<Integer> getValidMoves(int player) throws RemoteException;
    public boolean makeMove(int move, int player) throws RemoteException;
    public boolean myTurn(int playerNum) throws RemoteException;
    public int getNewPlayer() throws RemoteException;
    public String getCurrentPlayer() throws RemoteException;
    public int[] getBoard() throws RemoteException;
    public String getBoardString() throws RemoteException;
    public int getScore(int player) throws RemoteException;
    public String getScoreString() throws RemoteException;
    public void reset() throws RemoteException;
    public boolean gameOver() throws RemoteException;

}
