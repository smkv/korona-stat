public class MovingAverage {
    private final int[] source;

    public MovingAverage(int[] source) {
        this.source = source;
    }

    public double[] getMovingAverage(int n) {
        if (n > source.length) {
            throw new IllegalArgumentException("n > source.length");
        }
        final double[] result = new double[source.length];
        int[] buffer = new int[n];
        int length;
        for (int i = 0; i < result.length; i++) {
            length = i < n ? i + 1 : n;
            System.arraycopy(source, Math.max(0, i - n), buffer, 0, length);
            result[i] = avg(buffer, length);
        }
        return result;
    }

    private double avg(int[] buffer, int length) {
        int sum = 0;
        for (int i1 = 0; i1 < length; i1++) {
            int i = buffer[i1];
            sum += i;
        }
        return (double) sum / length;
    }
}
