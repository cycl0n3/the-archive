import collections

Person = collections.namedtuple('Person', 'name age')

bob = Person(name='Bob', age=30)

print('Before:', bob)

peter = bob._replace(name='Peter')

print('After:', peter)

print('Same?:', peter is bob)
