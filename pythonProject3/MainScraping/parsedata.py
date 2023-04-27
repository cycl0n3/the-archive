import psycopg2
import xml.etree.ElementTree as ET

from bs4 import BeautifulSoup
from pathlib import Path


def parse(url):
    with open(url, 'rb') as f:
        s = BeautifulSoup(f, 'html.parser')
    return s


for j in range(1, 10):
    html_page = f'.\\data\\page{j}.html'
    soup = parse(html_page)

    tables = soup.find_all('table')
    table_main = tables[2]
    rows = table_main.find_all('tr')

    xml_books = ET.Element('books')

    print(f'Page {j} starting')

    for i in range(1, len(rows)):
        row = rows[i]

        authors = row.find_all('td')[1]
        book = row.find_all('td')[2]
        publisher = row.find_all('td')[3]
        year = row.find_all('td')[4]
        language = row.find_all('td')[6]

        books_ = []
        for b in book.find_all('a'):
            for it in b.find_all('i'):
                it.replace_with('')
            books_.append(''.join(b.strings))

        authors_ = []
        for a in authors.find_all('a'):
            if a.string is not None:
                authors_.append(a.string.strip())

        # print(f'Book {i}: {book_}')
        # print(f'Authors {i}: {authors_}')

        for b in books_:
            b = b.strip()
            if b != '':
                xml_book = ET.SubElement(xml_books, 'book')

                xml_book_name = ET.SubElement(xml_book, 'name')
                xml_book_name.text = b

                xml_book_publisher = ET.SubElement(xml_book, 'publisher')
                xml_book_publisher.text = publisher.string

                xml_book_year = ET.SubElement(xml_book, 'year')
                xml_book_year.text = year.string

                xml_book_language = ET.SubElement(xml_book, 'language')
                xml_book_language.text = language.string

                xml_authors = ET.SubElement(xml_book, 'authors')

                for a in authors_:
                    a = a.strip()
                    if a != '':
                        xml_author = ET.SubElement(xml_authors, 'author')
                        xml_author_text = ET.SubElement(xml_author, 'name')
                        xml_author_text.text = a

    xml_books_str = ET.tostring(xml_books)

    with open(f'.\\data\\page{j}.xml', 'wb') as f:
        f.write(xml_books_str)

    print(f'Page {j} done')
