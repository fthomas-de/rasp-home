from flask import Flask, render_template, url_for, request, redirect 
from flask.ext.httpauth import HTTPBasicAuth
from app import app
from users import users
from forms import Ipaddress
import subprocess
import socket

(user, passwd) = users
userlist = {user : passwd}
auth = HTTPBasicAuth()
states = { 'a1' : False, 'b2' : False  }

cs_a1 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
cs_a1.connect(('192.168.0.111', 1892))
cs_a1.send('GETAS198273e924684c71c4c56d654fd01ae3bc714a18f50530f0992999bdb9c031992')
a1 = cs_a1.recv(64)
cs_a1.close()

cs_b2 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
cs_b2.connect(('192.168.0.111', 1892))
cs_b2.send('GETAS198273e924684c71c4c56d654fd01ae3bc714a18f50530f0992999bdb9c031992')
b2 = cs_b2.recv(64)
cs_b2.close()

a1 = str(a1)
b2 = str(b2)

states['a1'] = str(a1)
states['b2'] = str(b2)

@auth.get_password
def get_pw(username):
	if username in userlist:
		return userlist[username]
	return None

@app.route('/')
@app.route('/index')
@auth.login_required
def home():
	return render_template('index.html', page='index')

@app.route('/control')
@auth.login_required
def control():
	if request.method == 'POST':
		return 'Form posted'

	elif request.method == 'GET':
		form = Ipaddress()
		return render_template('control.html', page='control')

@app.route('/contact')
@auth.login_required
def contact():
	return render_template('contact.html', page='contact')

@app.route('/a1on')
@auth.login_required
def a1on():
	if states['a1']:
		command = '/usr/bin/sudo /home/fthomas/Dokumente/rasp-home/server/send m 4 1 0'
		process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
		states['a1'] = False
	else:
		command = '/usr/bin/sudo /home/fthomas/Dokumente/rasp-home/server/send m 4 1 1'
                process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
		states['a1'] = True

	return redirect('/control', code=302)

@app.route('/b2on')
@auth.login_required
def b2on():
        if states['b2']:
                command = '/usr/bin/sudo /home/fthomas/Dokumente/rasp-home/server/send n 4 2 0'
                process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
                states['b2'] = False
        else:
                command = '/usr/bin/sudo /home/fthomas/Dokumente/rasp-home/server/send n 4 2 1'
                process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
                states['b2'] = True

        return redirect('/control', code=302)




