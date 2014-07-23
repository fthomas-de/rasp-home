from flask.ext.wtf import Form
from wtforms import TextField, SubmitField


class Ipaddress(Form):
	ip = TextField('IP')
	submit = SubmitField('Send')

