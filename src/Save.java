import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Save {

    public static void saveDataToExcel(double[][] data, String filename) {
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

    public static double[] findXCoordinatesOfPeaksAboveThreshold(double[][] data, int threshold) {
        List<Double> peakCoordinates = new ArrayList<>();

        for (int i = 0; i < data.length; i++) {
            boolean aboveThreshold = data[i][0] >= threshold;

            for (int j = 1; j < data[i].length; j++) {
                boolean currentAbove = data[i][j] >= threshold;

                if (currentAbove != aboveThreshold) {
                    if (!currentAbove) {
                        // Tepe noktası bulundu
                        peakCoordinates.add((double) j);
                    }
                }
                aboveThreshold = currentAbove;
            }
        }

        return peakCoordinates.stream().mapToDouble(Double::doubleValue).toArray();
    }

    public static void drawRedBars(BufferedImage image, double[] xCoordinates) {
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.RED);

        int rectHeight = 20; // Çubukların yüksekliği
        int width = image.getWidth();

        for (int i = 0; i < xCoordinates.length; i++) {

            int centerX = width / 2;
            int centerY = (int)xCoordinates[i]; // Yatayda merkezlemek için

            int rectX = centerX - (width / 2);
            int rectY = centerY - (rectHeight / 2);

            g2d.fillRect(rectX, rectY, image.getWidth(), rectHeight);
        }

        g2d.dispose();
    }

    public static void saveImageWithRedBars(String filename, BufferedImage image, String imagePath, double[] xCoordinates) {
        drawRedBars(image, xCoordinates);

        String outputImagePath = imagePath.substring(0, imagePath.lastIndexOf('.')) + filename;
        File outputFile = new File(outputImagePath);


        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Image with Red Bars");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxWidth = screenSize.width - 50;
        int maxHeight = screenSize.height - 50;

        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();

        double scale = Math.min((double) maxWidth / imgWidth, (double) maxHeight / imgHeight);
        int newWidth = (int) (imgWidth * scale);
        int newHeight = (int) (imgHeight * scale);

        ImageIcon scaledIcon = new ImageIcon(image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH));
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(new JLabel(scaledIcon));
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);

        try {
            ImageIO.write(image, "jpg", outputFile);
            System.out.println("Kırmızı çubuklar yerleştirilmiş görüntü kaydedildi: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
