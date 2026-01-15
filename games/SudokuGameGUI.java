import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class SudokuGameGUI extends JFrame {

    private JTextField[][] cells = new JTextField[9][9];
    private int[][] solution;
    private int[][] puzzle;

    private JComboBox<String> difficultyBox;

    public SudokuGameGUI() {
        setTitle("Sudoku Game");
        setSize(600, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        difficultyBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        JButton newGameBtn = new JButton("New Game");
        JButton saveBtn = new JButton("Save");
        JButton loadBtn = new JButton("Load");

        topPanel.add(new JLabel("Difficulty:"));
        topPanel.add(difficultyBox);
        topPanel.add(newGameBtn);
        topPanel.add(saveBtn);
        topPanel.add(loadBtn);

        add(topPanel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        Font font = new Font("Arial", Font.BOLD, 18);

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                JTextField tf = new JTextField();
                tf.setHorizontalAlignment(JTextField.CENTER);
                tf.setFont(font);

                int row = r, col = c;
                tf.addKeyListener(new KeyAdapter() {
                    public void keyReleased(KeyEvent e) {
                        String text = tf.getText();

                        if (!text.matches("[1-9]?")) {
                            tf.setText("");
                            return;
                        }

                        if (text.length() == 1) {
                            int num = Integer.parseInt(text);
                            if (num != solution[row][col]) {
                                tf.setBackground(Color.PINK); // wrong
                            } else {
                                tf.setBackground(Color.WHITE);
                                puzzle[row][col] = num;

                                if (isComplete()) {
                                    JOptionPane.showMessageDialog(null, "🎉 Sudoku Solved!");
                                }
                            }
                        }
                    }
                });

                cells[r][c] = tf;
                gridPanel.add(tf);
            }
        }

        add(gridPanel, BorderLayout.CENTER);

        newGameBtn.addActionListener(e -> startNewGame());
        saveBtn.addActionListener(e -> saveGame());
        loadBtn.addActionListener(e -> loadGame());

        startNewGame();
        setVisible(true);
    }

    private void startNewGame() {
        String diff = (String) difficultyBox.getSelectedItem();

        if (diff.equals("Easy")) {
            puzzle = easyPuzzle();
            solution = easySolution();
        } else if (diff.equals("Medium")) {
            puzzle = mediumPuzzle();
            solution = mediumSolution();
        } else {
            puzzle = hardPuzzle();
            solution = hardSolution();
        }

        loadPuzzle();
    }

    private void loadPuzzle() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (puzzle[r][c] != 0) {
                    cells[r][c].setText(String.valueOf(puzzle[r][c]));
                    cells[r][c].setEditable(false);
                    cells[r][c].setBackground(new Color(220, 220, 220));
                } else {
                    cells[r][c].setText("");
                    cells[r][c].setEditable(true);
                    cells[r][c].setBackground(Color.WHITE);
                }
            }
        }
    }

    private boolean isComplete() {
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                if (puzzle[r][c] == 0)
                    return false;
        return true;
    }

    private void saveGame() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("sudoku.save"))) {
            out.writeObject(puzzle);
            JOptionPane.showMessageDialog(this, "Game Saved!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Save Failed!");
        }
    }

    private void loadGame() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("sudoku.save"))) {
            puzzle = (int[][]) in.readObject();
            loadPuzzle();
            JOptionPane.showMessageDialog(this, "Game Loaded!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Load Failed!");
        }
    }

    // ====== PUZZLES ======

    private int[][] easyPuzzle() {
        return new int[][]{
            {5,3,0,0,7,0,0,0,0},
            {6,0,0,1,9,5,0,0,0},
            {0,9,8,0,0,0,0,6,0},
            {8,0,0,0,6,0,0,0,3},
            {4,0,0,8,0,3,0,0,1},
            {7,0,0,0,2,0,0,0,6},
            {0,6,0,0,0,0,2,8,0},
            {0,0,0,4,1,9,0,0,5},
            {0,0,0,0,8,0,0,7,9}
        };
    }

    private int[][] easySolution() {
        return new int[][]{
            {5,3,4,6,7,8,9,1,2},
            {6,7,2,1,9,5,3,4,8},
            {1,9,8,3,4,2,5,6,7},
            {8,5,9,7,6,1,4,2,3},
            {4,2,6,8,5,3,7,9,1},
            {7,1,3,9,2,4,8,5,6},
            {9,6,1,5,3,7,2,8,4},
            {2,8,7,4,1,9,6,3,5},
            {3,4,5,2,8,6,1,7,9}
        };
    }

    private int[][] mediumPuzzle() { return easyPuzzle(); }
    private int[][] mediumSolution() { return easySolution(); }

    private int[][] hardPuzzle() { return easyPuzzle(); }
    private int[][] hardSolution() { return easySolution(); }

    public static void main(String[] args) {
        new SudokuGameGUI();
    }
}
