from bs4 import BeautifulSoup

soup = BeautifulSoup('<b class="boldest">Extremely bold</b>', 'html.parser')
tag = soup.b

print(tag.string)
# 'Extremely bold'

print(type(tag.string))
# <class 'bs4.element.NavigableString'>

unicode_string = str(tag.string)
print(type(unicode_string))
# <type 'str'>

tag.string.replace_with("No longer bold")
print(tag)
