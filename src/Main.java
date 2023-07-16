import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYDataset;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {
    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String imagePath = selectedFile.getAbsolutePath();

            try {
                BufferedImage image = ImageIO.read(new File(imagePath));
                int width = image.getWidth();
                int height = image.getHeight();

                double[][] data = new double[2][height];

                // Iterate through each vertical pixel and calculate the sum of grayscale values of horizontal pixels
                for (int y = 0; y < height; y++) {
                    int graySum = 0;
                    for (int x = 0; x < width; x++) {
                        Color pixelColor = new Color(image.getRGB(x, y));
                        int grayScale = (pixelColor.getRed() + pixelColor.getGreen() + pixelColor.getBlue()) / 3;
                        graySum += grayScale;
                    }
                    data[0][y] = y + 1; // Vertical pixel count
                    data[1][y] = graySum; // Sum of grayscale values in the horizontal direction
                }

                DefaultXYDataset dataset = new DefaultXYDataset();
                dataset.addSeries("Grayscale Sums", data);

                JFreeChart chart = ChartFactory.createXYLineChart(
                        "Grayscale Sums",
                        "Vertical Pixel Count",
                        "Sum of Grayscale Values in the Horizontal Direction",
                        dataset
                );

                ChartFrame frame = new ChartFrame("Grayscale Analysis", chart);
                frame.pack();
                frame.setVisible(true);

                // Apply smoothing to the data
                int windowSize = 45; // Size of the window for smoothing

                double[][] smoothedData = smoothData(data, windowSize);

                DefaultXYDataset smoothedDataset = new DefaultXYDataset();
                smoothedDataset.addSeries("Smoothed Grayscale Sums", smoothedData);

                JFreeChart smoothedChart = ChartFactory.createXYLineChart(
                        "Smoothed Grayscale Sums",
                        "Vertical Pixel Count",
                        "Sum of Grayscale Values in the Horizontal Direction",
                        smoothedDataset
                );

                ChartFrame smoothedFrame = new ChartFrame("Smoothed Grayscale Analysis", smoothedChart);
                smoothedFrame.pack();
                smoothedFrame.setVisible(true);

                // Calculate the number of peaks in the smoothed data
                int peakCount = countPeaks(smoothedData);

                System.out.println("Number of Peaks: " + peakCount);

                // Show the smoothed data chart
                ChartFrame smoothedDataChartFrame = new ChartFrame("Smoothed Data Analysis", createChart(smoothedData));
                smoothedDataChartFrame.pack();
                smoothedDataChartFrame.setVisible(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static double[][] smoothData(double[][] data, int windowSize) {
        int height = data[0].length;
        int smoothedHeight = height - windowSize + 1;

        double[][] smoothedData = new double[2][smoothedHeight];

        for (int i = 0; i < smoothedHeight; i++) {
            int sum = 0;
            for (int j = i; j < i + windowSize; j++) {
                sum += data[1][j];
            }
            smoothedData[0][i] = data[0][i + windowSize / 2]; // Use the middle point of the window
            smoothedData[1][i] = sum / windowSize; // Average of the values in the window
        }

        return smoothedData;
    }

    private static int countPeaks(double[][] data) {
        int peakCount = 0;
        int height = data[0].length;

        for (int i = 1; i < height - 1; i++) {
            double prevValue = data[1][i - 1];
            double currentValue = data[1][i];
            double nextValue = data[1][i + 1];
            if (currentValue > prevValue && currentValue > nextValue) {
                peakCount++;
            }
        }

        return peakCount;
    }

    private static JFreeChart createChart(double[][] data) {
        DefaultXYDataset dataset = new DefaultXYDataset();
        dataset.addSeries("Smoothed Grayscale Sums", data);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Smoothed Data Analysis",
                "Vertical Pixel Count",
                "Sum of Grayscale Values in the Horizontal Direction",
                dataset
        );

        return chart;
    }
}
