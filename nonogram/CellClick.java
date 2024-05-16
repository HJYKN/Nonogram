package nonogram;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class CellClick implements ActionListener {
    private final Nonogram nonogram;
    private final int row;
    private final int col;

    public CellClick(Nonogram nonogram, int row, int col) {
        this.nonogram = nonogram;
        this.row = row;
        this.col = col;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = nonogram.buttons[row][col];
        button.setBackground(nonogram.currentColor);
    }
}
