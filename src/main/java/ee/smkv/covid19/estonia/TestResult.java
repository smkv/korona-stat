package ee.smkv.covid19.estonia;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class TestResult {
    String id; // "95013b64dd5ff18548a92eb5375d9c4a1881467390fed4a1c084253ef72be9ea",
    String Gender; // "M" - man, "N" - woman
    String AgeGroup; // "10-14",
    String Country; // "Eesti",
    String County; // "Tartu maakond",
    String ResultValue; // "N" - negative, "P" - positive
    LocalDate StatisticsDate; // "2020-03-10",
    LocalDateTime ResultTime; // "2020-03-06T18:44:00+02:00",
    LocalDateTime AnalysisInsertTime; // "2020-03-10T16:01:55+02:00"


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getAgeGroup() {
        return AgeGroup;
    }

    public void setAgeGroup(String ageGroup) {
        AgeGroup = ageGroup;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCounty() {
        return County;
    }

    public void setCounty(String county) {
        County = county;
    }

    public String getResultValue() {
        return ResultValue;
    }

    public void setResultValue(String resultValue) {
        ResultValue = resultValue;
    }

    public LocalDate getStatisticsDate() {
        return StatisticsDate;
    }

    public void setStatisticsDate(LocalDate statisticsDate) {
        StatisticsDate = statisticsDate;
    }

    public LocalDateTime getResultTime() {
        return ResultTime;
    }

    public void setResultTime(LocalDateTime resultTime) {
        ResultTime = resultTime;
    }

    public LocalDateTime getAnalysisInsertTime() {
        return AnalysisInsertTime;
    }

    public void setAnalysisInsertTime(LocalDateTime analysisInsertTime) {
        AnalysisInsertTime = analysisInsertTime;
    }

    public boolean isPositive() {
        return "P".equals(getResultValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestResult testResult = (TestResult) o;
        return Objects.equals(id, testResult.id) &&
               Objects.equals(Gender, testResult.Gender) &&
               Objects.equals(AgeGroup, testResult.AgeGroup) &&
               Objects.equals(Country, testResult.Country) &&
               Objects.equals(County, testResult.County) &&
               Objects.equals(ResultValue, testResult.ResultValue) &&
               Objects.equals(StatisticsDate, testResult.StatisticsDate) &&
               Objects.equals(ResultTime, testResult.ResultTime) &&
               Objects.equals(AnalysisInsertTime, testResult.AnalysisInsertTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Gender, AgeGroup, Country, County, ResultValue, StatisticsDate, ResultTime, AnalysisInsertTime);
    }

    @Override
    public String toString() {
        return "ee.smkv.covid19.estonia.Data{" +
                "id='" + id + '\'' +
                ", Gender='" + Gender + '\'' +
                ", AgeGroup='" + AgeGroup + '\'' +
                ", Country='" + Country + '\'' +
                ", County='" + County + '\'' +
                ", ResultValue='" + ResultValue + '\'' +
                ", StatisticsDate=" + StatisticsDate +
                ", ResultTime=" + ResultTime +
                ", AnalysisInsertTime=" + AnalysisInsertTime +
                '}';
    }
}
