package nonogram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

public class Nonogram extends JFrame {
    protected int[][] solution;
    protected JButton[][] buttons;
    protected JPanel[][] buttonPanels;
    protected JLabel[] rowLabels;
    protected JLabel[] colLabels;
    protected int width;
    protected int height;
    protected final int buttonSize = 45; // Size of the puzzle buttons
    protected Color[] colors; // Array to store unique colors from the image
    protected Color currentColor; // Current selected color
    protected Color initialColor = Color.YELLOW; // Default unknown color
    protected JFrame colorSelectorFrame; // Color selector window
    protected JButton checkButton; // Check button

    public Nonogram() {
        initializeUI();
    }

    // Initializes the UI components
    private void initializeUI() {
        //Title and close button
        setTitle("Nonogram Puzzle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //set the base for all panel
        JPanel base = new JPanel(new BorderLayout());
        setContentPane(base);
        //set puzzle panel
        JPanel puzzle = new JPanel();
        base.add(puzzle, BorderLayout.CENTER);

        //set row lables panel initial 15,1
        JPanel rows = new JPanel(new GridLayout(15, 1));
        base.add(rows, BorderLayout.WEST);
        //set column lables panel initial 1 15 and give extra space to show
        JPanel colsSpace = new JPanel(new BorderLayout());
        base.add(colsSpace, BorderLayout.NORTH);

        JPanel cols = new JPanel(new GridLayout(1, 15));
        colsSpace.add(Box.createRigidArea(new Dimension(buttonSize + 100, buttonSize)), BorderLayout.WEST); // Add extra space
        colsSpace.add(cols, BorderLayout.CENTER);
        //set the menu bar to open bmp
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open BMP");
        openMenuItem.addActionListener(new OpenBMP(this));
        fileMenu.add(openMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        //set check button
        checkButton = new JButton("CHECK!");
        checkButton.addActionListener(new CheckButtonListener(this));
        base.add(checkButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Updates the UI components based on the loaded image
    public void updateUI() {
        JPanel puzzle = (JPanel) getContentPane().getComponent(0);
        puzzle.removeAll();

        JPanel rows = (JPanel) getContentPane().getComponent(1);
        rows.removeAll();

        JPanel colsSpace = (JPanel) getContentPane().getComponent(2);
        JPanel cols = (JPanel) colsSpace.getComponent(1);
        cols.removeAll();

        puzzle.setLayout(new GridLayout(height, width));
        buttons = new JButton[height][width];
        buttonPanels = new JPanel[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                JPanel panel = new JPanel(new BorderLayout());
                JButton button = new JButton("");
                button.setBackground(initialColor);
                button.setOpaque(true);
                button.setBorderPainted(false);
                button.setPreferredSize(new Dimension(buttonSize, buttonSize));
                button.addActionListener(new CellClick(this, i, j));
                panel.add(button, BorderLayout.CENTER);
                puzzle.add(panel);
                buttons[i][j] = button;
                buttonPanels[i][j] = panel;
            }
        }

        rows.setLayout(new GridLayout(height, 1));
        rowLabels = new JLabel[height];
        for (int i = 0; i < height; i++) {
            JLabel label = new JLabel("", SwingConstants.RIGHT);
            label.setPreferredSize(new Dimension(buttonSize + 100, buttonSize)); // Adjust label size
            rows.add(label);
            rowLabels[i] = label;
        }

        cols.setLayout(new GridLayout(1, width));
        colLabels = new JLabel[width];
        for (int i = 0; i < width; i++) {
            JLabel label = new JLabel("", SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(buttonSize, buttonSize + 100)); // Adjust label size
            cols.add(label);
            colLabels[i] = label;
        }

        updateLabels();
        colorSelector();
        revalidate();
        repaint();
    }

    // Updates the row and column labels with the Nonogram clues
    private void updateLabels() {
        // Update row labels
        for (int i = 0; i < height; i++) {
            StringBuilder label = new StringBuilder();
            int count = 0;
            Color currentColor = null;
            for (int j = 0; j < width; j++) {
                Color pixelColor = new Color(solution[i][j]);
                if (currentColor == null || !pixelColor.equals(currentColor)) {
                    if (currentColor != null && !currentColor.equals(Color.WHITE)) {
                        label.append("</font><font color=\"")
                                .append(getColorHexString(currentColor))
                                .append("\">").append(count).append(" ");
                    }
                    count = 1;
                    currentColor = pixelColor;
                } else {
                    count++;
                }
            }
            if (currentColor != null && !currentColor.equals(Color.WHITE)) {
                label.append("</font><font color=\"")
                        .append(getColorHexString(currentColor))
                        .append("\">").append(count).append(" ");
            }
            if (label.length() == 0) {
                label.append("0");
            }
            rowLabels[i].setText("<html><font color=\"black\">" + label.toString() + "</font></html>");
        }

        // Update column labels
        for (int j = 0; j < width; j++) {
            StringBuilder label = new StringBuilder();
            int count = 0;
            Color currentColor = null;
            for (int i = 0; i < height; i++) {
                Color pixelColor = new Color(solution[i][j]);
                if (currentColor == null || !pixelColor.equals(currentColor)) {
                    if (currentColor != null && !currentColor.equals(Color.WHITE)) {
                        label.append("</font><font color=\"")
                                .append(getColorHexString(currentColor))
                                .append("\">").append(count).append("<br>");
                    }
                    count = 1;
                    currentColor = pixelColor;
                } else {
                    count++;
                }
            }
            if (currentColor != null && !currentColor.equals(Color.WHITE)) {
                label.append("</font><font color=\"")
                        .append(getColorHexString(currentColor))
                        .append("\">").append(count).append("<br>");
            }
            if (label.length() == 0) {
                label.append("0");
            }
            // Add leading spaces to align numbers
            String labelText = label.toString().replace("<br>", "<br> ");
            colLabels[j].setText("<html><font color=\"black\">" + labelText + "</font></html>");
        }
    }

    // Converts a Color object to a hex string
    private String getColorHexString(Color color) {
        return String.format("#%06X", (0xFFFFFF & color.getRGB()));
    }

    // Creates the color selector window
    public void colorSelector() {
        if (colorSelectorFrame != null) {
            colorSelectorFrame.dispose(); // Dispose of the old window if it exists
        }

        colorSelectorFrame = new JFrame("Color Selector");
        colorSelectorFrame.setLayout(new GridLayout(colors.length, 1));
        for (Color color : colors) {
            JButton colorButton = new JButton();
            colorButton.setBackground(color);
            colorButton.setOpaque(true);
            colorButton.setBorderPainted(false);
            colorButton.setPreferredSize(new Dimension(50, 50));
            colorButton.addActionListener(e -> currentColor = color);
            colorSelectorFrame.add(colorButton);
        }
        colorSelectorFrame.pack();
        colorSelectorFrame.setLocationRelativeTo(null);
        colorSelectorFrame.setVisible(true);
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Nonogram::new);
    }
}
