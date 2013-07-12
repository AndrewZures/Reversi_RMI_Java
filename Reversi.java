import java.util.Scanner;

public class Reversi {
    BoardInterface board = null;

    public static void main(String[] args){
        BoardInterface board1 = new ReversiBoard();
        Scanner sc = new Scanner(System.in);

        for(int j = 0; j < 10; j++){
            getStatus(board1, 1);
            board1.makeMove(sc.nextInt(), 1);

            getStatus(board1, 2);
            board1.makeMove(sc.nextInt(), 2);
        }
        board1.printBoard();

    }

    public static void getStatus(BoardInterface board, int player){
        board.printBoard();
        System.out.println("Player1: " + board.getScore(1) + " Player2: " + board.getScore(2));
        System.out.print("Valid Moves: " + board.getValidMoves(player));
        System.out.println("");
    }

    public Reversi(){
        board = new ReversiBoard();
    }



}
