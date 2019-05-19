package ai;

import game.Board;

/**
 * Static class for the AlphaBeta algorithm
 * Provides all logic needed for a functioning AI
 */
public class AlphaBeta {

    private static final int SLOT = 0;
    private static final int SCORE = 1;

    /**
     * Gets the best move to be played next
     * Searches 2,000 combinations for the best move
     *
     * @param board
     * The Board to check for the best move
     *
     * @param player
     * The Piece enum that the AI should associate with
     *
     * @return
     * Returns the best slot to be played
     */
    protected static int getMove(Board board, Board.Piece player) {
        return alphabeta(board, player, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE)[SLOT];
    }

    /**
     * Gets the best move to be played next
     * Searches 2,000 combinations for the best move
     *
     * @param board
     * The Board to check for the best move
     *
     * @param player
     * The Piece enum that the AI should associate with
     *
     * @param depth
     * How far ahead the AI should look before picking a slot
     *
     * @return
     * Returns the best slot to be played
     */
    protected static int getMove(Board board, Board.Piece player, int depth) {
        return alphabeta(board, player, Integer.MIN_VALUE, Integer.MAX_VALUE, depth)[SLOT];
    }

    /**
     * The actual algorithm that calculates the best slot to play
     * Heavily uses recursion to trace all possible plays
     * Recommended to learn what alpha-beta pruning is before trying
     * to understand the method
     *
     * @param board
     * The Board to check for the best move
     *
     * @param player
     * The Piece enum that the AI should associate with
     *
     * @param alpha
     * The current best score for the Board for the player that the
     * AI is trying to maximise the score for (usually itself)
     *
     * @param beta
     * The current best score for the Board for the player that the
     * AI is trying to minimize the score for (usually the opponent)
     *
     * @param depth
     * How far ahead the AI should look before picking a slot
     *
     * @return
     * Returns an int array of size two where the int stored at index 0 is
     * the best slot to play and the int stored at index 1 is the best
     * scoring board found out of all combinations
     */
    private static int[] alphabeta(Board board, Board.Piece player, int alpha, int beta, int depth) {
        if(depth-- < 0 || board.isGameOver())
            return new int[]{-1, score(board, player)};

        int bestSlot = -1;
        for(final int slot : board.getAvailableSlots()) {
            final Board copy = board.createCopy();
            copy.move(slot);

            final int score = alphabeta(copy, player, alpha, beta, depth)[SCORE];
            if(board.getTurn() == player && score > alpha) {
                alpha = score;
                bestSlot = slot;
            }else if(board.getTurn() != player && score < beta) {
                beta = score;
                bestSlot = slot;
            }

            if(alpha >= beta)
                break;
        }

        return new int[]{bestSlot, board.getTurn() == player ? alpha : beta};
    }

    /**
     * Method to calculate the "score" of a board
     * Scoring is only based on win or loss
     *
     * @param board
     * The Board to check the "score" of
     *
     * @param player
     * The Piece enum that the AI should associate with
     *
     * @return
     * Returns 1 if the player wins and -1 for a loss
     * Returns 0 if there was no winner or game is not finished
     */
    private static int score(Board board, Board.Piece player) {
        if(board.isGameOver())
            if(board.getWinner() == player) return 1;
            else if(board.getWinner() != null) return -1;

        return 0;
    }
}