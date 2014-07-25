from flask import Flask, render_template, url_for, request, redirect 
from flask.ext.httpauth import HTTPBasicAuth
from app import app
from users import users
from forms import Ipaddress
import subprocess

(user, passwd) = users
userlist = {user : passwd}
auth = HTTPBasicAuth()
states = { 'a1' : False, 'b2' : False  }

@auth.get_password
def get_pw(username):
	if username in userlist:
		return userlist[username]
	return None

@app.route('/')
@app.route('/index')
@auth.login_required
def home():
	return render_template('index.html')

@app.route('/control')
@auth.login_required
def control():
	if request.method == 'POST':
		return 'Form posted'

	elif request.method == 'GET':
		form = Ipaddress()
		return render_template('control.html')

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
