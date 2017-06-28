package ru.igorjan94.randomForest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

	private static final String rootDir = "trees" + File.separator;
	private static final String dataset = "dataset";
	private static final String trainFilePath = rootDir + dataset + "Train";
	private static final String testFilePath = rootDir + dataset + "Test";
    private static int parts = 2;
    private static int decisionTreeCount = 5;

	static List<Instance> read(String file) throws IOException {
		List<Instance> list = new ArrayList<>(100);

		try (BufferedReader data = new BufferedReader(new FileReader(new File(file + ".data")))) {
			try (BufferedReader lable = new BufferedReader(new FileReader(new File(file + ".labels")))) {
				String dataLine, labelLine;

				while ((dataLine = data.readLine()) != null && (labelLine = lable.readLine()) != null) {
					String[] number = dataLine.split(" ");
					if (number.length == 0 || labelLine.length() == 0) break;
					int color = Integer.parseInt(labelLine);

					int[] features = new int[number.length];
					for (int i = 0; i < features.length; i++) features[i] = Integer.parseInt(number[i]);

					list.add(new Instance(features, color));
				}
			}
		}

		return list;
	}

	public static void main(String[] args) throws IOException {
        if (args.length == 1)
            parts = Integer.parseInt(args[0]);
        if (args.length == 2) {
            parts = Integer.parseInt(args[0]);
            decisionTreeCount = Integer.parseInt(args[1]);
        }
        if (args.length == 3) {
            parts = Integer.parseInt(args[0]);
            decisionTreeCount = Integer.parseInt(args[1]);
            DecisionTree.maxHeight = Integer.parseInt(args[2]);
        }
		List<Instance> trainSet = read(trainFilePath);
		List<Instance> testSet = read(testFilePath);
//		Collections.shuffle(trainSet);

        long startTime, finishTime;
		startTime = System.currentTimeMillis();

        Result result = new Result();
        Result average = new Result();
        System.out.println("Count of parts: " + parts);
        System.out.println("Decision trees: " + decisionTreeCount);
        System.out.printf("Train size: %5d, test size %5d%n", trainSet.size() / parts, trainSet.size() / parts * (parts - 1));
        if (false)
        for (int i = 0; i < parts; i++) {
            List<Instance> train = new ArrayList<>();
            List<Instance> test = new ArrayList<>();
            for (int j = 0; j < trainSet.size(); j++) {
                if (j % parts == i)
                    train.add(trainSet.get(j));
                else
                    test.add(trainSet.get(j));
            }
            RandomForest decisionTree = new RandomForest(train, decisionTreeCount);
            for (Instance instance : test)
                if (decisionTree.get(instance) >= 0.5)
                    instance.prediction = 1;
                else
                    instance.prediction = 0;
            Result current = new Result(test);
            if (current.compareTo(result) < 0)
                result = current;
            average.add(current);
        }

        average.c /= parts;
        average.p /= parts;
        average.r /= parts;
        average.f /= parts;
        System.out.println("Max result:");
        System.out.println(result);

        System.out.println("Average:");
        System.out.println(average);

		finishTime = System.currentTimeMillis();
		System.out.println("test time = " + (finishTime - startTime) / 1000.0);
        System.out.println("OUTPUT");

        RandomForest decisionTree = new RandomForest(trainSet, decisionTreeCount);
        for (Instance instance : testSet)
            System.out.println(decisionTree.get(instance));
        System.out.println("TRAIN");
        for (Instance instance : trainSet)
            System.out.println(decisionTree.get(instance));
	}
}
