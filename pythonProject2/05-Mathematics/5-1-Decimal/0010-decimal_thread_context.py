import decimal
import threading
from queue import PriorityQueue


class Multiplier(threading.Thread):
    def __init__(self, a, b, prec, q):
        self.a = a
        self.b = b
        self.prec = prec
        self.q = q
        threading.Thread.__init__(self)

    def run(self) -> None:
        # c = decimal.getcontext().copy()
        # c.prec = self.prec
        # decimal.setcontext(c)
        decimal.getcontext().prec = self.prec
        self.q.put((self.prec, self.a * self.b))


x = decimal.Decimal('3.14')
y = decimal.Decimal('1.234')

# A PriorityQueue will return values sorted by precision,
# no matter in which order the threads finish.

qu = PriorityQueue()
threads = [Multiplier(x, y, i, qu) for i in range(1, 6)]

for t in threads:
    t.start()

for t in threads:
    t.join()

for i in range(5):
    prec, value = qu.get()
    print('{} {}'.format(prec, value))
