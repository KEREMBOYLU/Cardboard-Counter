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

    public static double[] findPeaksAboveThreshold(double[][] data, int threshold) {
        List<Double> peakHeights = new ArrayList<>();

        for (int i = 0; i < data.length; i++) {
            boolean aboveThreshold = data[i][0] >= threshold;
            double peakHeight = -1.0;

            for (int j = 1; j < data[i].length; j++) {
                boolean currentAbove = data[i][j] >= threshold;

                if (currentAbove != aboveThreshold) {
                    if (!currentAbove) {
                        // Tepe noktası bulundu
                        if (peakHeight != -1.0) {
                            peakHeights.add(peakHeight);
                        }
                        peakHeight = data[i][j];
                    }
                } else if (currentAbove && data[i][j] > peakHeight) {
                    peakHeight = data[i][j];
                }
                aboveThreshold = currentAbove;
            }

            if (peakHeight != -1.0) {
                peakHeights.add(peakHeight);
            }
        }

        return peakHeights.stream().mapToDouble(Double::doubleValue).toArray();
    }


}
