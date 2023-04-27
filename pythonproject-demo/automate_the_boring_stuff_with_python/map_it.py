import requests


res = requests.get('http://www.gutenberg.org/cache/epub/1112/pg1112.txt')

try:
    res.raise_for_status()

    playFile = open('data/RomeoAndJuliet.txt', 'wb')

    for chunk in res.iter_content(100000):
        playFile.write(chunk)

    print('File downloaded')
except Exception as exc:
    print('There was a problem: %s' % exc)
