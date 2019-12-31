import os
from shutil import copyfile


'''
os.mkdir('lwjgl-jars')
os.mkdir('lwjgl-sources')
os.mkdir('lwjgl-javadocs')
os.mkdir('lwjgl-natives-windows')
os.mkdir('lwjgl-natives-macos')
os.mkdir('lwjgl-natives-linux')
'''

copydests = ['lwjgl-jars','lwjgl-sources','lwjgl-javadocs','lwjgl-natives-windows','lwjgl-natives-macos','lwjgl-natives-linux']

for root,dirs,files in os.walk('./'):
    rname = root[2:]
    print(rname)
    if (rname in copydests):
        print("Skipped " + root)
        continue
    for f in files:
        if 'natives-windows' in f:
            copyfile(os.path.join(root, f), 'lwjgl-natives-windows/'+f)
        if 'sources' in f:
            copyfile(os.path.join(root, f), 'lwjgl-sources/'+f)
        if 'javadoc' in f:
            copyfile(os.path.join(root, f), 'lwjgl-javadocs/'+f)
        if rname+".jar" in f:
            copyfile(os.path.join(root, f), 'lwjgl-jars/'+f)
