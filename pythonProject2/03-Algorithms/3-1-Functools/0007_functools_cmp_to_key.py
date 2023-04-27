import functools


class MyObject(object):
    def __init__(self, val):
        self.val = val

    def __str__(self):
        return 'MyObject({})'.format(self.val)


def compare(a, b):
    """Old-style comparison function."""
    print('Comparing {} and {}'.format(a, b))
    if a.val < b.val:
        return -1
    elif a.val > b.val:
        return 1
    return 0


get_key = functools.cmp_to_key(compare)


def get_key_wrapper(o):
    """Wrapper function for get_key to allow for print statements."""
    new_key = get_key(o)
    print('key_wrapper({}) -> {!r}'.format(o, new_key))
    return new_key


objs = [MyObject(x) for x in range(5, 0, -1)]

for x in sorted(objs, key=get_key_wrapper):
    print(x)

