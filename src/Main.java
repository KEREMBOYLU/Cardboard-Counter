import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Görüntüyü yükle
            File file = new File("Karton.jpg");
            BufferedImage image = ImageIO.read(file);

            // 2. Genişlik ve yüksekliği al
            int width = image.getWidth();
            int height = image.getHeight();

            System.out.println(width +"\n");
            System.out.println(width +"\n");

            // 3. Histogramı hesapla
            HistogramDataset dataset = new HistogramDataset();
            double[] values = new double[width * height];
            int index = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x,y);
                    int gray = (rgb >> 16) & 0xFF; // Gri ton değeri
                    values[index] = gray;
                    index++;
                }
            }
            dataset.addSeries("Histogram", values, 256);

            // 4. Histogramı grafik olarak oluştur ve görüntüle
            JFreeChart chart = ChartFactory.createHistogram("Histogram", "Gri Ton Değeri", "Piksel Sayısı", dataset);
            JFrame frame = new JFrame("Histogram");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new ChartPanel(chart));
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
