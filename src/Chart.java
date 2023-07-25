import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYDataset;

public class Chart {

    public static void Create(String title, String xAxisLabel, String yAxisLabel, DefaultXYDataset dataset){
        JFreeChart chart = ChartFactory.createXYLineChart(title,xAxisLabel,yAxisLabel,dataset);

        ChartFrame Frame = new ChartFrame(title,chart);
        Frame.pack();
        Frame.setVisible(true);

    }



}
