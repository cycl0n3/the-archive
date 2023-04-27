from bs4 import BeautifulSoup
from pathlib import Path


for i in range(1, 10):
    path_to_file = f'.\\data\\page{i}.html'
    path = Path(path_to_file)

    if not path.is_file():
        with open(f'.\\data\\page{i}.xml', 'r') as f:
            data = f.read()
            soup = BeautifulSoup(data, 'xml')

            for book in soup.find_all('book'):
                name = book.find('name').string
                print('name: ', name)

                publisher = book.find('publisher').string
                print('publisher: ', publisher)

                year = book.find('year').string
                print('year: ', year)

                language = book.find('language').string
                print('language: ', language)

                book_ = (name, publisher, language, year)

                for author in book.find_all('authors'):
                    print('author:', author.find('name').string)

                    author_ = (author.find('name').string,)

                break
