from itertools import *


def show(it):
    first = None
    for i, item in enumerate(it, 1):
        if first != item[0]:
            if first is not None:
                print()
            first = item[0]
        print(''.join(item), end=' ')
    print()


print('All permutations:\n')
show(permutations('abcd'))
# print(list(permutations('abcd')))

print('\nPairs:\n')
show(permutations('abcd', r=2))
