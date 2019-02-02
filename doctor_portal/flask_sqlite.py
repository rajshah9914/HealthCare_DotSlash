from flask import Flask, render_template, request, Markup
import sqlite3 as sql
from flask import flash, redirect, session, abort
from datetime import date, datetime
app = Flask(__name__)
app.secret_key = "super secret key"

@app.route('/')
def home():
   if not session.get('logged_in'):
      return render_template('login.html')
   else:
      return render_template('home.html')

@app.route('/login', methods=['POST'])
def do_admin_login():
   usr = request.form['username']
   pwd = request.form['password']
   conn = sql.connect("admin.db")
   cur = conn.cursor()
   cur.execute("SELECT password FROM doctors")
 
   rows = cur.fetchall()
 
   for row in rows:
      pswd=row[0]
      print(pswd)
   conn.close()


   #print(pswd,pwd)
   if pwd==pswd:
      session['logged_in'] = True
   else:
      flash('wrong password!')
   return home()
 
@app.route("/logout")
def logout():
   session['logged_in'] = False
   return home()


@app.route('/plogin')
def phome():
   if not session.get('logged_in'):
      return render_template('login2.html')
   else:
      return plist()
   
@app.route('/login2', methods=['POST'])
def do_patient_login():
   usr = request.form['username']
   pwd = request.form['password']
   conn = sql.connect("patient.db")
   cur = conn.cursor()
   cur.execute("SELECT password FROM patients where pid=?",(usr,))
 
   rows = cur.fetchall()
 
   for row in rows:
      pswd=row[0]
      print(pswd)
   conn.close()


   #print(pswd,pwd)
   if pwd==pswd:
      session['logged_in'] = usr
   else:
      flash('wrong password!')
   return phome()
 

@app.route('/enternew')
def new_student():
   if not session.get('logged_in'):
      return render_template('login.html')
   return render_template('student.html')


@app.route('/faq')
def faq():
   if not session.get('logged_in'):
      return render_template('login.html')
   return render_template('doctq.html')

@app.route('/findp')
def find_student():
   if not session.get('logged_in'):
      return render_template('login.html')
   return render_template('find.html')

@app.route('/addrec',methods = ['POST', 'GET'])
def addrec():
   if not session.get('logged_in'):
      return render_template('login.html')
   if request.method == 'POST':
      try:
         i = request.form['id']
         p = request.form['psswd']
         n = request.form['nme']
         h = request.form['hlth']
         today = date.today()
         me = request.form['medicine']
         v = request.form['val']
         
         with sql.connect("patient.db") as con:
            cur = con.cursor()
            
            cur.execute("INSERT INTO patients (pid,password,pname,visit,health,medication,value) VALUES (?,?,?,?,?,?,?)",(i,p,n,today,h,me,v) )
            
            con.commit()
            msg = "Record successfully added"
      except:
         con.rollback()
         msg = "error in insert operation"
      
      finally:
         return render_template("result.html",msg = msg)
         con.close()



@app.route('/addque',methods = ['POST', 'GET'])
def addque():
   if not session.get('logged_in'):
      return render_template('login.html')
   if request.method == 'POST':
      try:
         qu = request.form['ques']
         an = request.form['ans']
         tp = request.form['typ']
         ch = request.form['choose_one']
         if(ch=='Orthopedics/ Physiotherapy'):
            url= 'http://chahalacademyexpenditures.hostingerapp.com/addhealth.php?questions='+qu+'&answers='+an+'&category='+tp
            print(url)
         else:
            url= 'http://chahalacademyexpenditures.hostingerapp.com/addhealthnut.php?questions='+qu+'&answers='+an+'&category='+tp
            print(url)
      except:
         print("Error")
      
      finally:
         url2='http://www.google.com'
         return redirect(url, code=302)

         



@app.route('/find',methods = ['POST', 'GET'])
def find():
   if not session.get('logged_in'):
      return render_template('login.html')
   nm = request.form['nm']
   con = sql.connect("patient.db")
   con.row_factory = sql.Row
   
   cur = con.cursor()
   cur.execute("select * from patients where pid=?",(nm,))
   
   rows = cur.fetchall();
   return render_template("list2.html",rows = rows)


@app.route('/find2',methods = ['POST', 'GET'])
def find2():
   if not session.get('logged_in'):
      return render_template('login.html')
   nm=session.get('logged_in')
   con = sql.connect("patient.db")
   con.row_factory = sql.Row
   
   cur = con.cursor()
   cur.execute("select visit from patients where pid=?",(nm,))
   
   rows = cur.fetchall();
   labels=[]
   for row in rows:
      labels.append(row[0])
   cur = con.cursor()
   cur.execute("select value from patients where pid=?",(nm,))
   
   rows = cur.fetchall();
   values=[]
   for row in rows:
      values.append(row[0]) 
   print(values)
   print(labels)
   return render_template("chart.html",values=values, labels=labels)


@app.route('/list')
def list():
   if not session.get('logged_in'):
      return render_template('login.html')
   con = sql.connect("patient.db")
   con.row_factory = sql.Row
   
   cur = con.cursor()
   cur.execute("select * from patients ")
   
   rows = cur.fetchall();
   return render_template("list2.html",rows = rows)

@app.route('/plist')
def plist():
   if not session.get('logged_in'):
      return render_template('login.html')
   m=session.get('logged_in')
   con = sql.connect("patient.db")
   con.row_factory = sql.Row
   
   cur = con.cursor()
   cur.execute("select * from patients where pid=?",(m,))
   
   rows = cur.fetchall();
   return render_template("list.html",rows = rows)

if __name__ == '__main__':
   app.run(debug = True,host= '0.0.0.0')
