from bs4 import BeautifulSoup

doc = BeautifulSoup("<document><content/>INSERT FOOTER HERE</document>", "xml")
footer = BeautifulSoup("<footer>Here's the footer</footer>", "xml")

doc.find(text="INSERT FOOTER HERE").replace_with(footer)
# 'INSERT FOOTER HERE'

print(doc)
# <?xml version="1.0" encoding="utf-8"?>
# <document><content/><footer>Here's the footer</footer></document>
print(doc.name)
