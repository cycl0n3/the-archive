import functools
import inspect

from pprint import pprint


@functools.total_ordering
class MyObject(object):
    def __init__(self, val):
        self.val = val

    def __eq__(self, other):
        print('testing __eq__ ({}, {})'.format(self.val, other.val))
        return self.val == other.val

    def __gt__(self, other):
        print('testing __gt__ ({}, {})'.format(self.val, other.val))
        return self.val > other.val

    # def __le__(self, other):
    #    print('testing __le__ ({}, {})'.format(self.val, other.val))
    #    return self.val <= other.val


print('Methods')
pprint(inspect.getmembers(MyObject, inspect.isfunction))

a = MyObject(1)
b = MyObject(2)

print('Comparisons')
for expr in ['a < b', 'a <= b', 'a == b', 'a >= b', 'a > b']:
    print('\n{:<6}:'.format(expr))
    result = eval(expr)
    print('    the result of {} is {}'.format(expr, result))

