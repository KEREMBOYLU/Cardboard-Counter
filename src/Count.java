
public class Count {

    public static void countThresholdLines(int linesCrossingThreshold, String title) {

        if (linesCrossingThreshold % 2 == 1) {
            linesCrossingThreshold++;
            linesCrossingThreshold /= 2;
        } else {
            linesCrossingThreshold /= 2;
        }

        System.out.println(title + " için sonuç: " + linesCrossingThreshold);
    }

    public static int countThresholdIntersections(double[][] data, int threshold) {
        int count = 0;
        for (int i = 0; i < data.length; i++) {
            boolean aboveThreshold = data[i][0] >= threshold;
            for (int j = 1; j < data[i].length; j++) {
                boolean currentAbove = data[i][j] >= threshold;
                if (currentAbove != aboveThreshold) {
                    count++;
                }
                aboveThreshold = currentAbove;
            }
        }
        return count;
    }
}

