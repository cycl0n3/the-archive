import PyPDF2
import os

directory = '/run/media/milind/disk1/Books/Android'

for f in os.listdir(directory):
    if f.lower().endswith('.pdf') and not f.lower().endswith('_encrypted.pdf'):
        f_src = os.path.join(directory, f)
        pdfInputFile = open(f_src, 'rb')
        pdfReader = PyPDF2.PdfFileReader(pdfInputFile)

        filename, filename_ext = os.path.splitext(f_src)
        f_dst = os.path.join(directory, filename + '_encrypted' + filename_ext)
        pdfOutputFile = open(f_dst, 'wb')

        pdfWriter = PyPDF2.PdfFileWriter()

        print('encrypting: ', f_src)

        totalPages = pdfReader.numPages
        for page in range(totalPages):
            pdfWriter.addPage(pdfReader.getPage(page))

        pdfWriter.encrypt('password')
        pdfWriter.write(pdfOutputFile)

        pdfInputFile.close()
        pdfOutputFile.close()

