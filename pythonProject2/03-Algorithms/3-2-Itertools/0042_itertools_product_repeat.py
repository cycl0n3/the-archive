from itertools import *


def show(it):
    for i, item in enumerate(it, 1):
        print(item, end=' ')
        if i % 3 == 0:
            print()
    print()


print('Repeat 2:\n')
show(list(product(range(3), repeat=2)))

print('Repeat 3:\n')
show(list(product(range(3), repeat=3)))
