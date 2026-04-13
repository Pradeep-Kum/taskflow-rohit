DROP INDEX IF EXISTS idx_tasks_created_by_id;
ALTER TABLE tasks DROP COLUMN IF EXISTS created_by_id;
