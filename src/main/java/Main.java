import ee.smkv.covid19.estonia.DigiliguOpenDataProvider;
import ee.smkv.covid19.estonia.MovingAverage;
import ee.smkv.covid19.estonia.Statistics;
import ee.smkv.covid19.estonia.StatisticsByDate;
import ee.smkv.covid19.estonia.TestResult;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    StatisticsByDate positiveInEstonia = statistics.getPositiveByDays();
    StatisticsByDate positiveInHarjuCounty = statistics.getPositiveByDays("Harju maakond");
    stage.setTitle("COVID19 Estonia");

    double max = positiveInEstonia.getMaxValue().doubleValue() * 1.10;
    final BarChart<String, Number> barChart = new BarChart<>(createXAxis(), createYAxis(max));
    barChart.setBarGap(2);
    barChart.setCategoryGap(1);
    barChart.setLegendVisible(false);
    barChart.setAnimated(false);

    final LineChart<String, Number> lineChart = new LineChart<>(createXAxis(), createYAxis(max));
    lineChart.setCreateSymbols(false);
    lineChart.setLegendVisible(false);
    lineChart.setAnimated(false);

    barChart.getData().add(createSeries("Positive in Estonia", positiveInEstonia));
    barChart.getData().add(createSeries("Positive in Harju maakond", positiveInHarjuCounty));

    lineChart.getData().add(createSeries("Average in Estonia", MovingAverage.getMovingAverage(positiveInEstonia, 7)));
    lineChart.getData().add(createSeries("Average in Harju maakond", MovingAverage.getMovingAverage(positiveInHarjuCounty, 7)));

    StackPane chartsPane = new StackPane();
    chartsPane.getChildren().addAll(barChart, lineChart);

    BorderPane root = new BorderPane(chartsPane);
    FlowPane legendPane = new FlowPane(Orientation.HORIZONTAL);
    root.setBottom(legendPane);
    legendPane.getStyleClass().add("legend");
    legendPane.getChildren().addAll(
      getLegendItem(" Positive in Estonia", "positive-in-estonia"),
      getLegendItem(" Positive in Harju maakond", "positive-in-harju"),
      getLegendItem(" Moving average(7 days) in Estonia", "average-in-estonia"),
      getLegendItem(" Moving average(7 days) in Harju maakond", "average-in-harju")
    );

    configureOverlayChart(lineChart);

    Scene scene = new Scene(root, 1200, 600);
    stage.setScene(scene);
    scene.getStylesheets().add("style.css");
    stage.show();
  }

  private Node getLegendItem(String label, String cssClassName) {
    BorderPane pane = new BorderPane(new Label(label));
    Circle circle = new Circle(6);
    circle.getStyleClass().add(cssClassName);
    circle.getStyleClass().add("legend-icon");
    pane.setLeft(circle);
    pane.getStyleClass().add("legend-item");
    return pane;
  }

  private NumberAxis createYAxis(double max) {
    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Positive");
    yAxis.setAutoRanging(false);
    yAxis.setLowerBound(0);
    yAxis.setUpperBound(max);
    return yAxis;
  }

  private CategoryAxis createXAxis() {
    CategoryAxis xAxis = new CategoryAxis();
    xAxis.setLabel("Date");
    return xAxis;
  }

  private XYChart.Series<String, Number> createSeries(String name, StatisticsByDate positiveInEstonia) {
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
}
