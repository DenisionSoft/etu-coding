CREATE TABLE EI (
    id_ei SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE ProductClassifier (
    id_class SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    base_ei INTEGER NOT NULL,
    FOREIGN KEY (base_ei) REFERENCES EI(id_ei) ON DELETE CASCADE ON UPDATE CASCADE,
    parent_id INTEGER,
    FOREIGN KEY (parent_id) REFERENCES ProductClassifier(id_class) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Product (
    id_product SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    id_class INTEGER NOT NULL,
    FOREIGN KEY (id_class) REFERENCES ProductClassifier(id_class) ON DELETE CASCADE ON UPDATE CASCADE
);

SELECT ins_ei('Штука');
SELECT ins_ei('Пара');

SELECT ins_pc('Изделие', 1, NULL);
SELECT ins_pc('Одежда', 1, 1);
SELECT ins_pc('Женская одежда', 1, 2);
SELECT ins_pc('Верхняя одежда', 1, 3);
SELECT ins_pc('Топы', 1, 3);
SELECT ins_pc('Брюки', 1, 3);
SELECT ins_pc('Платья', 1, 3);
SELECT ins_pc('Кожаные куртки', 1, 4);
SELECT ins_pc('Пуховики', 1, 4);
SELECT ins_pc('Ветровки', 1, 4);
SELECT ins_pc('Футболки', 1, 5);
SELECT ins_pc('Майки', 1, 5);
SELECT ins_pc('Спортивные брюки', 1, 6);
SELECT ins_pc('Джинсы', 1, 6);
SELECT ins_pc('Классические брюки', 1, 6);
SELECT ins_pc('Вечерние платья', 1, 7);
SELECT ins_pc('Сарафаны', 1, 7);
SELECT ins_pc('Повседневные платья', 1, 7);



SELECT ins_product('Пуховик укороченный с капюшоном', 9);
SELECT ins_product('Футболка с надписью', 11);
SELECT ins_product('Штаны спортивные серые', 13);
SELECT ins_product('Джинсы зауженные', 14);
SELECT ins_product('Джинсы прямого кроя', 14);
SELECT ins_product('Платье вечернее с пайетками', 16);
SELECT ins_product('Сарафан пляжный', 17);


INSERT INTO ProductClassifier (name, base_ei, parent_id) VALUES
                                                             ('Кожанные куртки', 1, 4),
                                                             ('Пуховики', 1, 4),
                                                             ('Ветровки', 1, 4),
                                                             ('Футболки', 1, 5),
                                                             ('Майки', 1, 5),
                                                             ('Спортивные брюки', 1, 6),
                                                             ('Джинсы', 1, 6),
                                                             ('Классические брюки', 1, 6),
                                                             ('Вечерние платья', 1, 7),
                                                             ('Сарафаны', 1, 7),
                                                             ('Повседневные платья', 1, 7);

DROP TABLE EI CASCADE;
DROP TABLE ProductClassifier CASCADE;
DROP TABLE Product CASCADE;

-- Now procedures using plpgsql

-- EI procedures
-- Procedure to delete a EI by id if it exists, return 0 if doesn't exist, 1 if deleted
CREATE OR REPLACE FUNCTION del_ei(r_id_ei INTEGER) RETURNS INTEGER AS $$
DECLARE
    ei_exists INTEGER;
    productclassifier_exists INTEGER;
BEGIN
    ei_exists := (SELECT COUNT(*) FROM EI WHERE id_ei = r_id_ei);
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE base_ei = r_id_ei);
    IF ei_exists = 0 OR productclassifier_exists > 0 THEN
        RETURN 0;
    ELSE
        DELETE FROM EI WHERE id_ei = r_id_ei;
        RETURN 1;
    END IF;
END;
$$ LANGUAGE plpgsql;


-- Create a EI, return its id and 1 if successful, 0 if not
CREATE OR REPLACE FUNCTION ins_ei(r_name TEXT) RETURNS TABLE (a_id_ei INTEGER, success INTEGER) AS $$
DECLARE
    ei_exists INTEGER;
BEGIN
    ei_exists := (SELECT COUNT(*) FROM EI WHERE name = r_name);
    IF ei_exists = 0 THEN
        INSERT INTO EI (name) VALUES (r_name);
        RETURN QUERY SELECT id_ei, 1 FROM EI WHERE name = r_name;
    ELSE
        RETURN QUERY SELECT id_ei, 0 FROM EI WHERE name = r_name;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Edit a EI, return 1 if successful, 0 if not
CREATE OR REPLACE FUNCTION update_ei(r_id_ei INTEGER, r_name TEXT) RETURNS INTEGER AS $$
DECLARE
    ei_exists INTEGER;
    name_taken INTEGER;
BEGIN
    ei_exists := (SELECT COUNT(*) FROM EI WHERE id_ei = r_id_ei);
    name_taken := (SELECT COUNT(*) FROM EI WHERE name = r_name);
    IF name_taken > 0 THEN
        RETURN -1;
    END IF;
    IF ei_exists = 0 THEN
        RETURN 0;
    ELSE
        UPDATE EI SET name = r_name WHERE id_ei = r_id_ei;
        RETURN 1;
    END IF;
END;
$$ LANGUAGE plpgsql;


-- Select a EI, return 0 if doesn't exist
CREATE OR REPLACE FUNCTION select_ei(r_id_ei INTEGER) RETURNS TABLE (a_id_ei INTEGER, a_name VARCHAR) AS $$
DECLARE
    ei_exists INTEGER;
BEGIN
    ei_exists := (SELECT COUNT(*) FROM EI WHERE id_ei = r_id_ei);
    IF ei_exists = 0 THEN
        RETURN QUERY SELECT 0, '0';
    ELSE
        RETURN QUERY SELECT id_ei, name FROM EI WHERE id_ei = r_id_ei;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION select_ei_legacy(r_id_ei INTEGER) RETURNS TABLE (a_id_ei INTEGER, a_name TEXT) AS $$
DECLARE
    ei_exists INTEGER;
BEGIN
    ei_exists := (SELECT COUNT(*) FROM EI WHERE id_ei = r_id_ei);
    IF ei_exists = 0 THEN
        RETURN QUERY SELECT 0, '0';
    ELSE
        RETURN QUERY SELECT id_ei, name FROM EI WHERE id_ei = r_id_ei;
    END IF;
END;
$$ LANGUAGE plpgsql;


-- ProductClassifier procedures
-- Create a ProductClassifier, return its id and 1 if successful, 0 if not
-- Создать()
CREATE OR REPLACE FUNCTION ins_pc(r_name TEXT, r_base_ei INTEGER, r_parent_id INTEGER) RETURNS INTEGER AS $$
DECLARE
    productclassifier_exists INTEGER;
    ei_exists INTEGER;
    parent_exists INTEGER;
    productclassifier_empty INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE name = r_name);
    ei_exists := (SELECT COUNT(*) FROM EI WHERE id_ei = r_base_ei);
    parent_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_parent_id);
    productclassifier_empty := (SELECT COUNT(*) FROM ProductClassifier);
    IF (parent_exists = 1 OR (r_parent_id IS NULL AND productclassifier_empty = 0)) AND ei_exists = 1 AND productclassifier_exists = 0 THEN
        INSERT INTO ProductClassifier (name, base_ei, parent_id) VALUES (r_name, r_base_ei, r_parent_id);
        RETURN 1;
    ELSE
        RETURN 0;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Create a ProductClassifier with a null parent id and find a ProductClassifier with a null parent id besides the one just created and set its parent id to the id of the one just created, return 1 if successful, 0 if not
