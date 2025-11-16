import requests
import json

url = "http://127.0.0.1:5000/journals/create"
data = {
    "user_id": 1,
    "title": "Test Entry",
    "content": "This is a test"
}

response = requests.post(url, json=data)
print(f"Status Code: {response.status_code}")
print(f"Response: {response.text}")