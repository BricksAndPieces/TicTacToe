package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that abstracts away annoying swing elements
 * This class provides easy to use methods to create a user interface
 */
public class Display extends JFrame implements MouseListener {

    private final int size;
    private final JLabel label;
    private final List<PlayerInput> listeners;

    /**
     * Default constructor for the Display object
     * This will open up a user interface when called
     *
     * @param title
     * The title of the user interface
     *
     * @param size
     * The size of the user interface
     *
     * @param img
     * The BufferedImage to be displayed in the user interface
     */
    public Display(final String title, final int size, final Image img){
        super(title);
        this.size = size;
        this.label = new JLabel();
        this.listeners = new ArrayList<>();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(size, size);
        setResizable(false);

        this.label.setSize(size,size);
        updateImage(img);
        add(this.label);

        setLocationRelativeTo(null);
        addMouseListener(this);

        pack();
        setVisible(true);
    }

    /**
     * Adds a new Listener to send events too
     * These events will fire on player input
     *
     * @see Display#mouseClicked(MouseEvent)
     * @see PlayerInput#onPlayerInput(Point)
     *
     * @param input
     * The object instance implementing PlayerInput
     */
    public void addListener(PlayerInput input) {
        this.listeners.add(input);
    }

    /**
     * Updates the image displayed on the Display object
     * Image will be automatically resized to fit the Display
     *
     * @param img
     * The image to display on the Display object
     */
    public void updateImage(final Image img) {
        this.label.setIcon(new ImageIcon(img.getScaledInstance(size, size, Image.SCALE_SMOOTH)));
    }

    /**
     * Displays a popup with the current Display object as the parent
     * Used primarily to create an easy way to get necessary user input
     *
     * @param msg
     * The message to be displayed on the popup
     *
     * @param options
     * An array of options to be displayed as buttons
     *
     * @return
     * Returns the index of the option picked
     * Returns -1 if none are picked
     */
    public int popup(String msg, String... options) {
        return JOptionPane.showOptionDialog(this, msg, this.getTitle(), JOptionPane.DEFAULT_OPTION,
                                            JOptionPane.PLAIN_MESSAGE, null, options, null);
    }

    /**
     * The raw event that is fired when a user clicks down with a mouse
     * This method is abstracted away into understandable methods
     *
     * @see PlayerInput#onPlayerInput(Point)
     *
     * @param e
     * The MouseEvent that provides info on the current mouse state
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = (int)(((double) e.getX()) / getWidth() * 3);
        int y = (int)(((double) e.getY()) / getHeight() * 3);

        for(PlayerInput listener : this.listeners)
            listener.onPlayerInput(new Point(x,y));
    }

    /**
     * Unnecessary implemented method
     */
    @Override
    public void mousePressed(MouseEvent e) { }

    /**
     * Unnecessary implemented method
     */
    @Override
    public void mouseReleased(MouseEvent e) { }

    /**
     * Unnecessary implemented method
     */
    @Override
    public void mouseEntered(MouseEvent e) { }

    /**
     * Unnecessary implemented method
     */
    @Override
    public void mouseExited(MouseEvent e) { }
}