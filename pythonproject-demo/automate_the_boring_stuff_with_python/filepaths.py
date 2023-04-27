import os

print('-'*20)

print(os.path.abspath('.'))
print(os.path.isabs('.'))
print(os.path.isabs(os.path.abspath('.')))
print(os.getcwd())

print('-'*20)

curl_path = '/usr/bin/curl'
print(os.path.basename(curl_path))
print(os.path.dirname(curl_path))
print(os.path.split(curl_path))
print(curl_path.split(os.path.sep))
print(os.path.getsize(curl_path))
documents_path = '/home/milind/Documents'
print(os.listdir(documents_path))

totalSize = 0
for filename in os.listdir(documents_path):
    totalSize += os.path.getsize(os.path.join(documents_path, filename))
print(totalSize)

print('-'*20)

helloFile = open('data/hello.txt', 'r')
helloContent = helloFile.read()
print(helloContent)
helloFile.close()

sonnetFile = open('data/sonnet29.txt')
# print(sonnetFile.readlines())
print([line[:-1] for line in sonnetFile])
sonnetFile.close()

print('-'*20)

baconFile = open('data/bacon.txt', 'w')
baconFile.write('Hello World!\n')
baconFile.close()

baconFile = open('data/bacon.txt', 'a')
baconFile.write('Bacon is not a vegetable.')
baconFile.close()

baconFile = open('data/bacon.txt')
content = baconFile.read()
print(content)
