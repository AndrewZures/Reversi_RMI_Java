import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ReversiBoard extends UnicastRemoteObject implements ReversiBoardInterface {
    private final int OPEN = 0;
    private final int INVALIDMOVE = -1;
    private final int PLAYER1 = 1;
    private final int PLAYER2 = 2;

    private int newPlayerCounter = 0;
    private boolean gameOn = true;
    private int currentPlayer = 1;
    private int update = 0;
    private int[] boardArray = null;

    public int getNewPlayer(){
        return ++newPlayerCounter;
    }

    public ReversiBoard() throws RemoteException {
        super();
        boardArray = new int[64];
        this.initializeBoard();
        this.setInitialPieces();
    }

    private void initializeBoard(){
        for(int i = 0; i < boardArray.length; i++) boardArray[i] = OPEN;
    }

    private void setInitialPieces(){
      boardArray[27] = PLAYER1;
      boardArray[36] = PLAYER1;
      boardArray[28] = PLAYER2;
      boardArray[35] = PLAYER2;
    }

    public boolean resetBoard() throws RemoteException {
        if(!gameOn){
            this.initializeBoard();
            this.setInitialPieces();
            return true;
        }
        else return false;
    }

    public int[] getBoard() throws RemoteException { return boardArray; }

    public String getCurrentPlayer() {
        if(currentPlayer == 1) return "Player1";
        else {
            return "Player2";
        }
    }

    public String getScoreString() throws RemoteException {
        return "Score: Player1: "+getScore(1)+"  Player2: "+getScore(2);
    }

    public boolean myTurn(int player){
        return player == currentPlayer;
    }

    public String getBoardString(){return Arrays.toString(boardArray);}

    public boolean makeMove(int move, int player) throws RemoteException {
        if(validateChosenMove(move, player) && player == currentPlayer){
            boardArray[move] = player;
            updateBoard(player, move);
            currentPlayer = opponent(player);
            update++;
            return true;
        } else return false;
    }

    private boolean validateChosenMove(int index, int player) throws RemoteException {
        if(indexIsOutOfBounds(index)) return false;
        ArrayList<Integer> validMoves = this.getValidMoves(player);
        return validMoves.contains(index);
    }

    public boolean indexIsOutOfBounds(int index){
        return index < 0 || index >= boardArray.length;
    }

    public ArrayList<Integer> getValidMoves(int player) throws RemoteException {
        ArrayList<Integer> playerPieces = findPlayerPieces(player);
        ArrayList<Integer> legalMoves = new ArrayList<Integer>();
        for (Integer playerPiece : playerPieces)
            legalMoves = findLegalMoves(playerPiece, player, legalMoves);
        Collections.sort(legalMoves);
        return legalMoves;
    }

    public ArrayList<Integer> findLegalMoves(int startIndex, int player, ArrayList<Integer> moves){
        for (int offset : getOffsets()) {
            int move = findMove(startIndex, offset, player);
            if (move != INVALIDMOVE && !adjacentMove(startIndex,offset,move) && !moves.contains(move))
                moves.add(move);
        }
        return moves;
    }

    public int findMove(int index, int offset, int player){
        int nextIndex = index+offset;
        if(indexIsOutOfBounds(nextIndex)) return INVALIDMOVE;
        else if(boardArray[nextIndex] == OPEN) return nextIndex;
        else if(boardArray[nextIndex] == opponent(player))
                return findMove(nextIndex, offset, player);
        else return INVALIDMOVE;
    }

    public boolean searchForUpdate(int startIndex, int currentIndex, int offset, int player){
        int nextIndex = currentIndex + offset;
        if(indexIsOutOfBounds(nextIndex) || boardArray[nextIndex] == OPEN) return false;
        else if(boardArray[nextIndex] == opponent(player)){
            boolean update = searchForUpdate(startIndex, nextIndex, offset, player);
            if(update){
                boardArray[nextIndex] = player;
                return true;
            }
        }
        else if(boardArray[nextIndex] == player && !adjacentMove(startIndex,offset,nextIndex))
             return true;
        return false;
    }

    public int[] getOffsets(){
        return new int[]{1, -1, -8, 8, 9, -9, 7, -7};
    }

    public boolean adjacentMove(int origIndex, int offset, int testIndex){
        return testIndex == origIndex+offset;
    }

    public void updateBoard(int player, int move) throws RemoteException {
        for(int offset : getOffsets()) searchForUpdate(move, move, offset, player);
    }

    public ArrayList<Integer> findPlayerPieces(int player) throws RemoteException {
        ArrayList<Integer> playerPieces = new ArrayList<Integer>();
        for(int i = 0; i < boardArray.length; i++)
            if(boardArray[i] == player) playerPieces.add(i);
        return playerPieces;
    }

    public int checkGameState() throws RemoteException {
        ArrayList<Integer> player1Moves = getValidMoves(PLAYER1);
        ArrayList<Integer> player2Moves = getValidMoves(PLAYER2);
        if(player1Moves.isEmpty() && player2Moves.isEmpty()){
            this.gameOn = false;
            return 0;
        }
        else return 1;
    }

    public int getScore(int player) throws RemoteException {
        int score = 0;
        for(int piece : boardArray) if(piece == player) score++;
        return score;
    }

    public int opponent(int player){
        return player == PLAYER1 ? PLAYER2 : PLAYER1;
    }
}
