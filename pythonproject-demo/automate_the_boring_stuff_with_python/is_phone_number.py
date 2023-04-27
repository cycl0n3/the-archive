import re


def is_phone_number(text):
    if len(text) != 12:
        return False
    for i in range(0, 3):
        if not text[i].isdecimal():
            return False
    if text[3] != '-':
        return False
    for i in range(4, 7):
        if not text[i].isdecimal():
            return False
    if text[7] != '-':
        return False
    for i in range(8, 12):
        if not text[i].isdecimal():
            return False
    return True


message = 'Call me at 415-555-1011 tomorrow. 415-555-9999 is my office'

for i in range(0, len(message)):
    chunk = message[i:i+12]
    if is_phone_number(chunk):
        print('Phone number found: ' + chunk)

print('-'*20)

phoneNumberRegex = re.compile(r'\d\d\d-\d\d\d-\d\d\d\d')
mo = phoneNumberRegex.search('My number is 415-555-4242.')
print('Phone number found: ' + mo.group())

print('-'*20)

phoneNumberRegex = re.compile(r'(\d\d\d)-(\d\d\d-\d\d\d\d)')
mo = phoneNumberRegex.search('My number is 415-555-4242.')
print(mo.group(1))
print(mo.group(2))
print(mo.group(0))
print(mo.groups())

print('-'*20)

areaCode, mainNumber = mo.groups()

print(areaCode)
print(mainNumber)

print('-'*20)

phoneNumberRegex = re.compile(r'(\(\d\d\d\)) (\d\d\d-\d\d\d\d)')
mo = phoneNumberRegex.search('My phone number is (415) 555-4242.')
print(mo.group(1))
print(mo.group(2))
