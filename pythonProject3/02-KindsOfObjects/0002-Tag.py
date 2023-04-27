from bs4 import BeautifulSoup


soup = BeautifulSoup('<b class="boldest">Extremely bold</b>', 'html.parser')
tag = soup.b

print('tag', tag)
print('type(tag):', type(tag))
print('tag.name:', tag.name)
