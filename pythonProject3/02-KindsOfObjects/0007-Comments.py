from bs4 import BeautifulSoup, CData

markup = "<b><!--Hey, buddy. Want to buy a used parser?--></b>"

soup = BeautifulSoup(markup, 'html.parser')
comment = soup.b.string

print(type(comment))
# <class 'bs4.element.Comment'>

print(comment)
# 'Hey, buddy. Want to buy a used parser'

print(soup.b.prettify())
# <b>
#  <!--Hey, buddy. Want to buy a used parser?-->
# </b>

cdata = CData("A CDATA block")
comment.replace_with(cdata)

print(soup.b.prettify())
# <b>
#  <![CDATA[A CDATA block]]>
# </b>
