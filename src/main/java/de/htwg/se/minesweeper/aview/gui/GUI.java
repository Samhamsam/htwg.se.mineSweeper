package de.htwg.se.minesweeper.aview.gui;

import de.htwg.se.minesweeper.controller.IController;
import observer.Event;
import observer.IObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// IMPORTANT NOTE
// GUI doesn't work properly
// TODO Mark please FIXME

public class GUI extends JFrame implements ActionListener, IObserver, MouseListener {
    private static final long serialVersionUID = 1L;
    private JButton[][] gridAsJButtons;
    private IController controller;
    private GUISettings guiSettings;

    JFrame mainFrame;
    JMenuBar menuBar;
    JMenu menu;
    JMenu menuQuestion;
    JMenuItem newGame;
    JMenuItem quit, settingsmenu, help;

    public GUI(IController controller) {
        this.controller = controller;
        controller.addObserver(this);
        mainFrame = new JFrame("Minesweeper");
        initJFrame();

    }

    private void initJFrame() {

        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        menuQuestion = new JMenu("?");
        menuBar.add(menu);
        menuBar.add(menuQuestion);
        newGame = new JMenuItem("New Game");
        quit = new JMenuItem("Quit");
        help = new JMenuItem("Help");
        settingsmenu = new JMenuItem("Settings");
        menu.add(newGame);
        menu.add(settingsmenu);
        menu.add(quit);
        menuQuestion.add(help);

        newGame.addActionListener(this);
        quit.addActionListener(this);
        help.addActionListener(this);
        settingsmenu.addActionListener(this);

        mainFrame.setJMenuBar(menuBar);

        mainFrame.setLayout(new GridLayout(controller.getGrid().getNumberOfRows(), controller.getGrid().getNumberOfColumns()));
        buildGameField();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }


    private void buildGameField() {
        gridAsJButtons = new JButton[controller.getGrid().getNumberOfRows()][controller.getGrid().getNumberOfColumns()];

        for (int row = 0; row < controller.getGrid().getNumberOfRows(); row++) {
            for (int col = 0; col < controller.getGrid().getNumberOfColumns(); col++) {
                gridAsJButtons[col][row] = new JButton(controller.getGrid().getCellAt(row, col).toString());
                mainFrame.add(gridAsJButtons[col][row]);
                gridAsJButtons[col][row].setBackground(Color.GRAY);
                gridAsJButtons[col][row].addMouseListener(this);
                gridAsJButtons[col][row].setPreferredSize(new Dimension(50, 50));
            }
        }
    }

    private void updateGameField() {
        for (int row = 0; row < controller.getGrid().getNumberOfRows(); row++) {
            for (int col = 0; col < controller.getGrid().getNumberOfColumns(); col++) {
                setJButtonText(controller.getGrid().getCellAt(row, col).toString(), row, col);

                if ("0".equals(getJButtonText(row, col))) {
                    setJButtonColor(row, col, Color.GREEN);
                } else if ("x".equals(getJButtonText(row, col))) {
                    setJButtonColor(row, col, Color.GRAY);
                } else if ("b".equals(getJButtonText(row, col)) || "f".equals(getJButtonText(row, col))) {
                    setJButtonColor(row, col, Color.RED);
                } else {
                    setJButtonColor(row, col, Color.WHITE);
                }
            }
        }
    }

    private void setEnableButtons(boolean status) {
        for (int row = 0; row < controller.getGrid().getNumberOfRows(); row++) {
            for (int col = 0; col < controller.getGrid().getNumberOfColumns(); col++) {
                gridAsJButtons[col][row].setEnabled(status);
            }
        }
    }

    public void setJButtonColor(int i, int j, Color color) {
        gridAsJButtons[i][j].setBackground(color);
    }


    public void setJButtonText(String text, int i, int j) {
        gridAsJButtons[i][j].setText(text);
    }

    public String getJButtonText(int i, int j) {
        return gridAsJButtons[i][j].getText();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == newGame) {
            controller.startNewGame();

        } else if (e.getSource() == quit) {
            controller.quit();
        } else if (e.getSource() == help) {
            controller.getHelpText(); // TODO ?
        } else if (e.getSource() == settingsmenu) {
            showSettings();
        }


    }

    @Override
    public void update(Event e) {

        final IController.State state = controller.getState();

        if (state == IController.State.ERROR) {
            // TODO Mark?
        }

        switch (state) {

            case NEW_GAME:
                setEnableButtons(true);
                break;

            case GAME_LOST:
                messageDialog("You Lost!");
                setEnableButtons(false);
                break;

            case GAME_WON:
                messageDialog("You Won! " + controller.getElapsedTimeSeconds() + " Points!");
                break;

            case HELP_TEXT:
                messageDialog(controller.getHelpText());
                break;

            case CHANGE_SETTINGS_ACTIVATED:
                showSettings();
                buildGameField();
                updateGameField();
                break;

            case CHANGE_SETTINGS_SUCCESS:
                applySettings();
                buildGameField();
                updateGameField();
                break;
        }

    }

    private void messageDialog(String text) {
        JOptionPane.showMessageDialog(mainFrame, text);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Not needed
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {

            if (e.getSource() == newGame) {
                controller.startNewGame();
                setEnableButtons(true);

            } else if (e.getSource() == quit) {
                controller.quit();
            } else {
                revealCell(e);
            }
        }

        // set flag with right click
        else if (e.getButton() == MouseEvent.BUTTON3) {
            setFlag(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Not needed
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Not needed
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Not needed
    }

    private void setFlag(MouseEvent e) {
        for (int row = 0; row < controller.getGrid().getNumberOfRows(); row++) {
            for (int col = 0; col < controller.getGrid().getNumberOfColumns(); col++) {
                Object buttonText = e.getSource();
                if (buttonText.equals(gridAsJButtons[row][col])) {
                    controller.toggleFlag(row, col);
                }

            }
        }
    }

    private void revealCell(MouseEvent e) {
        Object sourceButton = e.getSource();
        for (int row = 0; row < controller.getGrid().getNumberOfRows(); row++) {
            for (int col = 0; col < controller.getGrid().getNumberOfColumns(); col++) {
                if (sourceButton.equals(gridAsJButtons[row][col])) {
                    controller.revealCell(row, col);
                }

            }
        }
    }

    private void showSettings() {
        guiSettings = new GUISettings(controller.getGrid().getNumberOfColumns(), controller.getGrid().getNumberOfMines(), controller, mainFrame);
        guiSettings.run();
    }

    private void applySettings() {
        mainFrame.getContentPane().removeAll();
        mainFrame.validate();
        mainFrame.repaint();
    }

}
