import urllib3
import re


phoneRegex = re.compile(r'''
    (\d{3}|\(\d{3}\))?
    (\s|-|\.)?
    (\d{3})
    (\s|-|\.)
    (\d{4})
    (\s*(ext|x|ext.)\s*(\d{2,5}))?
''', re.VERBOSE)

emailRegex = re.compile(r'''
    [a-zA-Z0-9._%+-]+
    @
    [a-zA-Z0-9.-]+
    (\.[a-zA-Z]{2,4})
''', re.VERBOSE)

http = urllib3.PoolManager()
req = http.request('GET', 'https://www.nostarch.com/contactus.htm')

text = str(req.data)

matches = []
for groups in phoneRegex.findall(text):
    phoneNum = '-'.join([groups[0], groups[2], groups[4]])
    if groups[7] != '':
        phoneNum += ' x' + groups[7]
    matches.append(phoneNum)

for groups in emailRegex.findall(text):
    matches.append(groups[0])

print(matches)
