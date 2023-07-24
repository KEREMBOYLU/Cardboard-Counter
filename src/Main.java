import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYDataset;

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

                findPeaks(smoothedData);

                //saveDataToExcel(smoothedData, "smoothed_data.csv");

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
            smoothedData[1][i] = (double) sum / windowSize; // Average of the values in the window
        }

        return smoothedData;
    }

    private static void saveDataToExcel(double[][] data, String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write("x,y\n");

            for (int i = 0; i < data[0].length; i++) {
                writer.write(data[0][i] + "," + data[1][i] + "\n");
            }

            writer.close();
            System.out.println("Data saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static double[][] findPeaks(double[][] data) {
        int peakCount = 0;
        int length = data[0].length;

        // To count the peaks, we'll consider a data point as a peak if it is greater
        // than both of its neighboring data points.
        for (int i = 1; i < length - 1; i++) {
            if (data[1][i] > data[1][i - 1] && data[1][i] > data[1][i + 1]) {
                peakCount++;
            }
        }

        System.out.println("Number of peaks in the smoothed data: " + peakCount);

        // We'll return an array that includes the peak count as the first row,
        // and the smoothed data as the second row.
        double[][] result = new double[2][length];
        result[0][0] = peakCount;
        System.arraycopy(data[1], 0, result[1], 0, length);

        return result;
    }
}
