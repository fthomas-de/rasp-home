#!/usr/bin/python

import socket
import subprocess
import users

def main():
	
	device = users.device
	
	HOST = '192.168.0.111'
	PORT = 1892
	LOG1LEN = 30 #currently sending some more lines than needed
	LOG2LEN = 30 
	
	a1 = False #device on or off
	b2 = False # ...

	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
	s.bind((HOST, PORT))
	s.listen(2)
	data = ''
	
	while True:
		#answer = 'cmd' + 'val' + 'hash'
		#length =  3    +  3    +  64
		cmd, val, hash = None, None, None

		conn, _ = s.accept()
		while True:
			data = conn.recv(512)
			data = data.rstrip()
			if not data or len(data) != 70: 
				print "data:", data
				print "wrong LEN"
				conn.close()
				break
					
			cmd = data[:3]
			val = data[3:6]
			hash = data[6:]
			print cmd, val, hash	
			data = 'Success'		
			break 

		if hash != device: 
			print "wrong ID"
			conn.close()
			continue

		if cmd == 'SET':
			if val == 'A11':
				command = '/usr/bin/sudo /home/fthomas/Dokumente/rasp-home/server/send m 4 1 1' #A1 = m 4 1 _
				process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
				a1 = True
			elif val == 'A10':
				command = '/usr/bin/sudo /home/fthomas/Dokumente/rasp-home/server/send m 4 1 0' #A1 = m 4 1 _
				process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
				#output = process.communicate()[0]
				#print output
				a1 = False

			elif val == 'B21':
		                command = '/usr/bin/sudo /home/fthomas/Dokumente/rasp-home/server/send n 4 2 1' #B2 = n 4 1 _
				process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
				b2 = True
			elif val == 'B20':
			        command = '/usr/bin/sudo /home/fthomas/Dokumente/rasp-home/server/send n 4 2 0' #B2 = n 4 1 _
       	                	process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
       	                	#output = process.communicate()[0]
				#print output
				b2 = False

			else: 
				data = 'Error: cmd unknown'

		elif cmd =='GET':
			if val == 'lg1': #log1
				with open('/home/fthomas/reboot-log', 'r') as log1_file:
					log1 = log1_file.readlines()[-LOG1LEN:]
					log1_file.close()
				data = val + ''.join(log1)

			elif val == 'lg2': #log2
				with open('/home/fthomas/shutdown-log', 'r') as log2_file:
					log2 = log2_file.readlines()[-LOG2LEN:]
					log2_file.close()	
				data = val + ''.join(log2)

			elif val == 'rst':
				command = '/usr/bin/sudo /sbin/shutdown -r now'
				process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
				#output = process.communicate()[0]
				#print output

			elif val == '101':
				data = 'Cookies'

			elif val == 'AS1':
				data = str(a1)
		
			elif val == 'BS2':
				data = str(b2)
				
			else:
				data = "Error: cmd unknown"
		else:
			data = 'Error: cmd unknown'
	
		print 'Sending (data): ' , data		
		conn.sendall(data)
		conn.close()

if __name__ == "__main__":
	main()

