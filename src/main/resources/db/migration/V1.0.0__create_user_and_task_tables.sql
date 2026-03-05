-- Criação da tabela de usuários
CREATE TABLE tb_user (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Criação da tabela de tarefas
// TO DO

-- Índice para melhorar a performance da busca por user_id
CREATE INDEX idx_task_user_id ON tb_task(user_id);
