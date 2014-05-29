#!/usr/bin/python           # This is server.py file

import socket               # Import socket module

def main():
	HOST = '192.168.0.111'                 # Symbolic name meaning all available interfaces
	PORT = 1892 		               # Arbitrary non-privileged port
	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.bind((HOST, PORT))
	s.listen(1)
	while True:
		conn, addr = s.accept()
		print 'Connected by', addr
		while True:
		    	data = conn.recv(1024)
    			if not data: break
			print data
    			conn.sendall(data)
	conn.close()

if __name__ == "__main__":
	main()

