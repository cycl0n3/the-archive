from operator import *


class MyObj(object):
    """example class for attrgetter"""
    def __init__(self, arg):
        self.arg = arg

    def __repr__(self):
        return 'MyObj({})'.format(self.arg)


l = [MyObj(i) for i in range(5)]
print('objects:', l)

g = attrgetter('arg')
vals = [g(i) for i in l]
print('arg values:', vals)

l.reverse()
print('reversed:', l)
print('sorted:', sorted(l, key=g))
