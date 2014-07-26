import socket 

cs = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
cs.connect(('192.168.0.111', 1892))
cs.send('GETAS198273e924684c71c4c56d654fd01ae3bc714a18f50530f0992999bdb9c031992')
msg = cs.recv(64)
print msg
