import functools


@functools.singledispatch
def myfunc(arg):
    print('default myfunc({!r})'.format(arg))


@myfunc.register(int)
def myfunc_int(arg):
    print('myfunc_int({!r})'.format(arg))


@myfunc.register(list)
def myfunc_list(arg):
    print('myfunc_list({!r})'.format(arg))


myfunc('string argument')
myfunc(1)
myfunc(2.3)
myfunc(['a', 'b', 'c'])