-- Создать корень()
CREATE OR REPLACE FUNCTION ins_pc_root(r_name TEXT, r_base_ei INTEGER) RETURNS INTEGER AS $$
DECLARE
    productclassifier_exists INTEGER;
    ei_exists INTEGER;
    productclassifier_empty INTEGER;
    parent_exists INTEGER;
    old_parent_id INTEGER;
    new_parent_id INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE name = r_name);
    ei_exists := (SELECT COUNT(*) FROM EI WHERE id_ei = r_base_ei);
    productclassifier_empty := (SELECT COUNT(*) FROM ProductClassifier);
    parent_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE parent_id IS NULL);
    IF ei_exists = 1 AND productclassifier_exists = 0 AND (parent_exists = 1 OR productclassifier_empty = 0) THEN
        INSERT INTO ProductClassifier (name, base_ei, parent_id) VALUES (r_name, r_base_ei, NULL);
        old_parent_id := (SELECT id_class FROM ProductClassifier WHERE parent_id IS NULL AND name != r_name);
        new_parent_id := (SELECT id_class FROM ProductClassifier WHERE name = r_name);
        UPDATE ProductClassifier SET parent_id = new_parent_id WHERE id_class = old_parent_id;
        RETURN 1;
    ELSE
        RETURN 0;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Select a ProductClassifier by id, return 0 if doesn't exist
