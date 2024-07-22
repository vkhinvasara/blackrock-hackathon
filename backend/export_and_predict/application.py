# from flask import Flask, request, jsonify
# import pandas as pd
# import pickle
# from sklearn.ensemble import GradientBoostingRegressor
# from datetime import timedelta
# from google.cloud import firestore
# from google.oauth2 import service_account
# from io import StringIO
# import os
# from dotenv import load_dotenv

# app = Flask(__name__)

# load_dotenv()

# # Load pre-trained base model
# with open('base_model.pkl', 'rb') as file:
#     base_model = pickle.load(file)

# # Firestore setup
# project_id = os.environ.get('GOOGLE_CLOUD_PROJECT')
# cred_path = os.environ.get('GOOGLE_APPLICATION_CREDENTIALS')
# credentials = service_account.Credentials.from_service_account_file(cred_path)
# db = firestore.Client(project=project_id, credentials=credentials)

# def preprocess_data(data):
#     # Basic preprocessing steps
#     data['Timestamp'] = pd.to_datetime(data['Timestamp'], unit='s')
#     data['Date'] = data['Timestamp'].dt.date
#     data['Date'] = pd.to_datetime(data['Date'])

#     data.drop(['Timestamp'], axis=1, inplace=True)
#     data.drop(data[data['Category'] == 'Salary'].index, inplace=True)

#     data['DayOfMonth'] = data['Date'].dt.day
#     data['DayOfWeek'] = data['Date'].dt.dayofweek

#     # Encode categorical features
#     category_columns = ['Category_Food', 'Category_Health', 'Category_Living', 'Category_Miscellaneous', 'Category_Transport']
#     for col in category_columns:
#         data[col] = (data['Category'] == col.split('_')[1]).astype(int)
    
#     data.drop('Category', axis=1, inplace=True)
#     return data

# @app.route('/export_and_predict/<user_email>', methods=['GET'])
# def export_and_predict(user_email):
#     try:
#         # Reference to the Health Expense collection
#         collections = ['Health Expense', 'Living Expense', 'Transport Expense', 'Salary', 'Miscellaneous']
#         output = []

#         for collection in collections:
#             docs = db.collection('userDetails').document(user_email).collection(collection).stream()
#             for doc in docs:
#                 output.append(doc.to_dict())

#         if not output:
#             return jsonify({"message": "No data found for the provided user email.", "status": 404}), 404

#         # Parse the JSON data
#         df = pd.DataFrame(output)
#         # Select the required columns and rename them
#         df = df[['smsAmount', 'smsTimeStamp', 'smsCategory']]
#         df.columns = ['Amount', 'Timestamp', 'Category']

#         # Save DataFrame to CSV in memory
#         buffer = StringIO()
#         df.to_csv(buffer, index=False)
#         buffer.seek(0)

#         # Read CSV from buffer and preprocess
#         user_data = pd.read_csv(buffer)
#         user_data = preprocess_data(user_data)

#         # Prepare to retrain models
#         categories = [col for col in user_data.columns if col.startswith('Category_')]
#         category_models = base_model.copy()  # Use the base model as a starting point

#         for category in categories:
#             model = category_models[category]
#             category_data = user_data[user_data[category] == 1]
#             X_cat = category_data.drop(['Amount', 'Date'], axis=1)
#             y_cat = category_data['Amount']
#             model.fit(X_cat, y_cat)
#             category_models[category] = model

#         # Predict next month's expenses for each category
#         next_month = user_data['Date'].max() + timedelta(days=30)
#         next_month_data = user_data[user_data['Date'] < next_month]

#         predicted_expenses = {}
#         for category, model in category_models.items():
#             next_month_X = next_month_data[next_month_data[category] == 1].drop(['Amount', 'Date'], axis=1)
#             predicted_expenses[category] = model.predict(next_month_X).sum()

#         return jsonify({
#             "message": "Models retrained and next month's expenses predicted successfully!",
#             "predicted_expenses": predicted_expenses
#         })

#     except Exception as e:
#         return f"An Error Occurred: {e}"

