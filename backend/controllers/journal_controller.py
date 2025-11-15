from database import db
from models.journals import Journal

class JournalController:
    @staticmethod
    def create_journal(data):
        """Create a new journal entry"""
        try:
            user_id = data.get('user_id')
            title = data.get('title')
            content = data.get('content')
            
            if not all([user_id, title, content]):
                return {'message': 'Missing required fields'}, 400
            
            new_journal = Journal(user_id=user_id, title=title, content=content)
            db.session.add(new_journal)
            db.session.commit()
            
            return {'message': 'Journal entry created successfully', 'journal_id': new_journal.id}, 201
        except Exception as e:
            db.session.rollback()
            return {'message': 'Error creating journal entry', 'error': str(e)}, 500