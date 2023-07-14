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
        String imagePath = "karton.jpg"; // Analiz etmek istediğiniz resmin dosya yolu
        int threshold = 207000; // Eşik değeri

        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            int width = image.getWidth();
            int height = image.getHeight();

            double[][] data = new double[2][height];

            // Her bir dikey pikseli dolaşarak yataydaki piksellerin gri tonlarını topla ve veri setine ekle
            for (int y = 0; y < height; y++) {
                int graySum = 0;
                for (int x = 0; x < width; x++) {
                    Color pixelColor = new Color(image.getRGB(x, y));
                    int grayScale = (pixelColor.getRed() + pixelColor.getGreen() + pixelColor.getBlue()) / 3;
                    graySum += grayScale;
                }
                data[0][y] = y + 1; // Dikey piksel sayısı
                data[1][y] = graySum; // Yataydaki gri ton toplamı
            }

            DefaultXYDataset dataset = new DefaultXYDataset();
            dataset.addSeries("Gri Ton Toplamları", data);

            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Gri Ton Toplamları",
                    "Dikey Piksel Sayısı",
                    "Yataydaki Gri Ton Toplamı",
                    dataset
            );

            ChartFrame frame = new ChartFrame("Gri Ton Analizi", chart);
            frame.pack();
            frame.setVisible(true);

            // Peak değerlerini hesapla
            int peakCount = 0;
            for (int y = 1; y < height - 1; y++) {
                double prevValue = data[1][y - 1];
                double currentValue = data[1][y];
                double nextValue = data[1][y + 1];
                if (currentValue > prevValue && currentValue > nextValue && currentValue > threshold) {
                    peakCount++;
                }
            }
            System.out.println(threshold + " Üzerindeki Peak Noktalarının Sayısı: " + peakCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
