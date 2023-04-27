import collections

c = collections.Counter('abcdaab')

for letter in 'abcde':
    print('{!r}: {}'.format(letter, c[letter]))
