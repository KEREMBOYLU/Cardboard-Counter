import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main {
    public static void main(String[] args) {
        String imagePath = "Kartonkare.jpg"; // Analiz etmek istediğiniz resmin dosya yolu

        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            int width = image.getWidth();
            int height = image.getHeight();

            // Her bir yatay pikseli dolaşarak dikeydeki piksellerin gri tonlarını topla ve yazdır
            for (int x = 0; x < width; x++) {
                int graySum = 0;
                for (int y = 0; y < height; y++) {
                    Color pixelColor = new Color(image.getRGB(x, y));
                    int grayScale = (pixelColor.getRed() + pixelColor.getGreen() + pixelColor.getBlue()) / 3;
                    graySum += grayScale;
                }
                System.out.println("Yatay pixel sayısı: " + (x + 1) + ", dikeydeki gri ton toplamı: " + graySum);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
