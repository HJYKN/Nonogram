package nonogram;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheckButtonListener implements ActionListener {
    private final Nonogram nonogram;

    public CheckButtonListener(Nonogram nonogram) {
        this.nonogram = nonogram;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Highlight buttons that don't match the solution
        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < nonogram.height; i++) {
                for (int j = 0; j < nonogram.width; j++) {
                    Color currentColor = nonogram.buttons[i][j].getBackground();
                    Color pixelColor = new Color(nonogram.solution[i][j]);
                    if (!currentColor.equals(pixelColor)) {
                        nonogram.buttons[i][j].setBackground(Color.RED);
                    }
                    if (currentColor.equals(nonogram.initialColor)) {
                        nonogram.buttons[i][j].setBackground(Color.WHITE);
                    }
                }
            }
        }
    }
}
