CREATE TABLE IF NOT EXISTS "groove" (
    state VARCHAR(10),
    habit_id INTEGER REFERENCES public.habit(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    owner_id INTEGER REFERENCES public.user(id) ON DELETE CASCADE,
    PRIMARY KEY (habit_id, date, owner_id)
);
