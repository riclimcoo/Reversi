package reversi;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * ReversiOnFile is a subclass of Reversi, adding File I/O capabilities for
 * loading and saving game.
 *
 * I declare that the work here submitted is original except for source material
 * explicitly acknowledged, and that the same or closely related material has
 * not been previously submitted for another course. I also acknowledge that I
 * am aware of University policy and regulations on honesty in academic work,
 * and of the disciplinary guidelines and procedures applicable to breaches of
 * such policy and regulations, as contained in the website.
 *
 * University Guideline on Academic Honesty:
 * http://www.cuhk.edu.hk/policy/academichonesty Faculty of Engineering
 * Guidelines to Academic Honesty:
 * https://www.erg.cuhk.edu.hk/erg/AcademicHonesty
 *
 * Student Name: COO Ric Lim Student ID : 1155137820 Date : November 10, 2020
 *
 */
public class ReversiOnFile extends Reversi {

    public static final char UNICODE_BLACK_CIRCLE = '\u25CF';
    public static final char UNICODE_WHITE_CIRCLE = '\u25CB';
    public static final char UNICODE_WHITE_SMALL_SQUARE = '\u25AB';
    

    // constructor to give a new look to new subclass game
    public ReversiOnFile() {
        window.setTitle("ReversiOnFile");
        gameBoard.setBoardColor(Color.BLUE);
    }

    private int decodePieceChar(char ch) throws InvalidCharException{
        switch (ch) {
            case UNICODE_WHITE_CIRCLE:
                return WHITE;
            case UNICODE_BLACK_CIRCLE:
                return BLACK;
            case UNICODE_WHITE_SMALL_SQUARE:
                return EMPTY;
            default:
                throw new InvalidCharException();
        }
    }

    private char encodePiece(int color) {
        switch (color) {
            case BLACK:
                return UNICODE_BLACK_CIRCLE;
            case WHITE:
                return UNICODE_WHITE_CIRCLE;
            default:
                return UNICODE_WHITE_SMALL_SQUARE;
        }
    }

    public void loadBoard(String filename) {
        // 1) prepare an empty board
        pieces = new int[10][10];

        File saveData = new File(filename);

        try (Scanner reader = new Scanner(saveData, "UTF-8");) {
            readSaveFile(reader);
            sayInConsoleAndGui("Loaded board from " + filename);
        } catch (IOException | InvalidCharException ex) {
            sayInConsoleAndGui("Cannot load board from " + filename);
            setupBoardDefaultGame();
        }
    }
    
    private void readSaveFile(Scanner reader) throws InvalidCharException{
        int margin = 1;
        for (int row = 0; row < 8; row++) {
            String rowString = reader.nextLine();
            for (int col = 0; col < 8; col++) {
                int piece = decodePieceChar(rowString.charAt(col));
                pieces[row + margin][col + margin] = piece;
            }
        }
        currentPlayer = decodePieceChar(reader.nextLine().charAt(0));
        gameBoard.updateStatus(pieces, currentPlayer); 
    }

    private void sayInConsoleAndGui(String s) {
        gameBoard.addText(s);
        System.out.println(s);
    }

    public void saveBoard(String filename) {
        String boardState = boardStateToString();
        
        try (Writer writer = 
                new OutputStreamWriter(
                        new FileOutputStream(filename, false), 
                        "UTF-8")){
            writer.write(boardState);
            sayInConsoleAndGui("Saved board to " + filename);
        } catch (IOException e) {
            sayInConsoleAndGui("Cannot save board to " + filename);
        } 
    }

    private String boardStateToString() {
        String boardState = "";
        for (int x = 1; x < 9; x++) {
            for (int y = 1; y < 9; y++) {
                int piece = pieces[x][y];
                boardState += encodePiece(piece);
            }
            boardState += "\n";
        }
        boardState += encodePiece(currentPlayer);
        return boardState;
    }

    @Override
    protected void sayGoodbye() {
        System.out.println("Goodbye!");
        String filename = JOptionPane.showInputDialog("Save board filename");
        if(filename != null) saveBoard(filename);
    }

    public static void main(String[] args) {
        ReversiOnFile game = new ReversiOnFile();
        
        String filename = JOptionPane.showInputDialog("Load board filename");
        if (filename != null) game.loadBoard(filename);
    }
}
