from itertools import *


print('Stop at 5:')
for i in islice(range(100), 5):
    print(i, end=' ')
print()

print('Start at 5, stop at 10:')
for i in islice(range(100), 5, 10):
    print(i, end=' ')
print()

print('By tens to 100:')
for i in islice(range(100), 0, 100, 10):
    print(i, end=' ')
print()
