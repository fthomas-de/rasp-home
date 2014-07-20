from flask import Flask, render_template
from app import app

@app.route('/')
@app.route('/index')
def home():
	return render_template('index.html')

@app.route('/control')
def welcome():
	return render_template('control.html')
