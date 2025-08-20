create extension if not exists vector;

create table if not exists documents (
                                         id uuid primary key,
                                         tenant_id text not null default 'default',
                                         title text,
                                         uri text,
                                         version int default 1,
                                         created_at timestamptz default now()
    );

create table if not exists chunks (
                                      id uuid primary key,
                                      doc_id uuid not null references documents(id) on delete cascade,
    page int,
    ord int,
    text text not null,
    meta jsonb default '{}'::jsonb,
    embedding vector(1536)
    );

create index if not exists idx_chunks_embedding
    on chunks using ivfflat (embedding vector_cosine_ops) with (lists = 100);

create index if not exists idx_chunks_docid
    on chunks (doc_id);
