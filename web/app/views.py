from flask import Flask, render_template, url_for 
from flask.ext.httpauth import HTTPBasicAuth
from app import app
from users import users

(user, passwd) = users
userlist = {user : passwd}
auth = HTTPBasicAuth()

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
def welcome():
	return render_template('control.html')