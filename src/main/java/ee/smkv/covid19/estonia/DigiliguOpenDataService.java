package ee.smkv.covid19.estonia;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;
import java.util.Optional;

public class DigiliguOpenDataService extends Service<TestResults> {
  private final DigiliguOpenDataProvider provider;
  private final Optional<File> file;

  public DigiliguOpenDataService(DigiliguOpenDataProvider provider, Optional<File> file) {
    this.provider = provider;
    this.file = file;
  }

  @Override
  protected Task<TestResults> createTask() {
    return new Task<TestResults>() {
      @Override
      protected TestResults call() throws Exception {
        if (file.isPresent()) {
          return provider.getCovid19TestResultsOffline(file.get());
        }
        return provider.getCovid19TestResultsOnline();
      }
    };
  }
}
