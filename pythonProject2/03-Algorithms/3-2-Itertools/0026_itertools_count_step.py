from fractions import *
from itertools import *


start = Fraction(1, 5)
step = Fraction(1, 5)

for i in zip(count(start, step), ['a', 'b', 'c', 'd', 'e']):
    print('{}: {}'.format(*i))

