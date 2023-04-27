import csv


exampleFile = open('data/example.csv')
exampleReader = csv.reader(exampleFile)
exampleData = list(exampleReader)

print(exampleData)

exampleFile = open('data/example.csv')
exampleReader = csv.reader(exampleFile)

for row in exampleReader:
    print('Row #' + str(exampleReader.line_num) + ' ' + str(row))
