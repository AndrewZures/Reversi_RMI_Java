import java.util.ArrayList;

public interface BoardInterface {

    public int[] getBoard();
    public int getUpdate();
    public String getCurrentPlayer();
    public int getScore(int player);
    public boolean makeMove(int move, int player);
    public int checkGameState();
    public ArrayList<Integer> getValidMoves(int player);
    public void printBoard();

}
