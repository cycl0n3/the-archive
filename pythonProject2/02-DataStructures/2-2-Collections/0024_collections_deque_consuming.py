import collections


d = collections.deque('abcdefg')

print('From the right:', end='')
while True:
    try:
        print(d.pop(), end='')
    except IndexError:
        break

print('')

d = collections.deque(range(6))

print('From the left:', end='')
while True:
    try:
        print(d.popleft(), end='')
    except IndexError:
        break
