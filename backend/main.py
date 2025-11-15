from flask import Flask
from flask_migrate import Migrate
from database import db
from config import DATABASE_URL

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = DATABASE_URL
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

# Initialize database
db.init_app(app)

# Initialize Flask-Migrate
migrate = Migrate(app, db)

# Import models
from models.users import User
from models.journals import Journal

# Initialize routes
from routes import init_routes
init_routes(app)

if __name__ == '__main__':
    app.run(debug=True,port=5000)