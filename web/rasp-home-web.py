from flask import Flask, render_template

app = Flask(__name__)

@app.route('/')
@app.route('/index')
def home():
	return render_template('index.html')

@app.route('/control')
def welcome():
	return render_template('control.html')

if __name__ == '__main__':
	app.run(host='0.0.0.0', port=5000, debug=False)