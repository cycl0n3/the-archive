import collections

a = {'a': 'A', 'c': 'C'}
b = {'b': 'B', 'c': 'D'}

m = collections.ChainMap(a, b)
print('Before:', m)

a['c'] = 'E'

print('After : {}'.format(m['c']))
print('After:', m)
