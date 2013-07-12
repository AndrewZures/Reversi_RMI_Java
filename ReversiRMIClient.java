import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ReversiRMIClient extends UnicastRemoteObject implements ReversiRMIClientInterface {
    private ReversiBoardInterface board = null;
    private String playerName;
    private int playerNum;
    java.util.Scanner scan = new java.util.Scanner(System.in);

    public ReversiRMIClient(ReversiBoardInterface board) throws RemoteException {
        this.board = board;
        this.playerNum = board.getNewPlayer();
    }

    public void go() throws Exception{
        System.out.println("YOU ARE: "+playerNum);
        if(playerNum > 2)
            this.observerGo();
        else this.playerGo();
    }


    public void observerGo() throws Exception {
        boolean gameOn = true;
        this.printPlayerTurn();
        this.updateBoard();
        String currentPlayer = getCurrentPlayer();
        String player;
        while(gameOn){
            player = getCurrentPlayer();
            if(!player.equalsIgnoreCase(currentPlayer)){
                this.printPlayerTurn();
                printScore();
                updateBoard();
                currentPlayer = player;
            }
            Thread.sleep(3000);
        }
    }

    public void playerGo() throws Exception{
        boolean contGame = true;
        while(board.checkGameState() == 1 && contGame){

            if(!myturn()) {
                this.updateBoard();
                this.printScore();
                this.waitMessage();
            }
            while(!myturn()) Thread.sleep(3000);

            this.updateBoard();
            this.printScore();
            this.printValidMoves();
            this.myTurnMessage();
            while (myturn()){
                String input = scan.nextLine();
                if (input.equals("exit")){
                    contGame = !contGame;
                    break;
                }
                executeInput(input);

                Thread.sleep(1000);
            }
        }
    }

    public void executeInput(String input) throws Exception {
        String reply = "";
        StringTokenizer tokenizer = new StringTokenizer(input);
        String action = tokenizer.nextToken();

        try{
            int move = Integer.valueOf(action);
            this.makeMove(move);

            if(action.equalsIgnoreCase("p"))
                this.printBoard(board.getBoardString());

            if(action.equalsIgnoreCase("v"))
                this.printValidMoves();

            if(action.equalsIgnoreCase("l"))
                printCurrentPlayer();

            if(action.equalsIgnoreCase("myturn?")){
                boolean result = board.myTurn(playerNum);
                if(result) reply = "yes";
                else reply = "no";
            }

            if(action.equalsIgnoreCase("s")){
                this.printScore();
            }

            System.out.println(reply);
        }
        catch (NumberFormatException nfe){
            printInputError();
        }

    }

    private void printInputError(){
        System.out.println("Invalid Move, Try Again");
    }

    private void printCurrentPlayer() throws Exception{
        System.out.println(board.getCurrentPlayer());
    }


    private ArrayList<Integer> getIntegerMovesList() throws Exception{
        String temp = ""+board.getValidMoves(playerNum);
        temp = temp.replaceAll("\\[", "").replaceAll("\\]","");
        String moves = temp.replaceAll(",", "");
        StringTokenizer moveTokenizer = new StringTokenizer(moves);
        ArrayList<Integer> moveList =new ArrayList<Integer>();
        while(moveTokenizer.hasMoreTokens()){
            String move = moveTokenizer.nextToken();
            int num = Integer.parseInt(move);
            moveList.add(num);
        }
        return moveList;
    }

    public void printBoard(String board) throws Exception{
        StringTokenizer tokenizer = new StringTokenizer(board);
        String element = "";

        ArrayList<Integer> moveList = new ArrayList<Integer>();
        if(myturn()){
            moveList = getIntegerMovesList();
        }

        int count = 0;
        System.out.println("----------------------------------------------------------");
        while(tokenizer.hasMoreTokens()){
            element = tokenizer.nextToken();
            if(element.equalsIgnoreCase("1,")){
                System.out.print("(@)\t");
            }
            else if(element.equalsIgnoreCase("2,")){
                System.out.print("(_)\t");
            }
            else if(moveList.contains(count)){
                System.out.print(count+"*\t");
            }
            else System.out.print(count+"\t");

            if((count+1)%8 == 0){
                System.out.println("");
            }
            count++;
        }
        System.out.println("----------------------------------------------------------");
    }

    private boolean myturn() throws Exception {
        return board.myTurn(playerNum);
    }

    private void updateBoard() throws Exception {
        this.printBoard(board.getBoardString());
    }

    private void printScore() throws Exception {
        System.out.println(board.getScoreString());
    }

    private void waitMessage(){
        System.out.println("Waiting for Other Player To Move...");
    }

    private void myTurnMessage(){
        System.out.println("Your Turn!  Choose:");
        System.out.println("    'V': get valid moves");
        System.out.println("    'P': print board");
        System.out.println("    'S': get game score");
        System.out.println("    'L': player with current turn");
    }

    private void printValidMoves() throws Exception{
        System.out.println("Valid Moves: " + board.getValidMoves(playerNum));
    }

    private String getCurrentPlayer() throws Exception{
        return board.getCurrentPlayer();
    }

    private void makeMove(int move) throws Exception{
        boolean result = board.makeMove(move, playerNum);
        if(result)
            System.out.println("Move Accepted");
        else System.out.println("Move Denied, Try Again");
    }

    private void printPlayerTurn() throws Exception{
        System.out.println("Turn: "+getCurrentPlayer());
    }

    public static void main(String[] args){
        try{
            ReversiBoardInterface game = (ReversiBoardInterface) java.rmi.Naming.lookup("reversi");
            ReversiRMIClient client = new ReversiRMIClient(game);
            client.go();
        }
        catch (Exception e){System.out.println(e);}

    }

}
