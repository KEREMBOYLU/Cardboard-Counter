import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

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
                // Read image
                BufferedImage image = ImageIO.read(new File(imagePath));
                double[][] data = Grayscale.getData(image);

                // Show Chart
                DefaultXYDataset dataset = new DefaultXYDataset();
                dataset.addSeries("Grayscale Sums", data);

                Map<String, String> context = new HashMap<>();
                context.put("title","Grayscale Sums");
                context.put("xAxisLabel","Vertical Pixel Count");
                context.put("yAxisLabel","Sum of Grayscale Values in the Horizontal Direction");

                Chart.Create(context.get("title"),context.get("xAxisLabel"),context.get("yAxisLabel"),dataset);


                // Apply smoothing to the data and show smoothed chart
                double[][] smoothedData = Smooth.smoothData(data);
                DefaultXYDataset smoothedDataset = new DefaultXYDataset();
                smoothedDataset.addSeries("Smoothed Grayscale Sums", smoothedData);

                Map<String, String> smoothed_context = new HashMap<>();
                smoothed_context.put("title","Smoothed Grayscale Sums");
                smoothed_context.put("xAxisLabel","Vertical Pixel Count");
                smoothed_context.put("yAxisLabel","Sum of Grayscale Values in the Horizontal Direction");

                Chart.Create(smoothed_context.get("title"),smoothed_context.get("xAxisLabel"),smoothed_context.get("yAxisLabel"),smoothedDataset);


                // Read OTSU_threshold.png and Get Grayscale Data
                BufferedImage OTSU_threshold = OTSU.applyOtsuThreshold(image);

                /* ImageIO.write(OTSU_threshold, "png", new File("OTSU_threshold.png"));
                System.out.println("OTSU_threshold.png saved."); */

                double[][] OTSU_grayscale_data = Grayscale.getData(OTSU_threshold);

                // Show OTSU Grayscale Data Chart
                DefaultXYDataset OTSU_dataset = new DefaultXYDataset();
                OTSU_dataset.addSeries("OTSU Grayscale Sums", OTSU_grayscale_data);

                Map<String, String> OTSU_context = new HashMap<>();
                OTSU_context.put("title","OTSU Grayscale Sums");
                OTSU_context.put("xAxisLabel","Vertical Pixel Count");
                OTSU_context.put("yAxisLabel","Sum of Grayscale Values in the Horizontal Direction");

                Chart.Create(OTSU_context.get("title"),OTSU_context.get("xAxisLabel"),OTSU_context.get("yAxisLabel"),OTSU_dataset);


                // Apply smoothing to the OTSU Grayscale Data and show smoothed chart
                double[][] OTSU_smoothedData = Smooth.smoothData(OTSU_grayscale_data);
                DefaultXYDataset OTSU_smoothedDataset = new DefaultXYDataset();
                OTSU_smoothedDataset.addSeries("Smoothed OTSU Grayscale Sums", OTSU_smoothedData);

                Map<String, String> OTSU_smoothed_context = new HashMap<>();
                OTSU_smoothed_context.put("title","Smoothed OTSU Grayscale Sums");
                OTSU_smoothed_context.put("xAxisLabel","Vertical Pixel Count");
                OTSU_smoothed_context.put("yAxisLabel","Sum of Grayscale Values in the Horizontal Direction");

                Chart.Create(OTSU_smoothed_context.get("title"),OTSU_smoothed_context.get("xAxisLabel"),OTSU_smoothed_context.get("yAxisLabel"),OTSU_smoothedDataset);

                // Farklı eşik değerleri belirleyin (Örnek olarak 100 ve 150 seçelim)
                int threshold = 56000;
                int threshold_OTSU = 180000;

                // Smoothed Grayscale Sums veri kümesi için eşik değerlerini kesen çizgilerin sayısını bulun
                int smoothedGrayscaleIntersections = Count.countThresholdIntersections(smoothedData, threshold);

                // Smoothed OTSU Grayscale Sums veri kümesi için eşik değerlerini kesen çizgilerin sayısını bulun
                int smoothedOTSUGrayscaleIntersections = Count.countThresholdIntersections(OTSU_smoothedData, threshold_OTSU);


                // Sonuçları yazdırın
                Count.countThresholdLines(smoothedGrayscaleIntersections,"Smoothed Grayscale");
                Count.countThresholdLines(smoothedOTSUGrayscaleIntersections,"Smoothed OTSU Grayscale");

                // Veri kümesinde threshold değerimizin üzerinde kalan tepe noktalarını heights dizisine saklayın
                double[] heights1 = Save.findPeaksAboveThreshold(smoothedData, threshold);
                double[] heights2 = Save.findPeaksAboveThreshold(OTSU_smoothedData, threshold_OTSU);


                // heights dizilerini yazdırın
                System.out.println("Heights: " + java.util.Arrays.toString(heights1));
                System.out.println("Heights otsu: " + java.util.Arrays.toString(heights2));


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }




}
