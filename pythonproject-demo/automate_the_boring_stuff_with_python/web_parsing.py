import requests
import webbrowser
import bs4


exampleFile = open('data/example.html')
exampleSoup = bs4.BeautifulSoup(exampleFile.read(), "html5lib")

elems = exampleSoup.select('#author')

print(type(elems))
print(len(elems))
print(type(elems[0]))
print(elems[0].getText())
print(str(elems[0]))
print(elems[0].attrs)

print('-'*20)

pElems = exampleSoup.select('p')

print(str(pElems[0]))
print(pElems[0].getText())
print(str(pElems[1]))
print(pElems[1].getText())
print(str(pElems[2]))
print(pElems[2].getText())

print('-'*20)

spanElem = exampleSoup.select('span')[0]
print(str(spanElem))
print(spanElem.get('id'))
print(spanElem.get('some_nonexistent_addr'))
print(spanElem.attrs)

res = requests.get('https://google.com/search?q=Blindspot tv series')
res.raise_for_status()
soup = bs4.BeautifulSoup(res.text, "html5lib")
linkElems = soup.select('.r a')
numOpen = min(1, len(linkElems))

for i in range(numOpen):
    webbrowser.open('https://google.com' + linkElems[i].get('href'))
