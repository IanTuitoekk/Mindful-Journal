from database import db
from models.users import User

class UserController:
    
    @staticmethod
    def register_user(data):
        """Register a new user"""
        try:
            # Check if data exists
            if not data:
                return {'error': 'No data provided'}, 400
            
            # Get fields
            username = data.get('username', '').strip()
            email = data.get('email', '').strip().lower()
            password = data.get('password', '')
            
            # Check required fields
            if not username:
                return {'error': 'Username is required'}, 400
            if not email:
                return {'error': 'Email is required'}, 400
            if not password:
                return {'error': 'Password is required'}, 400
            
            # Validate email (basic check)
            if '@' not in email or '.' not in email:
                return {'error': 'Invalid email format'}, 400
            
            # Validate password length
            if len(password) < 8:
                return {'error': 'Password must be at least 8 characters'}, 400
            
            # Check if username exists
            if User.query.filter_by(username=username).first():
                return {'error': 'Username already exists'}, 409
            
            # Check if email exists
            if User.query.filter_by(email=email).first():
                return {'error': 'Email already exists'}, 409
            
            # Create user
            new_user = User(
                username=username,
                email=email,
                password=password
            )
            
            db.session.add(new_user)
            db.session.commit()
            
            return {
                'message': 'User registered successfully',
                'user': {
                    'id': new_user.id,
                    'username': new_user.username,
                    'email': new_user.email
                }
            }, 201
            
        except Exception as e:
            db.session.rollback()
            return {'error': str(e)}, 500