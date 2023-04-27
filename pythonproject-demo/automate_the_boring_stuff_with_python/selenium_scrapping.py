from selenium import webdriver

browser = webdriver.Firefox()

browser.get('http://inventwithpython.com')

try:
    # elem = browser.find_element_by_class_name('bookcover')
    # print('Found <%s> element with that class name!' % elem.tag_name)
    linkElem = browser.find_element_by_link_text('Read It Online')
    linkElem.click()
except Exception as e:
    print('Exception: ' + e)
