import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Smooth {

    // Using "Move Average" Method
    public static double[][] smoothData(double[][] data) {
        int windowSize = 45;
        int height = data[0].length;
        int smoothedHeight = height - windowSize + 1;

        double[][] smoothedData = new double[2][smoothedHeight];

        for (int i = 0; i < smoothedHeight; i++) {
            int sum = 0;
            for (int j = i; j < i + windowSize; j++) {
                sum += data[1][j];
            }
            smoothedData[0][i] = data[0][i + windowSize / 2]; // Use the middle point of the window
            smoothedData[1][i] = (double) sum / windowSize; // Average of the values in the window
        }

        return smoothedData;
    }


}
