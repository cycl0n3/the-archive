import os
import time


def show_zone_info():
    print('    TZ: ', os.environ.get('TZ', '(not set)'))
    print('    tzname: ', time.tzname)
    print('    Zone: {} ({})'.format(time.timezone, (time.timezone/3600)))
    print('    Time: ', time.ctime())
    print('    DST: ', time.daylight)


show_zone_info()

ZONES = [
    'GMT',
    'Europe/Amsterdam'
]

for zone in ZONES:
    os.environ['TZ'] = zone
    time.tzset()
    print(zone, ':')
    show_zone_info()

