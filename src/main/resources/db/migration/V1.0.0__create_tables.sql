create table if not exists feed
(
    id          serial      not null primary key,
    date        timestamptz not null,
    source      varchar(50) not null,
    title       text        not null,
    description text        not null
);
comment on table feed is 'Новости';

create index if not exists feed_date on feed (date);

CREATE TABLE shedlock
(
    name       VARCHAR(64)  NOT NULL,
    lock_until TIMESTAMP    NOT NULL,
    locked_at  TIMESTAMP    NOT NULL,
    locked_by  VARCHAR(255) NOT NULL,

    PRIMARY KEY (name)
);

-- Create a GIN index for full-text search
create index IF NOT EXISTS idx_full_text_search
    ON feed
        USING GIN (to_tsvector('russian', title || ' ' || description));


CREATE TABLE IF NOT EXISTS sources
(
    id       serial       not null primary key,
    name     varchar(100) not null,
    url      varchar(200) not null,
    rules_id int4         not null
);
comment on table sources is 'Источники новостей';

CREATE TABLE IF NOT EXISTS source_rules
(
    id              serial       not null primary key,
    item_path        varchar(100) not null,
    title_path       varchar(100) not null,
    description_path varchar(100) not null,
    date_path        varchar(100) not null,
    date_format     varchar(100) not null
);
comment on table source_rules is 'Правила парсинга полей для источников новостей';