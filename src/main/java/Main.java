import ee.smkv.covid19.estonia.DigiliguOpenDataProvider;
import ee.smkv.covid19.estonia.DigiliguOpenDataService;
import ee.smkv.covid19.estonia.MovingAverage;
import ee.smkv.covid19.estonia.Statistics;
import ee.smkv.covid19.estonia.StatisticsByDate;
import ee.smkv.covid19.estonia.TestResults;
import eu.hansolo.tilesfx.chart.SmoothedChart;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Main extends Application {
  private DigiliguOpenDataService digiliguOpenDataService;
  private StackedBarChart<String, Number> dailyCasesBarChart;
  private SmoothedChart<String, Number> movingAverageSmoothLineChart;
  private BorderPane loadingPane;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void init() throws Exception {
    super.init();
    digiliguOpenDataService = new DigiliguOpenDataService(new DigiliguOpenDataProvider(), getOfflineFile());
    dailyCasesBarChart = createDailyCasesBarChart();
    movingAverageSmoothLineChart = createMovingAverageSmoothLineChart();
    loadingPane = new BorderPane(new Label("Loading data, please wait...."));
  }

  private Optional<File> getOfflineFile() {
    String fileName = getParameters().getNamed().get("file");
    File file = fileName != null ? new File(fileName) : null;
    return Optional.ofNullable(file);
  }

  @Override
  public void start(Stage stage) {
    stage.setTitle("COVID19 Estonia - Daily cases");
    BorderPane root = new BorderPane(new StackPane(dailyCasesBarChart, movingAverageSmoothLineChart, loadingPane));
    root.setBottom(createCustomChartLegend());
    Scene scene = new Scene(root, 1200, 600);
    stage.setScene(scene);
    scene.getStylesheets().add("style.css");
    stage.show();
    digiliguOpenDataService.setOnSucceeded(this::onLoadingComplete);
    digiliguOpenDataService.start();
  }

  private void onLoadingComplete(WorkerStateEvent event) {
    TestResults covid19TestResults = (TestResults)event.getSource().getValue();
    Statistics statistics = new Statistics(covid19TestResults.getResults());
    StatisticsByDate positiveInEstonia = statistics.getPositiveByDays();
    StatisticsByDate positiveInHarjuCounty = statistics.getPositiveByDays("Harju maakond");
    StatisticsByDate positiveExcludeHarjuCounty = statistics.getPositiveByDaysExclude("Harju maakond");

    double max = positiveInEstonia.getMaxValue().doubleValue() * 1.10;
    ((NumberAxis)movingAverageSmoothLineChart.getYAxis()).setUpperBound(max);
    ((NumberAxis)dailyCasesBarChart.getYAxis()).setUpperBound(max);

    dailyCasesBarChart.getData().add(createSeries("Daily cases in Harju maakond", positiveInHarjuCounty));
    dailyCasesBarChart.getData().add(createSeries("Daily case in Estonia total", positiveExcludeHarjuCounty));

    movingAverageSmoothLineChart.getData().add(createSeries("Average in Estonia total", MovingAverage.getMovingAverage(positiveInEstonia, 7)));
    movingAverageSmoothLineChart.getData().add(createSeries("Average in Harju maakond", MovingAverage.getMovingAverage(positiveInHarjuCounty, 7)));

    loadingPane.setVisible(false);
  }

  private Pane createCustomChartLegend() {
    FlowPane legendPane = new FlowPane(Orientation.HORIZONTAL);
    legendPane.getStyleClass().add("legend");
    legendPane.getChildren().addAll(
      getLegendItem(" Daily cases in Estonia total", "positive-in-estonia"),
      getLegendItem(" Daily cases in Harju maakond", "positive-in-harju"),
      getLegendItem(" Moving average(7 days) in Estonia total", "average-in-estonia"),
      getLegendItem(" Moving average(7 days) in Harju maakond", "average-in-harju")
    );
    return legendPane;
  }

  private SmoothedChart<String, Number> createMovingAverageSmoothLineChart() {
    final SmoothedChart<String, Number> lineChart = new SmoothedChart<>(createXAxis(), createYAxis());
    lineChart.setCreateSymbols(false);
    lineChart.setLegendVisible(false);
    lineChart.setAnimated(false);
    lineChart.setAlternativeRowFillVisible(false);
    lineChart.setAlternativeColumnFillVisible(false);
    lineChart.setHorizontalGridLinesVisible(false);
    lineChart.setVerticalGridLinesVisible(false);
    lineChart.getXAxis().setVisible(false);
    lineChart.getYAxis().setVisible(false);
    return lineChart;
  }

  private StackedBarChart<String, Number> createDailyCasesBarChart() {
    final StackedBarChart<String, Number> barChart = new StackedBarChart<>(createXAxis(), createYAxis());
    barChart.setCategoryGap(2);
    barChart.setCategoryGap(1);
    barChart.setLegendVisible(false);
    barChart.setAnimated(false);
    return barChart;
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

  private NumberAxis createYAxis() {
    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Positive");
    yAxis.setAutoRanging(false);
    yAxis.setLowerBound(0);
    yAxis.setUpperBound(100);
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
}