-- Выбрать()
CREATE OR REPLACE FUNCTION select_pc(r_id_class INTEGER) RETURNS TABLE (a_id_class INTEGER, a_name TEXT, a_base_ei INTEGER, a_parent_id INTEGER) AS $$
DECLARE
    productclassifier_exists INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 1 THEN
        RETURN QUERY SELECT id_class, name, base_ei, parent_id FROM ProductClassifier WHERE id_class = r_id_class;
    ELSE
        RETURN QUERY SELECT 0, '0', 0, 0;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Select a parent of a ProductClassifier by id, return 0 if doesn't exist and NULL if doesn't have a parent
-- Выбрать родителя()
CREATE OR REPLACE FUNCTION select_pc_parent(r_id_class INTEGER) RETURNS TABLE (a_id_class INTEGER, a_name TEXT, a_base_ei INTEGER, a_parent_id INTEGER) AS $$
DECLARE
    productclassifier_exists INTEGER;
    parent_exists BOOLEAN;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 1 THEN
        parent_exists := (SELECT parent_id IS NOT NULL FROM ProductClassifier WHERE id_class = r_id_class);
        IF parent_exists = TRUE THEN
            RETURN QUERY SELECT id_class, name, base_ei, parent_id FROM ProductClassifier WHERE id_class = (SELECT parent_id FROM ProductClassifier WHERE id_class = r_id_class);
        ELSE
            RETURN QUERY SELECT 0, 'NULL', 0, 0;
        END IF;
    ELSE
        RETURN QUERY SELECT 0, '0', 0, 0;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Change the parent of a ProductClassifier by id, return 1 if successful, 0 if not
-- Изменить родителя()
CREATE OR REPLACE FUNCTION update_pc_parent(r_id_class INTEGER, r_parent_id INTEGER) RETURNS INTEGER AS $$
DECLARE
    productclassifier_exists INTEGER;
    parent_exists INTEGER;
    productclassifier_empty INTEGER;
BEGIN
    IF r_parent_id = r_id_class THEN
        RETURN 0;
    END IF;
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    parent_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_parent_id);
    productclassifier_empty := (SELECT COUNT(*) FROM ProductClassifier);
    IF (parent_exists = 1 OR (r_parent_id IS NULL AND productclassifier_empty = 1)) AND productclassifier_exists = 1 THEN
        UPDATE ProductClassifier SET parent_id = r_parent_id WHERE id_class = r_id_class;
        RETURN 1;
    ELSE
        RETURN 0;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Delete a ProductClassifier and all its children recursive, and all products of this ProductClassifier and its children, return 1 if successful, 0 if not
