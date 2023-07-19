import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import javax.swing.JFrame;

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

                JFreeChart chart = createChart(dataset);

                JFrame frame = new JFrame("Grayscale Analysis");
                ChartPanel chartPanel = new ChartPanel(chart);
                frame.setContentPane(chartPanel);
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);

                // Apply smoothing to the data
                int windowSize = 45; // Size of the window for smoothing
                int[] windowShifts = {10, 20, 30}; // Array of window shift values

                for (int windowShift : windowShifts) {
                    double[][] smoothedData = scanMountains(data, windowSize);

                    DefaultXYDataset smoothedDataset = new DefaultXYDataset();
                    smoothedDataset.addSeries("Smoothed Grayscale Sums", smoothedData);

                    JFreeChart smoothedChart = createChart(smoothedDataset);

                    JFrame smoothedFrame = new JFrame("Smoothed Grayscale Analysis (" + windowShift + ")");
                    ChartPanel smoothedChartPanel = new ChartPanel(smoothedChart);
                    smoothedFrame.setContentPane(smoothedChartPanel);
                    smoothedFrame.pack();
                    smoothedFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    smoothedFrame.setVisible(true);

                    animateChart(smoothedChartPanel, smoothedData, windowShift);
                    animateOverlay(chartPanel, smoothedData, windowShift);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static JFreeChart createChart(DefaultXYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                null,
                "Vertical Pixel Count",
                "Sum of Grayscale Values in the Horizontal Direction",
                dataset
        );

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainGridlinePaint(Color.BLACK);

        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return chart;
    }

    private static double[][] scanMountains(double[][] data, int windowSize) {
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

    private static void animateChart(ChartPanel chartPanel, double[][] data, int windowShift) {
        Thread animationThread = new Thread(() -> {
            int height = data[0].length;
            int windowCount = height - windowShift * 2 + 1;

            for (int i = 0; i < windowCount; i++) {
                double[][] windowData = new double[2][windowShift];
                System.arraycopy(data[0], i, windowData[0], 0, windowShift);
                System.arraycopy(data[1], i, windowData[1], 0, windowShift);

                DefaultXYDataset windowDataset = new DefaultXYDataset();
                windowDataset.addSeries("Window Data", windowData);

                chartPanel.getChart().getXYPlot().setDataset(windowDataset);
                chartPanel.repaint();

                try {
                    Thread.sleep(100); // Adjust the animation speed here (in milliseconds)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        animationThread.start();
    }

    private static void animateOverlay(ChartPanel chartPanel, double[][] data, int windowShift) {
        Thread overlayThread = new Thread(() -> {
            int height = data[0].length;
            int windowCount = height - windowShift * 2 + 1;

            for (int i = 0; i < windowCount; i++) {
                double[][] overlayData = new double[2][windowShift];
                System.arraycopy(data[0], i + windowShift, overlayData[0], 0, windowShift);
                System.arraycopy(data[1], i + windowShift, overlayData[1], 0, windowShift);

                XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
                renderer.setSeriesPaint(0, Color.RED);

                chartPanel.getChart().getXYPlot().setRenderer(1, renderer);
                chartPanel.getChart().getXYPlot().setDataset(1, createDataset(overlayData));
                chartPanel.getChart().getXYPlot().mapDatasetToRangeAxis(1, 1);
                chartPanel.repaint();

                try {
                    Thread.sleep(100); // Adjust the animation speed here (in milliseconds)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        overlayThread.start();
    }

    private static DefaultXYDataset createDataset(double[][] data) {
        DefaultXYDataset dataset = new DefaultXYDataset();
        dataset.addSeries("Overlay Data", data);
        return dataset;
    }
}
