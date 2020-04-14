package ee.smkv.covid19.estonia;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Chart {
  private final XYChart chart;

  public Chart(String title, String xTitle, String yTitle, int width, int height) {
    chart = new XYChartBuilder()
      .title(title)
      .xAxisTitle(xTitle)
      .yAxisTitle(yTitle)
      .width(width)
      .height(height)
      .build();
    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
  }

  public void addSeries(String title, TreeMap<LocalDate, ? extends Number> data) {
    chart.addSeries(title, getXData(data.keySet()), getYData(data.values()));
  }

  public List<Date> getXData(Collection<LocalDate> data) {
    return data.stream().map(java.sql.Date::valueOf).collect(Collectors.toList());
  }

  public List<? extends Number> getYData(Collection<? extends Number> data) {
    return new ArrayList<>(data);
  }

  public void show(){
    new SwingWrapper<>(chart).displayChart();
  }
}
