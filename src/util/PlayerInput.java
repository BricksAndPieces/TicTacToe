package util;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Interface that abstracts away unnecessary implemented methods
 * This interface has just one method to help improve code understandability
 */
public interface PlayerInput {

    /**
     * This method is called when a player clicks down onto the user interface
     * All the board logic and UI updating is done within the implementation
     *
     * @see Display#mouseClicked(MouseEvent)
     * @see game.TicTacToe#onPlayerInput(Point)
     *
     * @param p
     * The point where the user clicked down on the user interface
     * This point is converted for the pixel location clicked to a location
     * on a 3x3 grid so that it can be easily used on the Board
     */
    void onPlayerInput(Point p);
}