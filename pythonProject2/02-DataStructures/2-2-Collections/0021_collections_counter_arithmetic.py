import collections

c1 = collections.Counter(['a', 'b', 'c', 'a', 'b', 'b'])
c2 = collections.Counter('alphabet')

print('C1: ', c1)
print('C2: ', c2)

print('Addition:', c1 + c2)
print('Subtraction:', c1 - c2)
print('Intersection:', c1 & c2)
print('Union:', c1 | c2)
