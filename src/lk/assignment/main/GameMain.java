/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.assignment.main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Thalisha-
 */
public class GameMain extends JPanel {

    private static final long serialVersionUID = 1L;
    static final String TITLE = "Snake Game";
    static final int ROWS = 40;
    static final int COLUMNS = 40;
    static final int CELL_SIZE = 15;
    static final int CANVAS_WIDTH = COLUMNS * CELL_SIZE;
    static final int CANVAS_HEIGHT = ROWS * CELL_SIZE;
    static final int UPDATE_PER_SEC = 3;
    static final long UPDATE_PERIOD_NSEC = 1000000000L / UPDATE_PER_SEC;

    enum GameState {
        INITIALIZED, PLAYING, PAUSED, GAMEOVER, DESTROYED
    }

    static GameState state;

    private Food food;
    private Snake snake;

    private GameCanvas pit;
    private ControlPanel control;
    private JLabel lblScore;
    int score = 0;

    public GameMain() {
        gameInit();
        setLayout(new BorderLayout());
        pit = new GameCanvas();
        pit.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        add(pit, BorderLayout.CENTER);

        control = new ControlPanel();
        add(control, BorderLayout.SOUTH);

        gameStart();
    }

    public void gameInit() {

        snake = new Snake();
        food = new Food();
        state = GameState.INITIALIZED;
    }

    public void gameShutdown() {

    }

    public void gameStart() {
        // Create a new thread
        Thread gameThread = new Thread() {

            public void run() {
                gameLoop();
            }
        };
        //Start the thread.start() calls run, which in turn calls gameLoop()
        gameThread.start();
    }

    // run the game loop here
    private void gameLoop() {

        if (state == GameState.INITIALIZED || state == GameState.GAMEOVER) {
            //Generate a new snake and a food item
            snake.regenerate();

            int x, y;
            do {
                food.regenerate();
                x = food.getX();
                y = food.getY();
            } while (snake.contains(x, y));

            state = GameState.PLAYING;

        }

        //Game loop
        long beginTime, timeTaken, timeLeft;
        while (state != GameState.GAMEOVER) {
            beginTime = System.nanoTime();
            if (state == GameState.PLAYING) {

                gameUpdate();
            }

            repaint();

            timeTaken = System.nanoTime() - beginTime;

            timeLeft = (UPDATE_PERIOD_NSEC - timeTaken) / 1000000;
            if (timeLeft < 10) {
                timeLeft = 10;
            }
            try {

                Thread.sleep(timeLeft);
            } catch (InterruptedException ex) {
            }

        }
    }

    public void gameUpdate() {
        snake.update();
        processCollision();
    }

    public void processCollision() {
        int headX = snake.getHeadX();
        int headY = snake.getHeadY();

        if (headX == food.getX() && headY == food.getY()) {

            SoundEffect.EAT.play();
            score = score + 1;
            lblScore.setText("Score: " + score);

            int x, y;
            do {
                food.regenerate();
                x = food.getX();
                y = food.getY();
            } while (snake.contains(x, y));
        } else {
            snake.shrink();
        }
        if (!pit.contains(headX, headY)) {
            state = GameState.GAMEOVER;
            SoundEffect.DIE.play();
            score = 0;
            lblScore.setText("Score: " + score);
            return;
        }

        if (snake.eatItself()) {
            state = GameState.GAMEOVER;

            SoundEffect.DIE.play();
            score = 0;
            lblScore.setText("Score: " + score);
            return;
        }
    }

    private void gameDraw(Graphics g) {
        //draw game objects
        snake.draw(g);
        food.draw(g);

        if (state == GameState.GAMEOVER) {
            g.setFont(new Font("Verdana", Font.BOLD, 30));
            g.setColor(Color.RED);
            g.drawString("GAME OVER!", 200, CANVAS_HEIGHT / 2);
        }

    }

