from bs4 import BeautifulSoup

with open('data/alice.html', 'rb') as f:
    soup = BeautifulSoup(f, 'lxml')

# print(soup.prettify())
# print(soup.getText())

for link in soup.find_all('a'):
    print(link)

