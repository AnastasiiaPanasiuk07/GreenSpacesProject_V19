package ua.edu.nupp.kpp.server;

public class RiskCalculator {
    public static double calculate(double f1, double f2, double f3) {
        check(f1);
        check(f2);
        check(f3);
        return Math.round((0.40 * f1 + 0.35 * f2 + 0.25 * f3) * 100.0) / 100.0;
    }

    private static void check(double value) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Фактор має бути в діапазоні 0..100");
        }
    }
}
