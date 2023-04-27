import calendar


c = calendar.LocaleTextCalendar(locale='en_US')
c.prmonth(2017, 7)

c = calendar.LocaleTextCalendar(locale='fr_FR')
c.prmonth(2017, 7)
