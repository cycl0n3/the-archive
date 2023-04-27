import time


print('The time is:', time.ctime())
later = time.time() + 15
print('The time 15 seconds later is:', time.ctime(later))
