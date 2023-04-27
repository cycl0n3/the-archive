import contextlib


class Context(contextlib.ContextDecorator):
    def __init__(self, how_used):
        self.how_used = how_used
        print('__init__()'.format(how_used))

    def __enter__(self):
        print('__enter__({})'.format(self.how_used))
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        print('__exit__({})'.format(self.how_used))


@Context('as a decorator')
def greetings(msg):
    print(msg)


with Context('as context manager'):
    print('Doing work in the context')

greetings('Doing work in the wrapped function')
