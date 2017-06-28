from sklearn import tree

path = '../../trees/'

def read(file):
    features = list(map(lambda x: list(map(int, x[:-2].split(' '))), open(path + file + '.data').readlines()))
    labels   = list(map(int, open(path + file + '.labels').readlines()))
    return features, labels


X, Y = read('arcene_train')
features, labels = read('arcene_valid')

clf = clf.fit(X, Y)
predictions = clf.predict(features)

tp, tn, fp, fn = [0] * 4

for a, b in zip(predictions, labels):
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

print('F = %4.3f   P = %4.3f   R = %4.3f   C = %4.3f' % (f, p, r, c))

