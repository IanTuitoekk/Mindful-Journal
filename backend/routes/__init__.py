from flask import request, jsonify
from controllers.user_controller import UserController

def init_routes(app):
    """Initialize all routes"""
    
    @app.route('/')
    def index():
        return jsonify({'message': 'Mindful Journal API', 'status': 'running'}), 200
    
    @app.route('/users/register', methods=['POST'])
    def register():
        """Register a new user"""
        data = request.get_json()
        result, status_code = UserController.register_user(data)
        return jsonify(result), status_code