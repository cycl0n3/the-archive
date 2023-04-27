import pprint

from bs4 import BeautifulSoup
from configparser import ConfigParser
from mysql.connector import MySQLConnection, Error

from pathlib import Path


def get_books_and_authors():
    all_books = list()

    for i in range(1, 10):
        with open(f'.\\data\\page{i}.xml', 'r') as f:
            data = f.read()
            soup = BeautifulSoup(data, 'xml')

            for book in soup.find_all('book'):
                all_authors = list()

                name = book.find('name').string
                # print('name: ', name)

                publisher = book.find('publisher').string
                # print('publisher: ', publisher)

                year = book.find('year').string
                # print('year: ', year)

                language = book.find('language').string
                # print('language: ', language)

                # book_ = (name, publisher, language, year, all_authors)
                book_ = {
                    'name': str(name),
                    'publisher': str(publisher),
                    'language': str(language),
                    'year': str(year),
                    'authors': all_authors
                }

                all_books.append(book_)
                authors = book.find('authors')

                for author in authors.find_all('author'):
                    if author.find('name') is not None:
                        author_ = author.find('name').string
                        all_authors.append(str(author_))

                # print(all_authors)

    return all_books


def read_db_config(filename='database.ini', section='mysql'):
    """ Read database configuration file and return a dictionary object
    :param filename: name of the configuration file
    :param section: section of database configuration
    :return: a dictionary of database parameters
    """
    # create parser and read ini configuration file
    parser = ConfigParser()
    parser.read(filename)

    # get section, default to mysql
    db = {}
    if parser.has_section(section):
        items = parser.items(section)
        for item in items:
            db[item[0]] = item[1]
    else:
        raise Exception('{0} not found in the {1} file'.format(section, filename))

    return db


def connect():
    """ Connect to MySQL database """

    db_config = read_db_config()
    conn = None
    books_and_authors = get_books_and_authors()

    try:
        print('Connecting to MySQL database...')
        conn = MySQLConnection(**db_config)

        cursor = conn.cursor(prepared=True)

        if conn.is_connected():
            print('Connection established.')

            for book_ in books_and_authors:
                # print(book_query_args)
                # print(book_)

                cursor.execute("SELECT * FROM publisher p WHERE p.name = ?", (book_['publisher'],))

                if cursor.fetchone() is None:
                    cursor.execute("INSERT INTO publisher(name) VALUES(?)", (book_['publisher'],))
                    publisher_id = cursor.lastrowid
                else:
                    publisher_id = None

                book_query_args = (book_['name'], book_['year'], book_['language'], publisher_id)

                cursor.execute("INSERT INTO book(title, year, language, publisher_fk_id) VALUES(?, ?, ?, ?)", book_query_args)
                conn.commit()

                book_id = cursor.lastrowid

                # if publisher_id is not None:
                #    cursor.execute("INSERT INTO publisher_books(publisher_id, book_id) VALUES(?, ?)", (publisher_id, book_id))
                #    conn.commit()

                for author_ in book_['authors']:
                    # print(author_)

                    cursor.execute("SELECT * FROM author a WHERE a.name = ?", (author_,))
                    if cursor.fetchone() is not None:
                        break

                    cursor.execute("INSERT INTO author(name) VALUES(?)", (author_,))
                    conn.commit()

                    author_id = cursor.lastrowid

                    # print(registration_query_args)
                    cursor.execute("INSERT INTO book_authors(book_fk_id, author_fk_id) VALUES(?, ?)", (book_id, author_id))

                conn.commit()
        else:
            print('Connection failed.')

    except Error as error:
        print(error)

    finally:
        if conn is not None and conn.is_connected():
            conn.close()
            print('Connection closed.')


if __name__ == '__main__':
    connect()
