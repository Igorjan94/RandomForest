package ru.igorjan94.randomForest;

import java.util.List;
import java.util.Locale;

public class Result implements Comparable {
    double p = 0, r = 0, f = 0, c = 0;

    public String toString() {
        return String.format(Locale.US,"F = %.2f, P = %.2f, R = %.2f, C = %.2f", f, p, r, c);
    }

    Result(List<Instance> results) {
        double tp = 0, tn = 0, fp = 0, fn = 0;
        for (Instance result : results) {
            boolean a = result.color == 1;
            boolean b = result.prediction == 1;
            if (a) {
                if (b) ++tp;
                else ++tn;
            } else {
                if (b) ++fp;
                else ++fn;
            }

        }
        p = tp / (tp + fp);
        r = tp / (tp + fn);
        f = 2 * (p * r) / (p + r);
        c = (tp + fn) / (tp + tn + fp + fn);
	}

    public Result() {}

    @Override
    public int compareTo(Object o) {
        return Double.compare(((Result) o).p, p);
    }

    public void add(Result current) {
        p += current.p;
        r += current.r;
        f += current.f;
        c += current.c;
    }
}
