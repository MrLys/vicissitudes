CREATE TABLE IF NOT EXISTS "groove" (
    id BIGSERIAL PRIMARY KEY,
    state VARCHAR(10),
    habit_id INTEGER REFERENCES public.user_habit(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    owner_id INTEGER REFERENCES public.user(id) ON DELETE CASCADE,
    CONSTRAINT unq_habit_date_owner UNIQUE (habit_id, date, owner_id)
);
