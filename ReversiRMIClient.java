import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ReversiRMIClient extends UnicastRemoteObject implements ReversiRMIClientInterface {
    private ReversiBoardInterface board = null;
    private int playerNum;
    java.util.Scanner scan = new java.util.Scanner(System.in);

    public ReversiRMIClient(ReversiBoardInterface board) throws RemoteException {
        this.board = board;
        this.playerNum = board.getNewPlayer();
    }

    public static void main(String[] args){
        try{
            ReversiBoardInterface game = (ReversiBoardInterface) java.rmi.Naming.lookup("reversi");
            ReversiRMIClient client = new ReversiRMIClient(game);
            client.go();
        }
        catch (Exception e){System.out.println(e);}
    }

    public void go() throws Exception{
        printGameInstructions();
        if(playerNum > 2)
            this.observerGo();
        else this.playerGo();
    }

    public void observerGo() throws Exception {
        System.out.println("YOU ARE AN OBSERVER");
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
        while(contGame){
            if(board.gameOver()){
                board.reset();
                if(this.askForNewGame())
                    this.printGameInstructions();
                else{break;}
            }
            if(!myturn()) {
                this.updateBoard();
                this.printScore();
                this.waitMessage();
            }
            while(!myturn()) Thread.sleep(3000);

            this.updatePlayer();
            while (myturn()){
                System.out.print("Player" + playerNum + " >>");
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

        if(action.equalsIgnoreCase("p")){
            this.printBoard(board.getBoardString());
            return;
        }
        else if(action.equalsIgnoreCase("v")){
            this.printValidMoves();
            return;
        }
        else if(action.equalsIgnoreCase("l")){
            printCurrentPlayer();
            return;
        }
        else if(action.equalsIgnoreCase("myturn?")){
            boolean result = board.myTurn(playerNum);
            if(result) reply = "yes";
            else reply = "no";
            return;
        }
        else if(action.equalsIgnoreCase("s")){
            this.printScore();
        }

        try{
            int move = Integer.valueOf(action);
            this.makeMove(move);
            System.out.println(reply);
        }
        catch (NumberFormatException nfe){
            printInputError();
        }
    }

    public void updatePlayer() throws Exception {
        this.updateBoard();
        this.printScore();
        this.printValidMoves();
        this.myTurnMessage();
    }

    private boolean myturn() throws Exception {
        return board.myTurn(playerNum);
    }

    private void makeMove(int move) throws Exception{
        boolean result = board.makeMove(move, playerNum);
        if(result)
            System.out.println("Move Accepted");
        else System.out.println("Move Denied, Try Again");
    }

    private void printInputError(){
        System.out.println("Invalid Move, Try Again");
    }

    private String getCurrentPlayer() throws Exception{
        return board.getCurrentPlayer();
    }

    private void printCurrentPlayer() throws Exception{
        System.out.println("Current Player is: "+board.getCurrentPlayer());
    }

    private void printPlayerTurn() throws Exception{
        System.out.println("Turn: "+getCurrentPlayer());
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

    private void printValidMoves() throws Exception{
        System.out.println("Valid Moves: " + board.getValidMoves(playerNum));
    }

    private void printGameInstructions(){
        System.out.println("WELCOME TO REVERSI!!!");
        System.out.println("    NOTE: Player1 is '(@)', Player2 is '(_)");
        System.out.println("    NOTE: '*' indicates a legal move!");
    }

    private boolean askForNewGame(){
        System.out.println("Game Over");
        System.out.println("Would you like to play a new game? press 'Y' if so, any other key to exit");
        String result = scan.nextLine();
        if(result.equalsIgnoreCase("y")){
            return true;
        }
        else return false;
    }

    private void myTurnMessage(){
        System.out.println("Your Turn!  Choose:");
        System.out.println("     A valid move number e.g. '19' to take spot 19");
        System.out.println("    'V': get valid moves");
        System.out.println("    'P': print board");
        System.out.println("    'S': get game score");
        System.out.println("    'L': player with current turn");
    }

    public void printBoard(String boardArray) throws Exception{
        StringTokenizer tokenizer = new StringTokenizer(boardArray);
        String element = "";

        ArrayList<Integer> moveList = new ArrayList<Integer>();
        if(myturn()) moveList = board.getValidMoves(playerNum);

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
}
