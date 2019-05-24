package game;

import ai.AI;
import util.Display;
import util.PlayerInput;
import util.AudioPlayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class provides all the game logic and the actual playing of the game
 * This class also contains the main method for running convenience
 */
public class TicTacToe implements PlayerInput {

    private final Board board;
    private final Display display;
    private final AudioPlayer audioPlayer;

    private final BufferedImage xImg;
    private final BufferedImage oImg;
    private final BufferedImage boardImg;

    private final ScheduledExecutorService service;
    private boolean singlePlayer;

    /**
     * Default constructor for the TicTacToe object
     * Initializes all needed objects and variables to play the game
     *
     * @param size
     * The size of the Display that should be created
     *
     * @throws Exception
     * Throws if there was an error accessing the image files
     * Throws if there was an error accessing the audio files
     */
    private TicTacToe(int size) throws Exception {
        this.xImg = ImageIO.read(new File("images/x.png"));
        this.oImg = ImageIO.read(new File("images/o.png"));
        this.boardImg = ImageIO.read(new File("images/board.png"));

        this.board = new Board();
        this.audioPlayer = new AudioPlayer("audio/move.wav");
        this.display = new Display("Tic-Tac-Toe", size, this.boardImg);
        this.service = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Begins the game of TicTacToe
     *
     * This will add the instance as a PlayerInput listener and also
     * display a popup to set the gamemode type (single and double)
     */
    private void run() {
        this.display.addListener(this);
        this.singlePlayer = this.display.popup("How would you like to play?", "Two Player", "Single Player") != 0;
    }

    /**
     * Closes all resources and ends the game
     */
    private void end() {
        this.service.shutdown();
        this.audioPlayer.close();
    }

    /**
     * The method that handles all the game logic
     * This method is fired every time the user clicks on the board
     *
     * @see PlayerInput#onPlayerInput(Point)
     * @see Display#mouseClicked(MouseEvent)
     *
     * @param p
     * The point where the user clicked down on the user interface
     * This point is converted from the pixel location clicked to
     * a location on a 3x3 grid
     */
    @Override
    public void onPlayerInput(Point p) {
        if((this.singlePlayer && this.board.getTurn() != Board.Piece.X) || !this.board.move(p.y * 3 + p.x))
            return;

        this.audioPlayer.play();
        this.display.updateImage(generateImage());
        if(this.board.isGameOver()) {
            onGameOver();
            return;
        }

        if(singlePlayer) {
            this.service.schedule(() -> {
                this.board.move(AI.getAlphaBetaMove(board, Board.Piece.O));
                this.audioPlayer.play();

                this.display.updateImage(generateImage());
                if(this.board.isGameOver())
                    onGameOver();
            }, 1000, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Resets the board with a delay of 3 seconds
     */
    private void onGameOver() {
        this.service.schedule(() -> {
            this.board.reset();
            this.display.updateImage(generateImage());
        }, 3, TimeUnit.SECONDS);
    }

    /**
     * Private helper method for the TicTacToe class
     * Generates an image of the board to be displayed
     *
     * @see TicTacToe#drawPieces(Graphics2D)
     * @see TicTacToe#drawWinner(Graphics2D, BufferedImage)
     *
     * @return
     * Returns a BufferedImage of the current state of the Board
     */
    private BufferedImage generateImage() {
        BufferedImage out = new BufferedImage(900, 900, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();

        g.drawImage(this.boardImg, 0, 0, 900, 900, null);
        drawPieces(g);

        if(this.board.isGameOver())
            drawWinner(g, out);

        g.dispose();
        return out;
    }

    /**
     * Private helper method for the TicTacToe class
     * Draws the Pieces onto the board
     *
     * @see TicTacToe#generateImage()
     *
     * @param g
     * The Graphics object to use when editing the image
     */
    private void drawPieces(Graphics2D g) {
        for(int x = 0; x < 3; x++) {
            for(int y = 0; y < 3; y++) {
                if(this.board.getBoardRaw()[x][y] != null) {
                    BufferedImage piece = this.board.getBoardRaw()[x][y] == Board.Piece.X ? xImg : oImg;
                    g.drawImage(piece, x*290+60, y*290+60, piece.getWidth(), piece.getHeight(), null);
                }
            }
        }
    }

    /**
     * Private helper method for the TicTacToe class
     * Draws the winner status onto the board
     *
     * @see TicTacToe#generateImage()
     *
     * @param g
     * The Graphics object to use when editing the image
     *
     * @param img
     * The BufferedImage to be edited
     */
    private void drawWinner(Graphics2D g, BufferedImage img) {
        Board.Piece winner = this.board.getWinner();
        if(winner != null) {
            Color c = Color.decode(winner == Board.Piece.X ? "#e25043" : "#1bbc9b");
            for(int x = 0; x < img.getWidth(); x++) {
                for(int y = 0; y < img.getHeight(); y++) {
                    if(new Color(img.getRGB(x,y)).getGreen() > 200)
                        img.setRGB(x,y,c.getRGB());
                }
            }
        }

        String msg = winner == null ? "Draw" : winner.name() + " Wins";
        g.setFont(new Font("helvetica", Font.BOLD, 60));

        FontMetrics metrics = g.getFontMetrics();
        g.drawString(msg, 450-metrics.stringWidth(msg)/2, 450+metrics.getAscent()-metrics.getHeight()/2);
    }

    /**
     * Main method of the project
     * Placed within the TicTacToe class for convenience
     *
     * @see TicTacToe(int)
     *
     * @param args
     * String array of arguments passed through command line
     *
     * @throws Exception
     * Throws if an error occurs when creating the TicTacToe object
     */
    public static void main(String[] args) throws Exception {
        TicTacToe ticTacToe = new TicTacToe(400);
        Runtime.getRuntime().addShutdownHook(new Thread(ticTacToe::end));
        ticTacToe.run();
    }
}