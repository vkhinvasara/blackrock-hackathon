from flask import Flask, jsonify
from dotenv import load_dotenv
import os
import requests

application = Flask(__name__)
load_dotenv()
# Replace 'your_news_api_key_here' with your actual News API key
NEWS_API_KEY = os.getenv('NEWS_API_KEY')
NEWS_API_URL = 'https://newsapi.org/v2/top-headlines?category=business&apiKey='

@application.route('/finance-news')
def get_finance_news():
    response = requests.get(f"{NEWS_API_URL}{NEWS_API_KEY}&language=en")
    if response.status_code == 200:
        news_data = response.json()
        articles = news_data.get('articles', [])
        return jsonify(articles)
    else:
        return jsonify({"error": "Failed to fetch news"}), 500

if __name__ == '__main__':
    application.run(debug=True)