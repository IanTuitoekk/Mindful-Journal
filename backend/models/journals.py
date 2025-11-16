from database import db
from dataclasses import dataclass

@dataclass
class JournalType:
    id: int
    user_id: int
    title: str
    content: str
    mood: str  # Add this line
    created_at: str
    updated_at: str

class Journal(db.Model):
    __tablename__ = 'journals'

    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('users.id'), nullable=False)
    title = db.Column(db.String(200), nullable=False)
    content = db.Column(db.Text, nullable=False)
    mood = db.Column(db.String(50), nullable=True)  # Add this line
    created_at = db.Column(db.DateTime, server_default=db.func.now())
    updated_at = db.Column(db.DateTime, server_default=db.func.now(), onupdate=db.func.now())

    user = db.relationship('User', backref=db.backref('journals', lazy=True))

    def __repr__(self):
        return f'<Journal {self.title} by User {self.user_id}>'