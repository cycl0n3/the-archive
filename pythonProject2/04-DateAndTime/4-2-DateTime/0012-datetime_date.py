import datetime


today = datetime.date.today()
print('Today:', today)
print('CTime:', today.ctime())
tt = today.timetuple()
print('Tuple:', tt)
print('Ordinal:', today.toordinal())
print('Year:', today.year)
print('Month:', today.month)
print('Year:', today.year)
