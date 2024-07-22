import csv
from io import StringIO  
from flask import Flask, json, jsonify, make_response, request
from google.cloud import firestore
from dotenv import load_dotenv
from google.oauth2 import service_account
import os
import pandas as pd
load_dotenv()

app = Flask(__name__)
project_id = os.environ.get('GOOGLE_CLOUD_PROJECT') 
cred_path = os.environ.get('GOOGLE_APPLICATION_CREDENTIALS')

credentials = service_account.Credentials.from_service_account_file(cred_path)

db = firestore.Client(project=project_id, credentials=credentials)




@app.route('/export_csv/<user_email>', methods=['GET'])
def export_csv(user_email):
    try:
        # Reference to the Health Expense collection
        health_expense_ref = db.collection('userDetails').document(user_email).collection('Health Expense')
        living_expese_ref = db.collection('userDetails').document(user_email).collection('Living Expense')
        transport_expense_ref = db.collection('userDetails').document(user_email).collection('Transport Expense')
        salary_ref = db.collection('userDetails').document(user_email).collection('Salary')
        miscellaneous_ref = db.collection('userDetails').document(user_email).collection('Miscellaneous')
        
        # Get all documents from the Health Expense collection
        
        health_docs = health_expense_ref.stream()
        living_expese_ref = living_expese_ref.stream()
        transport_expense_ref = transport_expense_ref.stream()
        salary_ref = salary_ref.stream()
        miscellaneous_ref = miscellaneous_ref.stream()
        

        # List to store all documents data
        output = []
        for doc in health_docs:
            output.append(doc.to_dict())
        for doc in living_expese_ref:
            output.append(doc.to_dict())
        for doc in transport_expense_ref:
            output.append(doc.to_dict())
        for doc in salary_ref:
            output.append(doc.to_dict())
    
            # Parse the JSON data
        df = pd.DataFrame(output)
        # Select the required columns and rename them
        df = df[['smsAmount', 'smsTimeStamp', 'smsCategory']]
        df.columns = ['Amount', 'Timestamp', 'Category']
        
        buffer = StringIO()
        df.to_csv(buffer, index=False)
        csv_data = buffer.getvalue()
        csv_file_path = 'data.csv'
        df.to_csv(csv_file_path, index=False)
        return csv_data
    except Exception as e:
        return f"An Error Occurred: {e}"
