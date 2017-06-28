import matplotlib.pyplot as plt
import math

from subprocess import check_output
from sklearn.metrics import roc_curve, auc
from sklearn.model_selection import train_test_split
from sklearn.datasets import make_moons
from sklearn.ensemble import RandomForestClassifier
from sklearn.tree import DecisionTreeClassifier

def getOutput(command):
    return ''.join(map(chr, check_output(command.split())))
def getLabels(y):
    return '\n'.join(map(str, y))
def getFeatures(X):
    return '\n'.join(map(lambda s: ' '.join(map(str, map(lambda x: math.floor(x * 1000), s))) + ' ', X)) + '\n'

plt.figure()
lw = 1

X, y = make_moons(n_samples = 1000)
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = .2, random_state = 0)

path = 'trees/dataset'
open(path + 'Train.data', 'w').write(getFeatures(X_train))
open(path + 'Train.labels', 'w').write(getLabels(y_train))
open(path + 'Test.data', 'w').write(getFeatures(X_test))
open(path + 'Test.labels', 'w').write(getLabels(y_test))

classifiers = [RandomForestClassifier(), DecisionTreeClassifier()]
for classifier in classifiers:
    s = str(classifier)
    s = s[:s.find('(')]
    y_score = classifier.fit(X_train, y_train).predict_proba(X_test)[:, 1]
    fpr, tpr, _ = roc_curve(y_test, y_score)
    plt.plot(fpr, tpr, lw = lw, label = s + ' (area = %0.4f)' % auc(fpr, tpr))

getOutput('javac Main.java Instance.java DecisionTree.java RandomForest.java Result.java')
for i in range(1, 10):
    java = getOutput('java -cp ../../../ ru.igorjan94.randomForest.Main 2 11 ' + str(i)).split('OUTPUT')[1]
    test = list(map(float, java.split('TRAIN')[0].split('\n')[1:-1]))
    train = list(map(float, java.split('TRAIN')[1].split('\n')[1:-1]))
    fpr, tpr, _ = roc_curve(y_test, test)
    plt.plot(fpr, tpr, lw = lw, label = 'My (%d) (area = %0.4f)' % (i, auc(fpr, tpr)))
    fpr, tpr, _ = roc_curve(y_train, train)
    plt.plot(fpr, tpr, lw = 1, label = 'My train (%d) (area = %0.4f)' % (i, auc(fpr, tpr)))

plt.xlim([0.0, 1.0])
plt.ylim([0.0, 1.05])
plt.xlabel('False Positive Rate')
plt.ylabel('True Positive Rate')
plt.title('Desicion tree mine')
plt.legend(loc = "lower right")
plt.show()
