package game;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Class that represents a TicTacToe Board object
 * Acts as a wrapper to a 2D array of Piece enums
 */
public class Board {

    public enum Piece { X, O }
    private final Piece[][] board;
    private final Set<Integer> availableMoves;

    private Piece turn;
    private Piece winner;
    private boolean gameOver;

    /**
     * Default constructor for the Board object
     * Initializes all variables to their default states
     */
    public Board() {
        this.board = new Piece[3][3];
        this.availableMoves = new HashSet<>();
        this.turn = Piece.X;
        this.winner = null;
        this.gameOver = false;

        for(int i = 0; i < 9; i ++)
            this.availableMoves.add(i);
    }

    /**
     * Utility constructor for the Board object
     * Only used for allowing Board#createCopy to function properly
     *
     * @see Board#createCopy()
     *
     * @param other
     * The Board object to copy attributes from
     */
    private Board(Board other) {
        this.board = new Piece[3][3];
        for(int i = 0; i < 3; i++)
            System.arraycopy(other.board[i], 0, this.board[i], 0, 3);

        this.availableMoves = new HashSet<>(other.availableMoves);
        this.turn = other.turn;
        this.winner = other.winner;
        this.gameOver = other.gameOver;
    }

    /**
     * Method abstracting the placement of a Piece
     * No piece is required as a parameter because players alternate between moves
     *
     * @param slot
     * The slot the pieces should be placed in
     * Slots are laid out on the board as follows:
     *
     * 0 | 1 | 2
     * ---------
     * 3 | 4 | 5
     * ---------
     * 6 | 7 | 8
     *
     * @return
     * Returns true if move was successfully made, else returns false
     */
    public boolean move(int slot) {
        if(this.gameOver || !this.availableMoves.contains(slot))
            return false;

        this.availableMoves.remove(slot);
        this.board[slot % 3][slot / 3] = this.turn;
        if(this.availableMoves.isEmpty())
            this.gameOver = true;

        if(checkForWinner()) {
            this.gameOver = true;
            this.winner = this.turn;
        }

        this.turn = this.turn == Piece.X ? Piece.O : Piece.X;
        return true;
    }

    /**
     * Private helper method to check for a winner
     * Uses the helper method checkLine() to be more efficient
     *
     * @see Board#checkLine(int...)
     *
     * @return
     * Returns true if a winner was found, else returns false
     */
    private boolean checkForWinner() {
        return checkLine(0,1,2) || checkLine(3,4,5) || checkLine(6,7,8) ||
               checkLine(0,3,6) || checkLine(1,4,7) || checkLine(2,5,8) ||
               checkLine(0,4,8) || checkLine(2,4,6);
    }

    /**
     * Private helper method to check for a winner
     * Checks a single individual line for a winning state
     * Makes up a large part of Board#checkForWinner()
     *
     * @see Board#checkForWinner()
     *
     * @param slots
     * The slots to check for a winner
     * Precondition - There will always be 3 slots provided
     *
     * @return
     * Returns true if all slots are filled by the same player, else returns false
     */
    private boolean checkLine(int... slots) {
        for(int slot : slots)
            if(this.turn != getPiece(slot))
                return false;

         return true;
    }

    /**
     * Resets the Board object to its original state
     * This pretty much acts as creating a new Board
     */
    public void reset() {
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                this.board[i][j] = null;

        for(int i = 0; i < 9; i ++)
            this.availableMoves.add(i);

        this.turn = Piece.X;
        this.winner = null;
        this.gameOver = false;
    }

    /**
     * Gets the Piece enum associated with a certain slot
     * Slots are laid out on the board as follows:
     *
     * 0 | 1 | 2
     * ---------
     * 3 | 4 | 5
     * ---------
     * 6 | 7 | 8
     *
     * @param slot
     * The slot that you want Piece enum for
     *
     * @return
     * Returns the Piece enum on the provided slot
     * If there is not Piece on the slot, returns null
     */
    public Piece getPiece(int slot) {
        return this.board[slot % 3][slot / 3];
    }

    /**
     * Gets the underlying 2D array that acts as the Board
     * This is a raw version of the Board and none of the data can be changed
     *
     * @return
     * Returns a 3x3 2D array representing the raw instance of the Board
     */
    public Piece[][] getBoardRaw() {
        return this.board;
    }

    /**
     * Gets a Set of Integers that holds all the open slots
     * This method is mainly for the convenience of the AI
     *
     * @return
     * Returns an unmodifiable Set of Integers representing empty slots
     */
    public Set<Integer> getAvailableSlots() {
        return Collections.unmodifiableSet(this.availableMoves);
    }

    /**
     * Gets the current turn for the Board
     *
     * @return
     * Returns a Piece enum representing the player's piece
     */
    public Piece getTurn() {
        return this.turn;
    }

    /**
     * Gets the current playing state of the game
     *
     * @return
     * Returns true if the game has been completed, else returns false
     */
    public boolean isGameOver() {
        return this.gameOver;
    }

    /**
     * Gets the Piece enum representing the winning player
     *
     * @return
     * Returns a Piece enum representing the winning player
     */
    public Piece getWinner() {
        return this.winner;
    }

    /**
     * Creates a copy of Board object
     * This method makes use of the utility constructor for the Board object
     * This method is mainly for the convenience of the AI
     *
     * @see Board(Board)
     *
     * @return
     * Returns an exact copy of the Board object
     */
    public Board createCopy() {
        return new Board(this);
    }

    /**
     * The String representation of the Board object
     * Slots are laid out on the board as follows:
     *
     * 0 | 1 | 2
     * ---------
     * 3 | 4 | 5
     * ---------
     * 6 | 7 | 8
     *
     * @return
     * Returns a String representation of the Board object
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int y = 0; y < 3; y++) {
            sb.append(" ");
            for (int x = 0; x < 3; x++) {
                if(this.board[x][y] == null) sb.append(" ");
                else sb.append(this.board[x][y].name());
                sb.append(" | ");
            }

            sb.deleteCharAt(sb.length()-2);
            if(y != 2) sb.append("\n--- --- ---\n");
        }

        return sb.toString();
    }
}