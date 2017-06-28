package ru.igorjan94.randomForest;

public class Instance {
	private final int[] features;
	public final int color;
	public int prediction;
	public static int numberOfFeatures = 100000;

	public boolean calc(int i, int c) {
		return features[i] < c;
	}

	public int get(int i) {
		return features[i];
	}

	public Instance(int[] features, int color) {
		this.color = color;
		this.features = features;

		numberOfFeatures = Math.min(numberOfFeatures, features.length);
	}
}
