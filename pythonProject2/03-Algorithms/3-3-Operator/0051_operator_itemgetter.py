from operator import *


l = [{'val': -1 * i} for i in range(4)]
print('Dictionaries:')
print('  Original:', l)

g = itemgetter('val')
vals = [g(i) for i in l]
print('  Values:', vals)
print('  Sorted:', sorted(l, key=g))

l = [(i, i * -2) for i in range(4)]
print('\nTuples')
print('  Original:', l)

g = itemgetter(1)
vals = [g(i) for i in l]
print('  Values:', vals)
print('  Sorted:', sorted(l, key=g))
