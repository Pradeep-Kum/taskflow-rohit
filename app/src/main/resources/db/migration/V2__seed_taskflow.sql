INSERT INTO users (id, name, email, password_hash, created_at)
VALUES (
    '11111111-1111-1111-1111-111111111111',
    'Test User',
    'test@example.com',
    crypt('password123', gen_salt('bf', 12)),
    NOW()
);

INSERT INTO projects (id, name, description, owner_id, created_at)
VALUES (
    '22222222-2222-2222-2222-222222222222',
    'Website Redesign',
    'Seed project for reviewer testing',
    '11111111-1111-1111-1111-111111111111',
    NOW()
);

INSERT INTO tasks (id, title, description, status, priority, project_id, assignee_id, due_date, created_at, updated_at)
VALUES
    (
        '33333333-3333-3333-3333-333333333333',
        'Design homepage',
        'Create the first pass of the landing page',
        'todo',
        'high',
        '22222222-2222-2222-2222-222222222222',
        '11111111-1111-1111-1111-111111111111',
        '2026-04-15 00:00:00+00',
        NOW(),
        NOW()
    ),
    (
        '44444444-4444-4444-4444-444444444444',
        'Build API routes',
        'Wire project and task endpoints',
        'in_progress',
        'medium',
        '22222222-2222-2222-2222-222222222222',
        '11111111-1111-1111-1111-111111111111',
        '2026-04-20 00:00:00+00',
        NOW(),
        NOW()
    ),
    (
        '55555555-5555-5555-5555-555555555555',
        'Write README',
        'Document setup and API usage',
        'done',
        'low',
        '22222222-2222-2222-2222-222222222222',
        '11111111-1111-1111-1111-111111111111',
        NULL,
        NOW(),
        NOW()
    );
