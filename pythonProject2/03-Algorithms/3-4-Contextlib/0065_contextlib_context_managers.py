import contextlib


class Tracker(object):
    """Base class for noisy context managers."""
    def __init__(self, i):
        self.i = i

    def msg(self, s):
        print('  {}({}): {}'.format(
            self.__class__.__name__, self.i, s
        ))

    def __enter__(self):
        self.msg('entering')


