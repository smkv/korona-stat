import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {

        final Provider provider = new Provider();
        final Map<LocalDate, Long> groupedData = provider.getPositiveStatisticsByDays();

        final int[] source = groupedData.values().stream().mapToInt(Long::intValue).toArray();
        final MovingAverage movingAverage = new MovingAverage(source);


        final XYChart chart = new XYChart(800, 600);
        chart.addSeries("source", source);
        chart.addSeries("AVG7", movingAverage.getMovingAverage(7));

        final SwingWrapper<XYChart> wrapper = new SwingWrapper<>(chart);
        wrapper.displayChart();
    }
}
