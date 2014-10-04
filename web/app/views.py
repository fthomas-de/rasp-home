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

try:
	cs_a1 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	cs_a1.connect(('mylilraspi.raspctl.com', 1892))
	cs_a1.send('GETAS198273e924684c71c4c56d654fd01ae3bc714a18f50530f0992999bdb9c031992')
	a1 = cs_a1.recv(64)
	cs_a1.close()
	a1 = str(a1)
	states['a1'] = str(a1)

except:
	print 'error'

try:
	cs_b2 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	cs_b2.connect(('mylilraspi.raspctl.com', 1892))
	cs_b2.send('GETAS198273e924684c71c4c56d654fd01ae3bc714a18f50530f0992999bdb9c031992')
	b2 = cs_b2.recv(64)
	cs_b2.close()
	b2 = str(b2)
	states['b2'] = str(b2)
except:
	print 'error'

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
		return render_template('control.html', 
			page='control', 
			a1=str(states['a1']), 
			b2=str(states['b2']))

@app.route('/contact')
@auth.login_required
def contact():
	return render_template('contact.html', page='contact')

@app.route('/a1on')
@auth.login_required
def a1on():
	command = '/usr/bin/sudo /home/fthomas/Dokumente/rasp-home/server/send m 4 1 1'
	process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
	states['a1'] = True
	return redirect('/control', code=302)

@app.route('/a1off')
@auth.login_required
def a1off():
	command = '/usr/bin/sudo /home/fthomas/Dokumente/rasp-home/server/send m 4 1 0'
        process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
        states['a1'] = False
	return redirect('/control', code=302)

@app.route('/b2on')
@auth.login_required
def b2on():
	command = '/usr/bin/sudo /home/fthomas/Dokumente/rasp-home/server/send n 4 2 1'
        process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
        states['b2'] = True
	return redirect('/control', code=302)

@app.route('/b2off')
@auth.login_required
def b2off():
        command = '/usr/bin/sudo /home/fthomas/Dokumente/rasp-home/server/send n 4 2 0'
        process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
        states['b2'] = False
        return redirect('/control', code=302)

@app.route('/shutdown')
@auth.login_required
def shutdown():
        command = '/usr/bin/sudo /sbin/shutdown -r now'
	process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
	return redirect('/control')
