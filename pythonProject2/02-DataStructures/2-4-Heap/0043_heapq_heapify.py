import heapq

from data.heapq_showtree import show_tree
from data.heapq_heapdata import data

print('random:', data)
heapq.heapify(data)

print('heapified :')
show_tree(data)
