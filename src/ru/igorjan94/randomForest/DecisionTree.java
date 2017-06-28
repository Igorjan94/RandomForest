package ru.igorjan94.randomForest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DecisionTree {
	private int color, question, separator;
	private DecisionTree l, r;
	public static int maxHeight = 1;
	static Random rnd = new Random();

	public int get(Instance instance) {
		if (l == r) return color;

		if (instance.calc(question, separator)) return l.get(instance);
		else return r.get(instance);
	}

	double calcEntropy(double x, double y) {
		double z = x + y;
		x /= z;
		y /= z;
		return -x * Math.log(x) - y * Math.log(y);
	}

	double getEntropy(List<Instance> data, int i, int coefficient) {

		double lp = 0, ln = 0, rp = 0, rn = 0;

		for (Instance instance : data) {
			boolean a = instance.calc(i, coefficient);
			boolean b = instance.color == 1;

			if (a) {
				if (b) ++lp;
				else ++ln;
			} else {
				if (b) ++rp;
				else ++rn;
			}

		}
		double le = calcEntropy(lp, ln), re = calcEntropy(rp, rn);
//        return le * le + re * re;
		return le * le * le + re * re * re;
//		return le + re;
	}

	DecisionTree(List<Instance> data, int height) {
		int x = 0, y = 0;
		for (Instance instance : data) {
			if (instance.color == 1) ++x;
			else ++y;
		}
		int n = Instance.numberOfFeatures;

		double minEntropy = Double.POSITIVE_INFINITY;

		for (int i = 0; i < n; i++) {
			double entropy = Double.POSITIVE_INFINITY;
			int coefficient = 0;
			for (Instance instance : data) {
				int cur = instance.get(i);
				double temp = getEntropy(data, i, cur);
				if (temp < entropy) {
					entropy = temp;
					coefficient = cur;
				}
			}

			if (entropy < minEntropy || (entropy == minEntropy && rnd.nextBoolean())) {
				minEntropy = entropy;
				separator = coefficient;
				question = i;
			}
		}

		if (Double.isInfinite(minEntropy) || height >= maxHeight) {
			color = rnd.nextInt(2);// * 2 - 1;
			if (x > y) color = 1;
			if (x < y) color = 0;
			return;
		}

		List<Instance> t = new ArrayList<>(), f = new ArrayList<>();
		for (Instance instance : data) {
			if (instance.calc(question, separator)) t.add(instance);
			else f.add(instance);
		}
		if (false)
            System.out.printf("feature = %5d    x = %3d, y = %3d    |l| = %3d, |r| = %3d   minEntropy = %.4f%n", question, x, y, t.size(), f.size(), minEntropy);

		l = new DecisionTree(t, height + 1);
		r = new DecisionTree(f, height + 1);
//		System.out.println("LOL " + l.color + " " + r.color);
		if (l != null && r != null && l.color == r.color) {
			color = l.color;
//			l = r = null;
        }

	}
}
