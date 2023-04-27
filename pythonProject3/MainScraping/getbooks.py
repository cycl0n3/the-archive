import time
import requests

from bs4 import BeautifulSoup
from pathlib import Path


def fetch(url):
    r = requests.get(url)
    return r.content


for i in range(1, 10):
    URL = f'https://libgen.is/search.php?&res=100&req=topicid87&phrase=1&view=simple&column=def&sort=def&sortmode=ASC&page={i}'
    path_to_file = f'.\\data\\page{i}.html'
    path = Path(path_to_file)
    if not path.is_file():
        contents = fetch(URL)
        path.write_bytes(contents)
        time.sleep(1.0)
        print(f'Page {i} is complete')
    else:
        print(f'Page {i} is already downloaded')