-- Удалить()
CREATE OR REPLACE FUNCTION del_pc(r_id_class INTEGER) RETURNS INTEGER AS $$
DECLARE
    productclassifier_exists INTEGER;
    has_children INTEGER;
    has_products INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 0 THEN
        RETURN -1;
    END IF;
    has_children := (SELECT COUNT(*) FROM ProductClassifier WHERE parent_id = r_id_class);
    has_products := (SELECT COUNT(*) FROM Product WHERE id_class = r_id_class);
    IF has_children = 0 AND has_products = 0 THEN
        DELETE FROM ProductClassifier WHERE id_class = r_id_class;
        RETURN 1;
    ELSE
        DELETE FROM ProductClassifier WHERE id_class = r_id_class;
        DELETE FROM Product WHERE id_class = r_id_class;
        DELETE FROM ProductClassifier WHERE parent_id = r_id_class;
        DELETE FROM Product WHERE id_class IN (SELECT id_class FROM ProductClassifier WHERE parent_id = r_id_class);
        RETURN 1;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Change the ProductClassifier name by id, return 1 if successful, 0 if not
-- Редактировать()
CREATE OR REPLACE FUNCTION update_pc_name(r_id_class INTEGER, r_name TEXT) RETURNS INTEGER AS $$
DECLARE
    productclassifier_exists INTEGER;
    name_taken INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    name_taken := (SELECT COUNT(*) FROM ProductClassifier WHERE name = r_name);
    IF name_taken > 0 THEN
        RETURN -1;
    END IF;
    IF productclassifier_exists = 0 THEN
        RETURN 0;
    ELSE
        UPDATE ProductClassifier SET name = r_name WHERE id_class = r_id_class;
        RETURN 1;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Change the ProductClassifier base_ei by id, return 1 if successful, 0 if not
-- Указать базовую единицу()
CREATE OR REPLACE FUNCTION update_pc_base_ei(r_id_class INTEGER, r_base_ei INTEGER) RETURNS INTEGER AS $$
DECLARE
    productclassifier_exists INTEGER;
    ei_exists INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    ei_exists := (SELECT COUNT(*) FROM EI WHERE id_ei = r_base_ei);
    IF productclassifier_exists = 0 OR ei_exists = 0 THEN
        RETURN 0;
    ELSE
        UPDATE ProductClassifier SET base_ei = r_base_ei WHERE id_class = r_id_class;
        RETURN 1;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Select a subgraph of children of a ProductClassifier by id, return 0 if doesn't exist
-- Найти потомков()
CREATE OR REPLACE FUNCTION select_pc_children(r_id_class INTEGER, sorttype TEXT) RETURNS TABLE (a_id_class INTEGER, a_name VARCHAR, a_base_ei INTEGER, a_parent_id INTEGER) AS $$
DECLARE
    productclassifier_exists INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0, 0;
    ELSE
        RETURN QUERY WITH RECURSIVE children AS (
            SELECT id_class, name, base_ei, parent_id FROM ProductClassifier WHERE id_class = r_id_class
            UNION ALL
            SELECT pc.id_class, pc.name, pc.base_ei, pc.parent_id FROM ProductClassifier pc, children c WHERE pc.parent_id = c.id_class
        ) SELECT * FROM children WHERE id_class != r_id_class
                                 ORDER BY
                                     CASE WHEN sorttype = 'name' THEN name END,
                                     CASE WHEN sorttype = 'id_class' THEN id_class END,
                                     CASE WHEN sorttype = 'base_ei' THEN base_ei END,
                                        CASE WHEN sorttype = 'parent_id' THEN parent_id END;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION select_pc_children_legacy(r_id_class INTEGER, sorttype TEXT) RETURNS TABLE (a_id_class INTEGER, a_name TEXT, a_base_ei INTEGER, a_parent_id INTEGER) AS $$
