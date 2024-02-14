CREATE TABLE documents
(
    doc_id INT NOT NULL,
    doc_series SMALLINT NOT NULL,
    doc_num INT NOT NULL,
    original_flg BOOLEAN NOT NULL DEFAULT FALSE,
    employee_id INT NOT NULL
);