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
        String imagePath = "Karton.jpg"; // Analiz etmek istediğiniz resmin dosya yolu

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