DECLARE
    productclassifier_exists INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0, 0;
    ELSE
        RETURN QUERY WITH RECURSIVE children AS (
            SELECT id_class, name, base_ei, parent_id FROM ProductClassifier WHERE id_class = r_id_class
            UNION ALL
            SELECT pc.id_class, pc.name, pc.base_ei, pc.parent_id FROM ProductClassifier pc, children c WHERE pc.parent_id = c.id_class
        ) SELECT * FROM children WHERE id_class != r_id_class
                     ORDER BY
                         CASE WHEN sorttype = 'name' THEN name END,
                         CASE WHEN sorttype = 'id_class' THEN id_class END,
                         CASE WHEN sorttype = 'base_ei' THEN base_ei END,
                         CASE WHEN sorttype = 'parent_id' THEN parent_id END;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Select children of a ProductClassifier and their products by id, for this join a ProductClassifier table and Product table, return 0 if doesn't exist
-- Найти потомков с изделиями()
CREATE OR REPLACE FUNCTION select_pc_children_products(r_id_class INTEGER, sorttype TEXT) RETURNS TABLE (a_id_class INTEGER, a_name VARCHAR, a_base_ei INTEGER, a_parent_id INTEGER, a_id_product INTEGER, a_name_product VARCHAR, a_id_class2 INTEGER) AS $$
DECLARE
    productclassifier_exists INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0, 0, 0, '0';
    ELSE
        RETURN QUERY WITH RECURSIVE children AS (
            SELECT id_class, name, base_ei, parent_id FROM ProductClassifier WHERE id_class = r_id_class
            UNION ALL
            SELECT pc.id_class, pc.name, pc.base_ei, pc.parent_id FROM ProductClassifier pc, children c WHERE pc.parent_id = c.id_class
        ) SELECT * FROM children c LEFT JOIN Product p ON c.id_class = p.id_class
                                 ORDER BY
                                     CASE WHEN sorttype = 'name' THEN c.name END,
                                     CASE WHEN sorttype = 'id_class' THEN c.id_class END,
                                     CASE WHEN sorttype = 'base_ei' THEN c.base_ei END,
                                     CASE WHEN sorttype = 'parent_id' THEN c.parent_id END,
                                     CASE WHEN sorttype = 'name_product' THEN p.name END,
                                     CASE WHEN sorttype = 'id_product' THEN p.id_product END;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION select_pc_children_products_legacy(r_id_class INTEGER, sorttype TEXT) RETURNS TABLE (a_id_class INTEGER, a_name TEXT, a_base_ei INTEGER, a_parent_id INTEGER, a_id_product INTEGER, a_name_product TEXT, a_id_class2 INTEGER) AS $$
DECLARE
    productclassifier_exists INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0, 0, 0, '0';
    ELSE
        RETURN QUERY WITH RECURSIVE children AS (
            SELECT id_class, name, base_ei, parent_id FROM ProductClassifier WHERE id_class = r_id_class
            UNION ALL
            SELECT pc.id_class, pc.name, pc.base_ei, pc.parent_id FROM ProductClassifier pc, children c WHERE pc.parent_id = c.id_class
        ) SELECT * FROM children c LEFT JOIN Product p ON c.id_class = p.id_class
                     ORDER BY
                         CASE WHEN sorttype = 'name' THEN c.name END,
                         CASE WHEN sorttype = 'id_class' THEN c.id_class END,
                         CASE WHEN sorttype = 'base_ei' THEN c.base_ei END,
                         CASE WHEN sorttype = 'parent_id' THEN c.parent_id END,
                         CASE WHEN sorttype = 'name_product' THEN p.name END,
                         CASE WHEN sorttype = 'id_product' THEN p.id_product END;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Select children of a ProductClassifier and their products by id, for this join a ProductClassifier table and Product table and return only products, return 0 if doesn't exist