# if __name__ == '__main__':
#     app.run(debug=True)

from flask import Flask, request, jsonify
import pandas as pd
import pickle
from sklearn.ensemble import GradientBoostingRegressor
from datetime import timedelta
from google.cloud import firestore
from google.oauth2 import service_account
from io import StringIO
import os
from dotenv import load_dotenv

application = Flask(__name__)

load_dotenv()

# Load pre-trained base model
with open('base_model.pkl', 'rb') as file:
    base_model = pickle.load(file)

# Firestore setup
project_id = os.environ.get('GOOGLE_CLOUD_PROJECT')
cred_path = os.environ.get('GOOGLE_APPLICATION_CREDENTIALS')
credentials = service_account.Credentials.from_service_account_file(cred_path)
db = firestore.Client(project=project_id, credentials=credentials)

def preprocess_data(data):
    # Convert the timestamp to datetime, handling any errors
    data['Timestamp'] = pd.to_datetime(data['Timestamp'], unit='ms', errors='coerce')
    data.dropna(subset=['Timestamp'], inplace=True)  # Drop rows where timestamp conversion failed
    
    data['Date'] = data['Timestamp'].dt.date
    data['Date'] = pd.to_datetime(data['Date'])

    data.drop(['Timestamp'], axis=1, inplace=True)
    data.drop(data[data['Category'] == 'Salary'].index, inplace=True)

    data['DayOfMonth'] = data['Date'].dt.day
    data['DayOfWeek'] = data['Date'].dt.dayofweek

    # Encode categorical features
    category_columns = ['Category_Food', 'Category_Health', 'Category_Living', 'Category_Miscellaneous', 'Category_Transport']
    for col in category_columns:
        data[col] = (data['Category'] == col.split('_')[1]).astype(int)
    
    data.drop('Category', axis=1, inplace=True)
    return data

@application.route('/export_and_predict/<user_email>', methods=['GET'])
def export_and_predict(user_email):
    try:
        # Reference to the Health Expense collection
        collections = ['Health Expense', 'Living Expense', 'Transport Expense', 'Salary', 'Miscellaneous','food Expense']
        output = []

        for collection in collections:
            docs = db.collection('userDetails').document(user_email).collection(collection).stream()
            for doc in docs:
                output.append(doc.to_dict())

        if not output:
            return jsonify({"message": "No data found for the provided user email.", "status": 404}), 404

        # Parse the JSON data
        df = pd.DataFrame(output)
        # Select the required columns and rename them
        df = df[['smsAmount', 'smsTimeStamp', 'smsCategory']]
        df.columns = ['Amount', 'Timestamp', 'Category']

        # Save DataFrame to CSV in memory
        buffer = StringIO()
        df.to_csv(buffer, index=False)
        buffer.seek(0)
        print(buffer.read())
		
        # Read CSV from buffer and preprocess
        user_data = pd.read_csv("student_transactions.csv")
        user_data = preprocess_data(user_data)

        # Prepare to retrain models
        categories = [col for col in user_data.columns if col.startswith('Category_')]
        category_models = base_model.copy()  # Use the base model as a starting point

        for category in categories:
            model = category_models[category]
            category_data = user_data[user_data[category] == 1]
            X_cat = category_data.drop(['Amount', 'Date'], axis=1)
            y_cat = category_data['Amount']
            model.fit(X_cat, y_cat)
            category_models[category] = model

        # Predict next month's expenses for each category
        next_month = user_data['Date'].max() + timedelta(days=30)
        next_month_data = user_data[user_data['Date'] < next_month]

        predicted_expenses = {}
        for category, model in category_models.items():
            next_month_X = next_month_data[next_month_data[category] == 1].drop(['Amount', 'Date'], axis=1)
            predicted_expenses[category] = model.predict(next_month_X).sum()

        return jsonify({
            "message": "Models retrained and next month's expenses predicted successfully!",
            "predicted_expenses": predicted_expenses
        })

    except Exception as e:
        return f"An Error Occurred: {e}"

if __name__ == '__main__':
    application.run(debug=True)
