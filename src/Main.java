import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYDataset;

public class Main {
    public static void main(String[] args) {
        String imagePath = "karton.jpg"; // File path of the image you want to analyze
        int threshold = 208000; // Threshold value

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

            // Calculate the number of peaks
            int peakCount = 0;
            for (int y = 1; y < height - 1; y++) {
                double prevValue = data[1][y - 1];
                double currentValue = data[1][y];
                double nextValue = data[1][y + 1];
                if (currentValue > prevValue && currentValue > nextValue && currentValue > threshold) {
                    peakCount++;
                }
            }
            System.out.println("Number of Peaks above " + threshold + ": " + peakCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
