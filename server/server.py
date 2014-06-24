#!/usr/bin/python

import socket
import subprocess

def main():
	print 'Starting ...'
	HOST = '192.168.0.111'
	PORT = 1892
	LOG1LEN = 30
	LOG2LEN = 30 
	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
	s.bind((HOST, PORT))
	s.listen(2)
	data = ''
	print 'Listening...'
	state_1 = False
	state_2 = False
	state_3 = False
	
	while True:
		answer = ""
		conn, addr = s.accept()
		print 'Connected by', addr
		while True:
		    	data = conn.recv(16)
    			if not data: break			
			answer = data.rstrip()
	    		print 'Incoming: ' + answer	
			break 
			
		if answer == '000':
			if not state_1:
				command = '/usr/bin/sudo /home/fthomas/Dokumente/rasp-home/send m 4 1 1' #A1 = m 4 1 _
				print 'Dose 1 an'	
				state_1 = not state_1
			else:
				command = '/usr/bin/sudo /home/fthomas/Dokumente/rasp-home/send m 4 1 0' #A1 = m 4 1 _
				print 'Dose 1 an'
				state_1 = not state_1

			process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
			output = process.communicate()[0]
			print output
			
		elif answer == '001':
			if not state_2:
                                command = '/usr/bin/sudo /home/fthomas/Dokumente/rasp-home/send n 4 1 1' #B1 = n 4 1 _
                                print 'Dose 2 an'
                                state_2 = not state_2
                        else:
                                command = '/usr/bin/sudo /home/fthomas/Dokumente/rasp-home/send n 4 1 0' #B1 = n 4 1 _
                                print 'Dose 2 an'
                                state_2 = not state_2

                        process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
                        output = process.communicate()[0]
			print output

		elif answer == '010':
			if not state_3:
                                command = '/usr/bin/sudo /home/fthomas/Dokumente/rasp-home/send o 4 1 1' #C1 = o 4 1 _
                                print 'Dose 3 an'
                                state_3 = not state_3
                        else:
                                command = '/usr/bin/sudo /home/fthomas/Dokumente/rasp-home/send o 4 1 0' #C1 = o 4 1 _
                                print 'Dose 3 an'
                                state_3 = not state_3

                        process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
                        output = process.communicate()[0]
			print output

		elif answer == '100': #log1
			with open('/home/fthomas/reboot-log', 'r') as log1_file:
				log1 = log1_file.readlines()[-LOG1LEN:]
			data = data + ''.join(log1)
			log1_file.close()

		elif answer == '110': #log2
			with open('/home/fthomas/shutdown-log', 'r') as log2_file:
				log2 = log2_file.readlines()[-LOG2LEN:]
			data = data + ''.join(log2)
			log2_file.close()

		elif data.rstrip() == '111':
			print 'restart'
			command = '/usr/bin/sudo /sbin/shutdown -r now'
			process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
			output = process.communicate()[0]
			print output

		print 'Sending (data): ' , data		
		conn.sendall(data)
		conn.close()

if __name__ == "__main__":
	main()

