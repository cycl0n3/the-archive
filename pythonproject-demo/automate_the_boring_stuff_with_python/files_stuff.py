import shutil
import os
import send2trash


os.chdir('/home/milind/Documents')
shutil.copy('Readme', 'Readme2')

#shutil.copytree('/home/milind/Downloads/mongodb-linux-x86_64-3.4.9', '/home/milind/Downloads/mongo-copy')

send2trash.send2trash('Readme2')
