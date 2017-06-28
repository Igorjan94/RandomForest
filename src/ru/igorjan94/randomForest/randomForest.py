from sklearn import tree
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import cross_val_score
from sklearn import svm

path = 'trees/'

def read(file):
    features = list(map(lambda x: list(map(int, x[:-2].split(' '))), open(path + file + '.data').readlines()))
    labels   = list(map(int, open(path + file + '.labels').readlines()))
    return features, labels

def getMetrics(values):
    tp, tn, fp, fn = [0] * 4

    for a, b in values:
        if a == 1:
            if b == 1: tp += 1
            else: tn += 1
        else:
            if b == 1: fp += 1
            else: fn += 1

    p = tp / (tp + fp)
    r = tp / (tp + fn)
    f = 2 * (p * r) / (p + r)
    c = (tp + fn) / (tp + tn + fp + fn)
    return p, r, f, c

X, Y = read('arcene_train')
features, labels = read('arcene_valid')

decisionTree = tree.DecisionTreeClassifier()
decisionTree = decisionTree.fit(X, Y)
predictions = decisionTree.predict(features)
p, r, f, c = getMetrics(zip(labels, predictions))
print('DecisionTreeClassifier: F = %4.3f   P = %4.3f   R = %4.3f   C = %4.3f' % (f, p, r, c))

svm = RandomForestClassifier()
svm = svm.fit(X, Y)
predictions = svm.predict(features)
p, r, f, c = getMetrics(zip(labels, predictions))
print('Svm:                    F = %4.3f   P = %4.3f   R = %4.3f   C = %4.3f' % (f, p, r, c))

randomForest = RandomForestClassifier()
randomForest = randomForest.fit(X, Y)
predictions = randomForest.predict(features)
p, r, f, c = getMetrics(zip(labels, predictions))
print('RandomForestClassifier: F = %4.3f   P = %4.3f   R = %4.3f   C = %4.3f' % (f, p, r, c))