-- Найти изделия()
CREATE OR REPLACE FUNCTION select_pc_children_only_products(r_id_class INTEGER, sorttype TEXT) RETURNS TABLE (a_id_product INTEGER, a_name_product VARCHAR, a_id_class INTEGER) AS $$
DECLARE
    productclassifier_exists INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0;
    ELSE
        RETURN QUERY WITH RECURSIVE children AS (
            SELECT id_class, name, base_ei, parent_id FROM ProductClassifier WHERE id_class = r_id_class
            UNION ALL
            SELECT pc.id_class, pc.name, pc.base_ei, pc.parent_id FROM ProductClassifier pc, children c WHERE pc.parent_id = c.id_class
        ) SELECT p.id_product, p.name, c.id_class FROM children c RIGHT JOIN Product p ON c.id_class = p.id_class
                                 ORDER BY
                                     CASE WHEN sorttype = 'name' THEN c.name END,
                                     CASE WHEN sorttype = 'id_class' THEN c.id_class END,
                                     CASE WHEN sorttype = 'base_ei' THEN c.base_ei END,
                                     CASE WHEN sorttype = 'parent_id' THEN c.parent_id END,
                                     CASE WHEN sorttype = 'name_product' THEN p.name END,
                                     CASE WHEN sorttype = 'id_product' THEN p.id_product END;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION select_pc_children_only_products_legacy(r_id_class INTEGER, sorttype TEXT) RETURNS TABLE (a_id_product INTEGER, a_name_product TEXT, a_id_class INTEGER) AS $$
DECLARE
    productclassifier_exists INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0;
    ELSE
        RETURN QUERY WITH RECURSIVE children AS (
            SELECT id_class, name, base_ei, parent_id FROM ProductClassifier WHERE id_class = r_id_class
            UNION ALL
            SELECT pc.id_class, pc.name, pc.base_ei, pc.parent_id FROM ProductClassifier pc, children c WHERE pc.parent_id = c.id_class
        ) SELECT p.id_product, p.name, c.id_class FROM children c RIGHT JOIN Product p ON c.id_class = p.id_class
                     ORDER BY
                         CASE WHEN sorttype = 'name' THEN c.name END,
                         CASE WHEN sorttype = 'id_class' THEN c.id_class END,
                         CASE WHEN sorttype = 'base_ei' THEN c.base_ei END,
                         CASE WHEN sorttype = 'parent_id' THEN c.parent_id END,
                         CASE WHEN sorttype = 'name_product' THEN p.name END,
                         CASE WHEN sorttype = 'id_product' THEN p.id_product END;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Select all parents of a ProductClassifier by id, return 0 if doesn't exist
-- Найти родителей()
CREATE OR REPLACE FUNCTION select_pc_parents(r_id_class INTEGER, sorttype TEXT) RETURNS TABLE (a_id_class INTEGER, a_name VARCHAR, a_base_ei INTEGER, a_parent_id INTEGER) AS $$
DECLARE
    productclassifier_exists INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0, 0;
    ELSE
        RETURN QUERY WITH RECURSIVE parents AS (
            SELECT id_class, name, base_ei, parent_id FROM ProductClassifier WHERE id_class = r_id_class
            UNION ALL
            SELECT pc.id_class, pc.name, pc.base_ei, pc.parent_id FROM ProductClassifier pc, parents p WHERE pc.id_class = p.parent_id
        ) SELECT * FROM parents WHERE id_class != r_id_class
                                 ORDER BY
                                     CASE WHEN sorttype = 'name' THEN name END,
                                     CASE WHEN sorttype = 'id_class' THEN id_class END,
                                     CASE WHEN sorttype = 'base_ei' THEN base_ei END,
                                        CASE WHEN sorttype = 'parent_id' THEN parent_id END;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION select_pc_parents_legacy(r_id_class INTEGER, sorttype TEXT) RETURNS TABLE (a_id_class INTEGER, a_name TEXT, a_base_ei INTEGER, a_parent_id INTEGER) AS $$
DECLARE
    productclassifier_exists INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0, 0;
    ELSE
        RETURN QUERY WITH RECURSIVE parents AS (
            SELECT id_class, name, base_ei, parent_id FROM ProductClassifier WHERE id_class = r_id_class
            UNION ALL
            SELECT pc.id_class, pc.name, pc.base_ei, pc.parent_id FROM ProductClassifier pc, parents p WHERE pc.id_class = p.parent_id
        ) SELECT * FROM parents WHERE id_class != r_id_class
                     ORDER BY
                         CASE WHEN sorttype = 'name' THEN name END,
                         CASE WHEN sorttype = 'id_class' THEN id_class END,
                         CASE WHEN sorttype = 'base_ei' THEN base_ei END,
                         CASE WHEN sorttype = 'parent_id' THEN parent_id END;
    END IF;
