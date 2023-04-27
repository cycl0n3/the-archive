from bs4 import BeautifulSoup

with open('data/alice.html', 'rb') as f:
    soup = BeautifulSoup(f, 'lxml')

print(soup.head)
# <head><title>The Dormouse's story</title></head>

print(soup.title)
# <title>The Dormouse's story</title>

print(soup.body.b)
# <b>The Dormouse's story</b>

print(soup.a)
# <a class="sister" href="http://example.com/elsie" id="link1">Elsie</a>

print(soup.find_all('a'))
# [<a class="sister" href="http://example.com/elsie" id="link1">Elsie</a>,
#  <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>,
#  <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>]

head_tag = soup.head
print(head_tag)
# <head><title>The Dormouse's story</title></head>

print(head_tag.contents)
# [<title>The Dormouse's story</title>]

title_tag = head_tag.contents[1]
print(title_tag)
# <title>The Dormouse's story</title>

print(title_tag.contents)
# ['The Dormouse's story']

print(soup.contents)
print(len(soup.contents))

for child in title_tag.children:
    print(child)
# The Dormouse's story

for child in head_tag.descendants:
    print(child)
# <title>The Dormouse's story</title>
# The Dormouse's story

print(len(list(soup.children)))
print(len(list(soup.descendants)))

for s in soup.stripped_strings:
    print(repr(s))
