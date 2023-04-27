from itertools import *


r1 = range(5)
r2 = range(7)

print('zip stops early:')
print(list(zip(r1, r2)))

print('zip_longest processes all values:')
print(list(zip_longest(r1, r2)))
