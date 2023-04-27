import array
import binascii
import tempfile

a = array.array('i', range(5))
print('A1:', a)

out = tempfile.NamedTemporaryFile(dir='.\\00-Data', delete=False)
a.tofile(out)
out.flush()

print('Out:', out.name)

with open(out.name, 'rb') as inp:
    raw_data = inp.read()
    print('Raw contents:', binascii.hexlify(raw_data))

    inp.seek(0)
    a2 = array.array('i')
    a2.fromfile(inp, len(a))
    print('A1:', a)
