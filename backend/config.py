import os

DATABASE_URL = os.getenv(
    "DATABASE_URL",
    "postgresql://iantu@localhost:5432/mindful_journal"
)