import datetime

fmt = "%a %b %d %H:%M:%S %Y"

today = datetime.datetime.today()
print('ISO:', today)

s = today.strftime(fmt)
print('strftime:', s)

d = datetime.datetime.strptime(s, fmt)
print('strptime:', d.strftime(fmt))
