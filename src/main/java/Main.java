import ee.smkv.covid19.estonia.DigiliguOpenDataProvider;
import ee.smkv.covid19.estonia.MovingAverage;
import ee.smkv.covid19.estonia.Statistics;
import ee.smkv.covid19.estonia.TestResult;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TreeMap;

public class Main extends Application {
  final DigiliguOpenDataProvider provider = new DigiliguOpenDataProvider();
  private List<TestResult> covid19TestResult;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void init() throws Exception {
    super.init();
    String fileName = getParameters().getNamed().get("file");
    if (fileName != null) {
      covid19TestResult = provider.getCovid19TestResultsOffline(new File(fileName));
    }
    else {
      covid19TestResult = provider.getCovid19TestResultsOnline();
    }
  }

  @Override
  public void start(Stage stage) {
    final Statistics statistics = new Statistics(covid19TestResult);
    TreeMap<LocalDate, Long> positiveInEstonia = statistics.getPositiveByDays();
    TreeMap<LocalDate, Long> positiveInHarjuCounty = statistics.getPositiveByDays("Harju maakond");
    stage.setTitle("COVID19 Estonia");

    CategoryAxis xAxis = new CategoryAxis();
    xAxis.setLabel("Date");

    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Positive");

    final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
    barChart.setBarGap(2);
    barChart.setCategoryGap(1);
    setCss(barChart, "bar-chart.css");

    final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
    lineChart.setCreateSymbols(false);
    lineChart.setOpacity(0.7);
    setCss(lineChart, "line-chart.css");

    barChart.getData().add(createSeries("Positive in Estonia", positiveInEstonia));
    barChart.getData().add(createSeries("Positive in Harju maakond", positiveInHarjuCounty));

    lineChart.getData().add(createSeries("Average in Estonia", MovingAverage.getMovingAverage(positiveInEstonia, 7)));
    lineChart.getData().add(createSeries("Average in Harju maakond", MovingAverage.getMovingAverage(positiveInHarjuCounty, 7)));

    StackPane root = new StackPane();
    root.getChildren().addAll(barChart, lineChart);

    configureOverlayChart(lineChart);

    Scene scene = new Scene(root, 1200, 600);
    stage.setScene(scene);
    stage.show();
  }

  private XYChart.Series<String, Number> createSeries(String name, TreeMap<LocalDate, ? extends Number> positiveInEstonia) {
    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName(name);
    ObservableList<XYChart.Data<String, Number>> seriesData = series.getData();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
    positiveInEstonia.forEach((date, value) -> seriesData.add(new XYChart.Data<>(date.format(formatter), value)));
    return series;
  }

  private void configureOverlayChart(final XYChart<String, Number> chart) {
    chart.setAlternativeRowFillVisible(false);
    chart.setAlternativeColumnFillVisible(false);
    chart.setHorizontalGridLinesVisible(false);
    chart.setVerticalGridLinesVisible(false);
    chart.getXAxis().setVisible(false);
    chart.getYAxis().setVisible(false);
  }

  private void setCss(XYChart<String, Number> chart, String file) {
    chart.getStylesheets().addAll(getClass().getResource(file).toExternalForm());
  }
}
