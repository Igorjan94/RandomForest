package ru.igorjan94.randomForest;

import java.util.*;

public class RandomForest {
    private List<DecisionTree> trees;
    private int count;

    public double get(Instance instance) {
        Map<Integer, Integer> map = new HashMap<>();
        trees.forEach(tree -> {
            int value = tree.get(instance);
            map.put(value, map.getOrDefault(value, 0) + 1);
        });
        int mx = 0;
        int value = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() > mx) {
                mx = entry.getValue();
                value = entry.getKey();
            }
        }
        if (value == 1)
            return mx / (0.0 + count);
        else
            return 1 - mx / (0.0 + count);
        //return value;
    }

    RandomForest(List<Instance> data, int count) {
        trees = new ArrayList<>();
        this.count = count;
        for (int i = 0; i < count; i++) {
            trees.add(new DecisionTree(data, 0));
        }
    }
}
