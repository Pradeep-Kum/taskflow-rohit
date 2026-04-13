ALTER TABLE tasks ADD COLUMN created_by_id UUID REFERENCES users(id) ON DELETE SET NULL;

UPDATE tasks
SET created_by_id = assignee_id
WHERE created_by_id IS NULL;

CREATE INDEX idx_tasks_created_by_id ON tasks(created_by_id);
