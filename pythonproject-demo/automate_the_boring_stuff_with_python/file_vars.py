import shelve


shelfFile = shelve.open('data/mydata')
cats = ['Zophie', 'Pooka', 'Simon']
shelfFile['cats'] = cats
shelfFile.close()

shelfFile = shelve.open('data/mydata')
print(type(shelfFile))
print(shelfFile['cats'])

print('-'*20)
