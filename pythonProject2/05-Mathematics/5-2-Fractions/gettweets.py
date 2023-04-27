import bs4
import requests

# get wikipedia information on elon musk
url = 'https://en.wikipedia.org/wiki/Elon_Musk'


def list_of_all_companies_of_elon_musk():
    # get the html from the url
    response = requests.get(url)
    # parse the html using beautiful soup and store in variable `soup`
    soup = bs4.BeautifulSoup(response.text, 'html.parser')
    # Take out the <div> of name and get its value
    name_box = soup.find('ul', attrs={'id': 'toc-Business_career-sublist'})
    # get all the links from the div
    links = name_box.find_all('a')
    # get the links that have the word company in them
    companies = [link.getText() for link in links]
    return companies


print(list_of_all_companies_of_elon_musk())
