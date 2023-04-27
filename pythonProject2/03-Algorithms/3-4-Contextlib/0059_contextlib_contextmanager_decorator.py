import contextlib


@contextlib.contextmanager
def make_context():
    print('    entering')
    try:
        yield
    except RuntimeError as err:
        print('    ERROR:', err)
    finally:
        print('    exiting')


@make_context()
def normal():
    print('    inside with statement')


@make_context()
def throw_error(err):
    raise err


print('Normal:')
normal()

print('\nHandled error:')
throw_error(RuntimeError('showing example of handling an error'))

print('\nUnhandled error:')
throw_error(ValueError('this exception is not handled'))
