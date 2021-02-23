package reversi;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * CSCI1130 Java Assignment Reversi board game
 *
 * Students shall complete this class to implement the game. There are
 * debugging, testing and demonstration code for your reference.
 *
 * I declare that the assignment here submitted is original except for source
 * material explicitly acknowledged, and that the same or closely related
 * material has not been previously submitted for another course. I also
 * acknowledge that I am aware of University policy and regulations on honesty
 * in academic work, and of the disciplinary guidelines and procedures
 * applicable to breaches of such policy and regulations, as contained in the
 * website.
 *
 * University Guideline on Academic Honesty:
 * http://www.cuhk.edu.hk/policy/academichonesty Faculty of Engineering
 * Guidelines to Academic Honesty:
 * https://www.erg.cuhk.edu.hk/erg/AcademicHonesty
 *
 * Student Name: COO Ric Lim Student ID : 1155137820 Date : November 10, 2020
 *
 * @author based on skeleton code provided by Michael FUNG
 */
public class Reversi {

    // pre-defined class constant fields used throughout this app
    public static final int BLACK = -1;
    public static final int WHITE = +1;
    public static final int EMPTY = 0;

    // a convenient constant field that can be used by students
    public final int FLIP = -1;

    // GUI objects representing and displaying the game window and game board
    protected JFrame window;
    protected ReversiPanel gameBoard;
    protected Color boardColor = Color.GREEN;

    // a 2D array of pieces, each piece can be:
    //  0: EMPTY/ unoccupied/ out of bound
    // -1: BLACK
    // +1: WHITE
    protected int[][] pieces;

    // currentPlayer:
    // -1: BLACK
    // +1: WHITE
    protected int currentPlayer;

    // STUDENTS may declare other fields HERE
    private static final Direction[] EIGHTDIRECTIONS = Direction.EIGHTDIRECTIONS;

    protected static final Vector[] playingAreaWithoutBorder
            = Vector.makeCartesianProduct(new int[]{1, 2, 3, 4, 5, 6, 7, 8});

    /**
     * The only constructor for initializing a new board in this app
     */
    public Reversi() {
        initializeGui();
        setupBoardDefaultGame();
    }

    private void initializeGui() {
        window = new JFrame("Reversi");
        gameBoard = new ReversiPanel(this);
        window.add(gameBoard);
        window.setSize(850, 700);
        window.setLocation(100, 50);
        window.setVisible(true);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // use of implicitly extended inner class with overriden method, advanced stuff
        window.addWindowListener(
                new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sayGoodbye();
            }
        }
        );
    }

    protected void setupBoardDefaultGame() {
        // a 8x8 board of pieces[1-8][1-8] surrounded by an EMPTY boundary of 10x10 
        pieces = new int[10][10];
        pieces[4][4] = WHITE;
        pieces[4][5] = BLACK;
        pieces[5][4] = BLACK;
        pieces[5][5] = WHITE;

        currentPlayer = BLACK;

        gameBoard.updateStatus(pieces, currentPlayer);
    }

    public void userClicked(int row, int col) {
        Vector clickedPos = new Vector(row, col);
        if (isValidMoveForCurrentPlayer(clickedPos)) {
            placePiece(clickedPos);
            currentPlayer = FLIP * currentPlayer;
            gameBoard.updateStatus(pieces, currentPlayer);
            checkForcedPass();
        } else {
            System.out.printf("%s is an invalid move.\n", clickedPos);
            gameBoard.addText("Invalid Move!");
        }

    }

    private void checkForcedPass() {
        if (!currentPlayerHasValidMove()) {
            gameBoard.addText("Forced Pass");
            currentPlayer = FLIP * currentPlayer;
            if (!currentPlayerHasValidMove()) {
                gameBoard.addText("Double Forced Pass");
                gameBoard.addText("End game!");
            }
        }
    }

    private void placePiece(Vector position) {
        System.out.printf("Placing piece at %s\n", position);
        setPieceAt(position, currentPlayer);

        for (Direction dir : EIGHTDIRECTIONS) {
            if (hasMatch(position, dir)) {
                flipEnemies(position, dir);
            } else {
                System.out.printf("\tNo friends found in the %s direction\n",
                        dir);
            }
        }
    }

    private void flipEnemies(Vector position, Direction direction) {
        Vector indexPos = position.add(direction);
        while (getPieceAt(indexPos) != currentPlayer) {
            setPieceAt(indexPos, currentPlayer);
            indexPos = indexPos.add(direction);
        }
        System.out.printf("\tFriendly disc found at %s\n", indexPos);
    }

    private boolean hasMatch(Vector clickedPos, Direction direction) {
        Vector indexPos = clickedPos.clone();

        // Must be adjacent to an enemy disc.
        indexPos = indexPos.add(direction);
        int enemy = currentPlayer * FLIP;
        if (getPieceAt(indexPos) != enemy) {
            return false;
        }

        // Look for a friendly disc, ignoring all enemy discs.
        while (getPieceAt(indexPos) != currentPlayer) {
            indexPos = indexPos.add(direction);
            if (getPieceAt(indexPos) == EMPTY) {
                return false;
            }
        }
        return true;

    }

    private Boolean isValidMoveForCurrentPlayer(Vector pos) {
        if (getPieceAt(pos) != EMPTY) {
            return false;
        }

        for (Direction direction : EIGHTDIRECTIONS) {
            if (hasMatch(pos, direction)) {
                return true;
            }
        }
        return false;
    }

    private Boolean currentPlayerHasValidMove() {
        for (Vector pos : playingAreaWithoutBorder) {
            if (isValidMoveForCurrentPlayer(pos)) {
                return true;
            }
        }
        return false;
    }

    private void setPieceAt(Vector position, int pieceColor) {
        try {
            pieces[position.getX()][position.getY()] = pieceColor;
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    private int getPieceAt(Vector position) {
        try {
            return pieces[position.getX()][position.getY()];
        } catch (ArrayIndexOutOfBoundsException e) {
            return EMPTY;
        }
    }

    protected void sayGoodbye() {
        System.out.println("Goodbye!");
    }

    public static void main(String[] args) {
        new Reversi();
    }
}
