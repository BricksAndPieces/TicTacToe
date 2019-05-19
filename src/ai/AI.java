package ai;

import game.Board;

/**
 * Static class that acts as a wrapper for all AI algorithms
 * This includes MiniMax, AlphaBeta pruning, and Random moves
 */
public class AI {

    /**
     * Wrapper method for the AlphaBeta pruning algorithm
     *
     * @see AlphaBeta#getMove(Board, Board.Piece)
     *
     * @param board
     * The Board object to find the next move for
     *
     * @param player
     * The Piece enum that the AI should associate with
     *
     * @return
     * Returns the slot number that the AI wants to play
     */
    public static int getAlphaBetaMove(Board board, Board.Piece player) {
        return AlphaBeta.getMove(board, player);
    }

    /**
     * Wrapper method for the AlphaBeta pruning algorithm
     *
     * @see AlphaBeta#getMove(Board, Board.Piece, int)
     *
     * @param board
     * The Board object to find the next move for
     *
     * @param player
     * The Piece enum that the AI should associate with
     *
     * @param depth
     * How far ahead the AI should look for moves
     *
     * @return
     * Returns the slot number that the AI wants to play
     */
    public static int getAlphaBetaMove(Board board, Board.Piece player, int depth) {
        return AlphaBeta.getMove(board, player, depth);
    }

    /**
     * Wrapper method for the MiniMax pruning algorithm
     *
     * @see MiniMax#getMove(Board, Board.Piece)
     *
     * @param board
     * The Board object to find the next move for
     *
     * @param player
     * The Piece enum that the AI should associate with
     *
     * @return
     * Returns the slot number that the AI wants to play
     */
    public static int getMiniMaxMove(Board board, Board.Piece player) {
        return MiniMax.getMove(board, player);
    }

    /**
     * Wrapper method for the MiniMax pruning algorithm
     *
     * @see MiniMax#getMove(Board, Board.Piece, int)
     *
     * @param board
     * The Board object to find the next move for
     *
     * @param player
     * The Piece enum that the AI should associate with
     *
     * @param depth
     * How far ahead the AI should look for moves
     *
     * @return
     * Returns the slot number that the AI wants to play
     */
    public static int getMiniMaxMove(Board board, Board.Piece player, int depth) {
        return MiniMax.getMove(board, player, depth);
    }

    /**
     * Gets a random slot from the available slots
     * This is not an AI, just a convenience method
     *
     * @param board
     * The Board object to find the next move for
     *
     * @return
     * Returns the random slot number that the AI wants to play
     */
    public static int getRandomMove(Board board) {
        return (int)board.getAvailableSlots().toArray()[(int)(Math.random() * board.getAvailableSlots().size())];
    }
}