import collections

Person = collections.namedtuple('Person', 'name age')

bob = Person(name='Bob', age=30)

print('Representation:', bob)
print('Fields:', bob._fields)
