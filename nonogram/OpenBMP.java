package nonogram;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class OpenBMP implements ActionListener {
    private final Nonogram nonogram;

    public OpenBMP(Nonogram nonogram) {
        this.nonogram = nonogram;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedImage image = ImageIO.read(selectedFile);
                if (image != null) {
                    nonogram.width = image.getWidth();
                    nonogram.height = image.getHeight();
                    nonogram.solution = new int[nonogram.height][nonogram.width];
                    List<Color> colorList = new ArrayList<>();

                    // Traverse through the image and collect unique colors
                    for (int i = 0; i < nonogram.height; i++) {
                        for (int j = 0; j < nonogram.width; j++) {
                            Color pixelColor = new Color(image.getRGB(j, i));
                            if (!pixelColor.equals(nonogram.initialColor) && !colorList.contains(pixelColor)) {
                                colorList.add(pixelColor);
                            }
                            nonogram.solution[i][j] = image.getRGB(j, i);
                        }
                    }

                    nonogram.colors = colorList.toArray(new Color[0]);
                    nonogram.currentColor = nonogram.colors[0]; // Set initial current color
                    nonogram.updateUI();
                } else {
                    JOptionPane.showMessageDialog(null, "Error loading image.");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error loading image.");
            }
        }
    }
}