END;
$$ LANGUAGE plpgsql;


-- Product procedures
-- Create a Product, return -1 if ProductClassifier doesn't exist, 0 if Product already exists, 1 if successful
-- Создать()
CREATE OR REPLACE FUNCTION ins_product(r_name TEXT, r_id_class INTEGER) RETURNS INTEGER AS $$
DECLARE
    productclassifier_exists INTEGER;
    product_exists INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    product_exists := (SELECT COUNT(*) FROM Product WHERE name = r_name);
    IF productclassifier_exists = 0 THEN
        RETURN -1;
    END IF;
    IF product_exists = 0 THEN
        INSERT INTO Product (name, id_class) VALUES (r_name, r_id_class);
        RETURN 1;
    ELSE
        RETURN 0;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Change parent of a Product by id, return 1 if successful, 0 if a Product doesn't exist, -1 if a ProductClassifier doesn't exist
-- Изменить родителя()
CREATE OR REPLACE FUNCTION update_product_parent(r_id_product INTEGER, r_id_class INTEGER) RETURNS INTEGER AS $$
DECLARE
    product_exists INTEGER;
    productclassifier_exists INTEGER;
BEGIN
    product_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_product);
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF product_exists = 0 THEN
        RETURN 0;
    END IF;
    IF productclassifier_exists = 0 THEN
        RETURN -1;
    END IF;
    UPDATE Product SET id_class = r_id_class WHERE id_product = r_id_product;
    RETURN 1;
END;
$$ LANGUAGE plpgsql;

-- Delete a Product by id, return 1 if successful, 0 if not
-- Удалить()
CREATE OR REPLACE FUNCTION del_product(r_id_product INTEGER) RETURNS INTEGER AS $$
DECLARE
    product_exists INTEGER;
BEGIN
    product_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_product);
    IF product_exists = 0 THEN
        RETURN 0;
    END IF;
    DELETE FROM Product WHERE id_product = r_id_product;
    RETURN 1;
END;
$$ LANGUAGE plpgsql;

-- Select a parent of a Product by id, return 0 if doesn't exist
-- Выбрать родителя()
CREATE OR REPLACE FUNCTION select_product_parent(r_id_product INTEGER) RETURNS TABLE (a_id_class INTEGER, a_name TEXT, a_base_ei INTEGER, a_parent_id INTEGER) AS $$
DECLARE
    product_exists INTEGER;
BEGIN
    product_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_product);
    IF product_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0, 0;
    END IF;
    RETURN QUERY SELECT id_class, name, base_ei, parent_id FROM ProductClassifier WHERE id_class = (SELECT id_class FROM Product WHERE id_product = r_id_product);
END;
$$ LANGUAGE plpgsql;

-- Select a Product by id, return 0 if doesn't exist
-- Выбрать()
CREATE OR REPLACE FUNCTION select_product(r_id_product INTEGER) RETURNS TABLE (a_id_product INTEGER, a_name TEXT, a_id_class INTEGER) AS $$
DECLARE
    product_exists INTEGER;
BEGIN
    product_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_product);
    IF product_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0;
    END IF;
    RETURN QUERY SELECT id_product, name, id_class FROM Product WHERE id_product = r_id_product;
END;
$$ LANGUAGE plpgsql;

-- Change the Product name by id, return 1 if successful, 0 if not
-- Редактировать()
CREATE OR REPLACE FUNCTION update_product_name(r_id_product INTEGER, r_name TEXT) RETURNS INTEGER AS $$
DECLARE
    product_exists INTEGER;
    name_taken INTEGER;
