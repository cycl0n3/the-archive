import collections

try:
    collections.namedtuple('Person', 'name class age')
except ValueError as e:
    print(e)


try:
    collections.namedtuple('Person', 'name age age')
except ValueError as e:
    print(e)
