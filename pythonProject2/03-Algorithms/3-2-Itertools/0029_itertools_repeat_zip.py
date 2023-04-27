from itertools import *


for i, s in zip(count(), repeat('over-and-over', 5)):
    print(i, s)
