import contextlib


@contextlib.contextmanager
def make_context():
    print('  entering')
    try:
        yield {}
    except RuntimeError as err:
        print('  ERROR:', err)
    finally:
        print('  exiting')


print('Normal:')
with make_context() as v:
    print('    inside with statement:', v)

print('\nHandle Error:')
with make_context() as v:
    raise RuntimeError('showing example of handling an error')

print('\nUnhandled Error:')
with make_context() as v:
    raise ValueError('this exception is not handled')
