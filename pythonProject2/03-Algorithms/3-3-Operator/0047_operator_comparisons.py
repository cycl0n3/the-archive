from operator import *


a = 1.0
b = 5.0

print('a = ', a)
print('b = ', b)

for f in [lt, le, eq, ne, gt, ge]:
    print('{}(a, b): {}'.format(f.__name__, f(a, b)))

