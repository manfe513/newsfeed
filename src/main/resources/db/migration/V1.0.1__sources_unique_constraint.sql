ALTER TABLE sources
ADD CONSTRAINT unique_name_url UNIQUE (name, url);
