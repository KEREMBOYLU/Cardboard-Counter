import java.awt.*;
import java.awt.image.BufferedImage;

public class OTSU {

    public static BufferedImage applyOtsuThreshold(BufferedImage grayscaleImage) {
        // Otsu eşikleme işlemi burada yapılır
        int width = grayscaleImage.getWidth();
        int height = grayscaleImage.getHeight();
        int totalPixels = width * height;

        int[] histogram = new int[256];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = grayscaleImage.getRGB(x, y) & 0xFF;
                histogram[pixel]++;
            }
        }

        double[] probabilities = new double[256];
        for (int i = 0; i < 256; i++) {
            probabilities[i] = (double) histogram[i] / totalPixels;
        }

        double maxVariance = -1;
        int threshold = 0;

        for (int t = 0; t < 256; t++) {
            double wBackground = 0;
            double wForeground = 0;
            double sumBackground = 0;
            double sumForeground = 0;

            for (int i = 0; i < 256; i++) {
                if (i < t) {
                    wBackground += probabilities[i];
                    sumBackground += i * probabilities[i];
                } else {
                    wForeground += probabilities[i];
                    sumForeground += i * probabilities[i];
                }
            }

            double meanBackground = sumBackground / wBackground;
            double meanForeground = sumForeground / wForeground;

            double variance = wBackground * wForeground * Math.pow(meanBackground - meanForeground, 2);
            if (variance > maxVariance) {
                maxVariance = variance;
                threshold = t;
            }
        }

        double coefficient = 1.1;
        int adjustedThreshold = (int) (threshold * coefficient);

        // Eğer eşik değeri 255'i aşarsa, 255 olarak ayarlayalım.
        adjustedThreshold = Math.min(adjustedThreshold, 255);

        BufferedImage thresholdedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = grayscaleImage.getRGB(x, y) & 0xFF;
                if (pixel < adjustedThreshold) {
                    thresholdedImage.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    thresholdedImage.setRGB(x, y, Color.WHITE.getRGB());
                }
            }
        }

        return thresholdedImage;
    }


}
