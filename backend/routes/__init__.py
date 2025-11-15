from flask import request, jsonify
from controllers.user_controller import UserController
from controllers.journal_controller import JournalController

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
    
    @app.route('/users/login', methods=['POST'])
    def login():
        """Login a user"""
        data = request.get_json()
        result, status_code = UserController.login_user(data)
        return jsonify(result), status_code
    
    @app.route('/journals/create', methods=['POST'] )
    def create_journal():
        """Create a new journal entry"""
        data = request.get_json()
        result, status_code = JournalController.create_journal(data)
        return jsonify(result), status_code
    
    @app.route('/journals/read/<int:journal_id>', methods=['GET'])
    def get_journal(journal_id):
        """Get a journal entry by ID"""
        result, status_code = JournalController.get_journal(journal_id)
        return jsonify(result), status_code
    
    @app.route('/journals/update/<int:journal_id>', methods=['PUT'])
    def update_journal(journal_id):       
        """Update a journal entry by ID"""
        data = request.get_json()
        result, status_code = JournalController.update_journal(journal_id, data)
        return jsonify(result), status_code
    
    @app.route('/journals/delete/<int:journal_id>', methods=['DELETE'])
    def delete_journal(journal_id):       
        """Delete a journal entry by ID"""
        result, status_code = JournalController.delete_journal(journal_id)
        return jsonify(result), status_code