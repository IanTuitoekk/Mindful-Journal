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
        
    @staticmethod
    def get_journal(journal_id):
        """Get a journal entry by ID"""
        journal = Journal.query.get(journal_id)
        if journal:
            return {
                'id': journal.id,
                'user_id': journal.user_id,
                'title': journal.title,
                'content': journal.content,
                'created_at': journal.created_at.strftime('%Y-%m-%d %H:%M:%S'),
                'updated_at': journal.updated_at.strftime('%Y-%m-%d %H:%M:%S')
            }, 200
        else:
            return {'message': 'Journal entry not found'}, 404
        
    @staticmethod
    def update_journal(journal_id, data):
        """Update a journal entry by ID"""
        journal = Journal.query.get(journal_id)
        if not journal:
            return {'message': 'Journal entry not found'}, 404
        
        try:
            title = data.get('title')
            content = data.get('content')
            
            if title:
                journal.title = title
            if content:
                journal.content = content
            
            db.session.commit()
            return {'message': 'Journal entry updated successfully'}, 200
        except Exception as e:
            db.session.rollback()
            return {'message': 'Error updating journal entry', 'error': str(e)}, 500
    
    @staticmethod
    def delete_journal(journal_id):
        """Delete a journal entry by ID"""
        journal = Journal.query.get(journal_id)
        if not journal:
            return {'message': 'Journal entry not found'}, 404
        
        try:
            db.session.delete(journal)
            db.session.commit()
            return {'message': 'Journal entry deleted successfully'}, 200
        except Exception as e:
            db.session.rollback()
            return {'message': 'Error deleting journal entry', 'error': str(e)}, 500
    
    @staticmethod
    def get_user_journals(user_id):
        """Get all journal entries for a user"""
        try:
            journals = Journal.query.filter_by(user_id=user_id).order_by(Journal.created_at.desc()).all()
            
            journal_list = []
            for journal in journals:
                journal_list.append({
                    'id': journal.id,
                    'user_id': journal.user_id,
                    'title': journal.title,
                    'content': journal.content,
                    'created_at': journal.created_at.strftime('%Y-%m-%d %H:%M:%S'),
                    'updated_at': journal.updated_at.strftime('%Y-%m-%d %H:%M:%S')
                })
            
            return {'journals': journal_list}, 200
        except Exception as e:
            return {'message': 'Error fetching journals', 'error': str(e)}, 500