CREATE TABLE IF NOT EXISTS task (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    due_date DATE,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT fk_task_user FOREIGN KEY (user_id) REFERENCES user_auth (id) ON DELETE CASCADE
); 