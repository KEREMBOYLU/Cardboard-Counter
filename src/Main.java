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
        String imagePath = "Karton2.jpg"; // Analiz etmek istediğiniz resmin dosya yolu

        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            int width = image.getWidth();
            int height = image.getHeight();

            double[][] data = new double[2][width];

            // Her bir yatay pikseli dolaşarak dikeydeki piksellerin gri tonlarını topla ve veri setine ekle
            for (int x = 0; x < width; x++) {
                int graySum = 0;
                for (int y = 0; y < height; y++) {
                    Color pixelColor = new Color(image.getRGB(x, y));
                    int grayScale = (pixelColor.getRed() + pixelColor.getGreen() + pixelColor.getBlue()) / 3;
                    graySum += grayScale;
                }
                data[0][x] = x + 1; // Yatay piksel sayısı
                data[1][x] = graySum; // Dikeydeki gri ton toplamı
            }

            DefaultXYDataset dataset = new DefaultXYDataset();
            dataset.addSeries("Gri Ton Toplamları", data);

            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Gri Ton Toplamları",
                    "Yatay Piksel Sayısı",
                    "Dikeydeki Gri Ton Toplamı",
                    dataset
            );

            ChartFrame frame = new ChartFrame("Gri Ton Analizi", chart);
            frame.pack();
            frame.setVisible(true);

            // Tepe noktalarını hesapla
            int peakCount = 0;
            for (int i = 1; i < width - 1; i++) {
                double prevValue = data[1][i - 1];
                double currentValue = data[1][i];
                double nextValue = data[1][i + 1];
                if (currentValue > prevValue && currentValue > nextValue) {
                    peakCount++;
                }
            }
            System.out.println("Tepe Noktalarının Sayısı: " + peakCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
