import collections
import threading
import time

candle = collections.deque(range(10))


def burn(direction, next_source):
    while True:
        try:
            nxt = next_source()
        except IndexError as e:
            # print(e)
            break
        else:
            # print('else')
            print('{:>8}: {}'.format(direction, nxt))
            time.sleep(0.1)

    print('{:>8} done'.format(direction))
    return


left = threading.Thread(target=burn, args=('Left', candle.popleft))
right = threading.Thread(target=burn, args=('Right', candle.pop))

left.start()
right.start()

left.join()
right.join()
