import java.awt.*;
import java.awt.image.BufferedImage;

public class Grayscale {

    public static double[][] getData(BufferedImage image) {
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

        return data;
    }
}
