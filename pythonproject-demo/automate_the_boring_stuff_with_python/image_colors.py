from PIL import ImageColor
from PIL import Image


print(ImageColor.getcolor('red', 'RGBA'))
print(ImageColor.getcolor('RED', 'RGBA'))
print(ImageColor.getcolor('Black', 'RGBA'))
print(ImageColor.getcolor('chocolate', 'RGBA'))
print(ImageColor.getcolor('CornflowerBlue', 'RGBA'))

print('-'*30)

catIm = Image.open('data/zophie.png')

print(catIm.size)
print(catIm.filename)
print(catIm.format)
print(catIm.format_description)

catIm.save('data/zophie.jpg')
