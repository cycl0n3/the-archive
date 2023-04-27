from itertools import *
from pprint import *


FACE_CARDS = ('J', 'Q', 'K', 'A')
SUITS = ('H', 'D', 'C', 'S')

DECK = list(
    product(
        SUITS, chain(range(2, 11), FACE_CARDS)
    )
)

for card in DECK:
    print('{:>2}{}'.format(*card), end=' ')
    if card[1] == FACE_CARDS[-1]:
        print()
