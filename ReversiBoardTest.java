import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.testng.AssertJUnit.assertEquals;

public class ReversiBoardTest {
    ReversiBoard board = new ReversiBoard();

    @Test
    public void setUp() throws Exception {
        board = new ReversiBoard();
    }

    @Test
    public void initialBoardSetup() {
         int[] resultArray = board.getBoard();
         assertEquals("test", 64, resultArray.length);
    }

    @Test
    public void setInitialPieces(){
        int[] resultArray = board.getBoard();
        assertEquals(1, resultArray[27]);
        assertEquals(1, resultArray[36]);
        assertEquals(2, resultArray[28]);
        assertEquals(2, resultArray[35]);
    }

    @Test
    public void findMoveFindsRows(){
        assertEquals(29, board.findMove(27, 1, 1));
        assertEquals(25, board.findMove(26,-1,1));

        assertEquals(26, board.findMove(28,-1,2));
        assertEquals(29, board.findMove(28,1,2));
    }

    @Test
    public void findMoveFindsColumns(){
        assertEquals(43, board.findMove(27,8,1));
        assertEquals(19, board.findMove(27,-8,1));
        assertEquals(44, board.findMove(36,8,1));

        assertEquals(19, board.findMove(35,-8,2));
    }

    @Test
    public void findMoveFindsDiagonals(){
        assertEquals(-1, board.findMove(36,-9,1));
        assertEquals(-1, board.findMove(35,-7,2));
    }

    @Test
    public void findLegalMoves(){
        ArrayList<Integer>testList = new ArrayList<Integer>();
        ArrayList<Integer>correctList = new ArrayList<Integer>();
        correctList.add(29);
        correctList.add(43);
        assertEquals(correctList, board.findLegalMoves(27, 1, testList));
        correctList.add(34);
        correctList.add(20);
        Collections.sort(correctList);
        ArrayList<Integer> results = board.findLegalMoves(36, 1, testList);
        Collections.sort(results);
        assertEquals(correctList, results);
    }

    @Test
    public void getValidMoves(){
        ArrayList<Integer>correctList = new ArrayList<Integer>();
        correctList.add(20);
        correctList.add(29);
        correctList.add(34);
        correctList.add(43);

        Collections.sort(correctList);
        ArrayList<Integer> results = board.getValidMoves(1);
        Collections.sort(results);
        assertEquals(correctList, results);
    }

    @Test
    public void findPlayerPieces(){
        ArrayList<Integer>correctList = new ArrayList<Integer>();
        correctList.add(27);
        correctList.add(36);
        assertEquals(correctList, board.findPlayerPieces(1));
    }

    @Test
    public void makeMove(){
        assertEquals(true, board.makeMove(29, 1));
        assertEquals(false, board.makeMove(45, 1));
    }

    @Test
    public void indexIsOutOfBounds(){
        assertEquals(true, board.indexIsOutOfBounds(64));
        assertEquals(false, board.indexIsOutOfBounds(63));
        assertEquals(true, board.indexIsOutOfBounds(-1));
        assertEquals(false, board.indexIsOutOfBounds(0));
    }

    @Test
    public void updateBoard1(){
        int[] boardArray = board.getBoard();
        assertEquals(2, boardArray[28]);

        board.makeMove(29, 1);
        boardArray = board.getBoard();
        assertEquals(1, boardArray[28]);
        assertEquals(1, boardArray[29]);
    }

    @Test
    public void updateBoard2(){
        int[] boardArray = board.getBoard();
        assertEquals(1, boardArray[36]);

        board.makeMove(44, 2);
        boardArray = board.getBoard();
        assertEquals(2, boardArray[36]);
        assertEquals(2, boardArray[44]);
    }

    @Test
    public void opponent(){
        assertEquals(2, board.opponent(1));
        assertEquals(1, board.opponent(2));
    }

}
