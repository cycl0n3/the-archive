from bs4 import BeautifulSoup


css_soup = BeautifulSoup('<p class="body"></p>', 'html.parser')
print(css_soup)
print(css_soup.p['class'])
# ['body']

css_soup = BeautifulSoup('<p class="body strikeout"></p>', 'html.parser')
print(css_soup.p['class'])
# ['body', 'strikeout']

id_soup = BeautifulSoup('<p id="my id"></p>', 'html.parser')
print(id_soup.p['id'])
# 'my id'

rel_soup = BeautifulSoup('<p>Back to the <a rel="index">homepage</a></p>', 'html.parser')
print(rel_soup.a['rel'])
# ['index']
rel_soup.a['rel'] = ['index', 'contents']
print(rel_soup.p)
# <p>Back to the <a rel="index contents">homepage</a></p>

no_list_soup = BeautifulSoup('<p class="body strikeout"></p>', 'html.parser', multi_valued_attributes=None)
print(no_list_soup.p['class'])
# 'body strikeout'

print(id_soup.p.get_attribute_list('id'))
# ["my id"]

xml_soup = BeautifulSoup('<p class="body strikeout"></p>', 'xml')
print(xml_soup.p['class'])
# 'body strikeout'

class_is_multi = {'*': 'class'}
xml_soup = BeautifulSoup('<p class="body strikeout"></p>', 'xml', multi_valued_attributes=class_is_multi)
print(xml_soup.p['class'])
# ['body', 'strikeout']
