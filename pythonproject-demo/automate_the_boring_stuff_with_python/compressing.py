import zipfile
import os


os.chdir('/home/milind/PycharmProjects/pythonproject')
exampleZip = zipfile.ZipFile('data.zip')
print(exampleZip.namelist())

baconInfo = exampleZip.getinfo('data/bacon.txt')
print(baconInfo.file_size)
print(baconInfo.compress_size)

exampleZip.close()
