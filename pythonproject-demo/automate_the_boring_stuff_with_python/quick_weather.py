import json
import requests
import sys


url = 'https://jsonplaceholder.typicode.com/users'
response = requests.get(url)
response.raise_for_status()

data = json.loads(response.text)
print(data)

for d in data:
    print(d['name'] + ', ' + d['email'])
