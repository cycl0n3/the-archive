from bs4 import BeautifulSoup


soup = BeautifulSoup('<b id="boldest">bold</b>', 'html.parser')
tag = soup.b

print('tag[id]:', tag['id'])
print('tag.attrs:', tag.attrs)

print('Printing Tag:')
print('tag:', tag)

print('\nAdding and Changing attributes:')
tag['id'] = 'verybold'
tag['another-attribute'] = 1
print('tag:', tag)

print('\nDeleting attributes:')
del tag['id']
del tag['another-attribute']
print('tag:', tag)
