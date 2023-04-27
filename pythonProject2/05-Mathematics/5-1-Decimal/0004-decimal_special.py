import decimal


for v in ['Infinity', 'NaN', '0']:
    print(decimal.Decimal(v), -decimal.Decimal(v))
print()

# Math with infinity
print('Infinity + 1:', decimal.Decimal('Infinity') + 1)
print('-Infinity + 1:', -decimal.Decimal('Infinity') + 1)

# Print comparing NaN
print(decimal.Decimal('NaN') == decimal.Decimal('Infinity'))
print(decimal.Decimal('NaN') != decimal.Decimal(1))