BEGIN
    product_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_product);
    name_taken := (SELECT COUNT(*) FROM Product WHERE name = r_name);
    IF name_taken > 0 THEN
        RETURN -1;
    END IF;
    IF product_exists = 0 THEN
        RETURN 0;
    ELSE
        UPDATE Product SET name = r_name WHERE id_product = r_id_product;
        RETURN 1;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Select all parents of a Product by id, return 0 if doesn't exist
-- Найти родителей()
CREATE OR REPLACE FUNCTION select_product_parents(r_id_product INTEGER, sorttype TEXT) RETURNS TABLE (a_id_class INTEGER, a_name VARCHAR, a_base_ei INTEGER, a_parent_id INTEGER) AS $$
DECLARE
    product_exists INTEGER;
BEGIN
    product_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_product);
    IF product_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0, 0;
    ELSE
        RETURN QUERY WITH RECURSIVE parents AS (
            SELECT id_class, name, base_ei, parent_id FROM ProductClassifier WHERE id_class = (SELECT id_class FROM Product WHERE id_product = r_id_product)
            UNION ALL
            SELECT pc.id_class, pc.name, pc.base_ei, pc.parent_id FROM ProductClassifier pc, parents p WHERE pc.id_class = p.parent_id
        ) SELECT * FROM parents
                                 ORDER BY
                                     CASE WHEN sorttype = 'name' THEN name END,
                                     CASE WHEN sorttype = 'id_class' THEN id_class END,
                                     CASE WHEN sorttype = 'base_ei' THEN base_ei END,
                                        CASE WHEN sorttype = 'parent_id' THEN parent_id END;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION select_product_parents_legacy(r_id_product INTEGER, sorttype TEXT) RETURNS TABLE (a_id_class INTEGER, a_name TEXT, a_base_ei INTEGER, a_parent_id INTEGER) AS $$
DECLARE
    product_exists INTEGER;
BEGIN
    product_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_product);
    IF product_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0, 0;
    ELSE
        RETURN QUERY WITH RECURSIVE parents AS (
            SELECT id_class, name, base_ei, parent_id FROM ProductClassifier WHERE id_class = (SELECT id_class FROM Product WHERE id_product = r_id_product)
            UNION ALL
            SELECT pc.id_class, pc.name, pc.base_ei, pc.parent_id FROM ProductClassifier pc, parents p WHERE pc.id_class = p.parent_id
        ) SELECT * FROM parents
                     ORDER BY
                         CASE WHEN sorttype = 'name' THEN name END,
                         CASE WHEN sorttype = 'id_class' THEN id_class END,
                         CASE WHEN sorttype = 'base_ei' THEN base_ei END,
                         CASE WHEN sorttype = 'parent_id' THEN parent_id END;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- General function to reset tables
-- Сбросить()
CREATE PROCEDURE reset_tables() AS $$
BEGIN
    DROP TABLE EI CASCADE;
    DROP TABLE ProductClassifier CASCADE;
    DROP TABLE Product CASCADE;

    CREATE TABLE EI (
    id_ei SERIAL PRIMARY KEY,
    name TEXT NOT NULL
    );

    CREATE TABLE ProductClassifier (
        id_class SERIAL PRIMARY KEY,
        name TEXT NOT NULL,
        base_ei INTEGER NOT NULL,
        FOREIGN KEY (base_ei) REFERENCES EI(id_ei) ON DELETE CASCADE ON UPDATE CASCADE,
        parent_id INTEGER,
        FOREIGN KEY (parent_id) REFERENCES ProductClassifier(id_class) ON DELETE CASCADE ON UPDATE CASCADE
    );

    CREATE TABLE Product (
        id_product SERIAL PRIMARY KEY,
        name TEXT NOT NULL,
        id_class INTEGER NOT NULL,
        FOREIGN KEY (id_class) REFERENCES ProductClassifier(id_class) ON DELETE CASCADE ON UPDATE CASCADE
    );
END;
$$ LANGUAGE plpgsql;
