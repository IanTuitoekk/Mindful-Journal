import os

DATABASE_URL = os.getenv(
    "DATABASE_URL",
    "postgresql://postgres:work@localhost:5432/mindful_journal"
)