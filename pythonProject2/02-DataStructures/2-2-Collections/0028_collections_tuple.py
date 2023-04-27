bob = ('Bob', 30, 'Male')
print('Representation: ', bob)

jane = ('Jane', 29, 'female')
print('Field by index:', jane[0])

print('\nFields by index:')
for p in [bob, jane]:
    print('{} is a {} year old {}'.format(*p))
