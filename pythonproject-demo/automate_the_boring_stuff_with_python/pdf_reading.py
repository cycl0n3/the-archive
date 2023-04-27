import PyPDF2


pdfFileObj = open('data/meetingminutes.pdf', 'rb')
pdfReader = PyPDF2.PdfFileReader(pdfFileObj)

print(pdfReader.numPages)
pageObj = pdfReader.getPage(0)
print(pageObj.extractText())

print('-'*30)

pdfReader = PyPDF2.PdfFileReader(open('data/encrypted.pdf', 'rb'))
print(pdfReader.isEncrypted)
pdfReader.decrypt('rosebud')
pageObj = pdfReader.getPage(0)
print(pageObj.extractText())