    public void gameKeyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                snake.setDirection(Snake.Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                snake.setDirection(Snake.Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                snake.setDirection(Snake.Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                snake.setDirection(Snake.Direction.RIGHT);
                break;
        }
    }

    class ControlPanel extends JPanel {

        private static final long serialVersionUID = 1L;
        private JButton btnStartPause;
        private JButton btnStop;
        private JButton btnMute;

        private ImageIcon iconStart = new ImageIcon(getClass().getResource("/lk/assignment/images/start.png"), "START");
        private ImageIcon iconPause = new ImageIcon(getClass().getResource("/lk/assignment/images/pause.png"), "PAUSE");
        private ImageIcon iconStop = new ImageIcon(getClass().getResource("/lk/assignment/images/stop.png"), "STOP");
        private ImageIcon iconSound = new ImageIcon(getClass().getResource("/lk/assignment/images/sound.png"), "SOUND ON");
        private ImageIcon iconMuted = new ImageIcon(getClass().getResource("/lk/assignment/images/muted.png"), "MUTED");

        public ControlPanel() {
            this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

            btnStartPause = new JButton(iconPause);
            btnStartPause.setToolTipText("Pause");
            btnStartPause.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnStartPause.setEnabled(true);
            add(btnStartPause);

            btnStop = new JButton(iconStop);
            btnStop.setToolTipText("Stop");
            btnStop.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnStop.setEnabled(true);
            add(btnStop);

            btnMute = new JButton(iconMuted);
            btnMute.setToolTipText("Mute");
            btnMute.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnMute.setEnabled(true);
            add(btnMute);

            lblScore = new JLabel("Score: 0");
            add(lblScore);

            //handle click events on buttons
            btnStartPause.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (state) {
                        case INITIALIZED:
                        case GAMEOVER:
                            btnStartPause.setIcon(iconPause);
                            btnStartPause.setToolTipText("Pause");
                            gameStart();
                            //To play a specific sound
                            SoundEffect.CLICK.play();
                            score = 0;
                            lblScore.setText("Score: " + score);
                            break;
                        case PLAYING:
                            state = GameState.PAUSED;
                            btnStartPause.setIcon(iconStart);
                            btnStartPause.setToolTipText("Start");
                            //To play a specific sound
                            SoundEffect.CLICK.play();
                            break;
                        case PAUSED:
                            state = GameState.PLAYING;
                            btnStartPause.setIcon(iconPause);
                            btnStartPause.setToolTipText("Pause");
                            //To play a specific sound
                            SoundEffect.CLICK.play();
                            break;
                    }
                    btnStop.setEnabled(true);
                    pit.requestFocus();

                }
            });

            btnStop.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    state = GameState.GAMEOVER;
                    btnStartPause.setIcon(iconStart);
                    btnStartPause.setEnabled(true);
                    btnStop.setEnabled(false);
                    //To play a specific sound
                    SoundEffect.CLICK.play();

                }
            });

            btnMute.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (SoundEffect.volume == SoundEffect.Volume.MUTE) {
                        SoundEffect.volume = SoundEffect.Volume.LOW;
                        btnMute.setIcon(iconSound);
                        //To play a specific sound
                        SoundEffect.CLICK.play();
                        pit.requestFocus();
                    } else {
                        SoundEffect.volume = SoundEffect.Volume.MUTE;
                        btnMute.setIcon(iconMuted);
                        //To play a specific sound
                        SoundEffect.CLICK.play();
                        pit.requestFocus();
                    }

                }
            });

        }

        public void reset() {
            btnStartPause.setIcon(iconStart);
            btnStartPause.setEnabled(true);
            btnStop.setEnabled(false);
        }
    }

    class GameCanvas extends JPanel implements KeyListener {

        private static final long serialVersionUID = 1L;
        //constructor

        public GameCanvas() {
            setFocusable(true); //so that can receive key-events
            requestFocus();
            addKeyListener(this);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            setBackground(Color.decode("0x3F919E"));
            gameDraw(g);
        }

        //KeyEvent handlers
        @Override
        public void keyPressed(KeyEvent e) {
            gameKeyPressed(e.getKeyCode());

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        public boolean contains(int x, int y) {
            if ((x < 0) || (x >= ROWS)) {
                return false;
            }
            if ((y < 0) || (y >= COLUMNS)) {
                return false;
            }
            return true;
        }

    }

    // main function
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame(TITLE);
                frame.setContentPane(new GameMain());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();

                frame.setLocationRelativeTo(null);

                frame.setVisible(true);

            }
        });
    }

}
