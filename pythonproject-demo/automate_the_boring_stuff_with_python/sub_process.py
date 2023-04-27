import subprocess


calcProc = subprocess.Popen('gnome-calculator')

print(calcProc.poll())
print(calcProc.wait())
print(calcProc.poll())
