CREATE TABLE EI (
    id_ei SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    short_name TEXT NOT NULL,
    code INTEGER NOT NULL
);

CREATE TABLE ProductClassifier (
    id_class SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    base_id INTEGER NOT NULL,
    FOREIGN KEY (base_id) REFERENCES EI(id_ei) ON DELETE CASCADE ON UPDATE CASCADE,
    parent_id INTEGER,
    FOREIGN KEY (parent_id) REFERENCES ProductClassifier(id_class) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Product (
    id_product SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    id_class INTEGER NOT NULL,
    FOREIGN KEY (id_class) REFERENCES ProductClassifier(id_class) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE ProductSpecification (
    id_product INTEGER NOT NULL,
    FOREIGN KEY (id_product) REFERENCES Product(id_product) ON DELETE CASCADE ON UPDATE CASCADE,
    pos_num INTEGER NOT NULL,
    id_part INTEGER NOT NULL,
    FOREIGN KEY (id_part) REFERENCES Product(id_product) ON DELETE CASCADE ON UPDATE CASCADE,
    quantity INTEGER NOT NULL,
    CONSTRAINT pk_product_specification PRIMARY KEY (id_product, pos_num)
);

-- EI procedures

-- Delete EI by id
-- 0 if EI not exists, 1 if EI deleted
-- Удалить()
CREATE OR REPLACE FUNCTION del_ei(r_id_ei INTEGER) RETURNS INTEGER AS $$
DECLARE
    ei_exists INTEGER;
BEGIN
    ei_exists := (SELECT COUNT(*) FROM EI WHERE id_ei = r_id_ei);
    IF ei_exists = 0 THEN
        RETURN 0;
    ELSE
        DELETE FROM EI WHERE id_ei = r_id_ei;
        RETURN 1;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Insert EI
-- 0 if EI exists, 1 if success
-- Вставить()
CREATE OR REPLACE FUNCTION ins_ei(r_name TEXT, r_short_name TEXT, r_code INTEGER) RETURNS INTEGER AS $$
DECLARE
    ei_exists INTEGER;
BEGIN
    ei_exists := (SELECT COUNT(*) FROM EI WHERE code = r_code);
    IF ei_exists = 0 THEN
        INSERT INTO EI (name, short_name, code) VALUES (r_name, r_short_name, r_code);
        RETURN 1;
    ELSE
        RETURN 0;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Update EI
-- 0 if EI not exists, 1 if EI updated
-- Изменить()
CREATE OR REPLACE FUNCTION update_ei(r_id_ei INTEGER, r_name TEXT, r_short_name TEXT, r_code INTEGER) RETURNS INTEGER AS $$
DECLARE
    ei_exists INTEGER;
    ei_name_exists INTEGER;
BEGIN
    ei_exists := (SELECT COUNT(*) FROM EI WHERE id_ei = r_id_ei);
    ei_name_exists := (SELECT COUNT(*) FROM EI WHERE name = r_name);
    IF ei_exists = 0 THEN
        RETURN 0;
    END IF;
    IF ei_name_exists != 0 THEN
        RETURN -1;
    ELSE
        UPDATE EI SET name = r_name, short_name = r_short_name, code = r_code WHERE id_ei = r_id_ei;
        RETURN 1;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Select EI by id
-- 0 if EI not exists, EI if success
-- Выбрать()
CREATE OR REPLACE FUNCTION select_ei(r_id_ei INTEGER) RETURNS TABLE (a_id_ei INTEGER, a_name TEXT, a_short_name TEXT, a_code INTEGER) AS $$
DECLARE
    ei_exists INTEGER;
BEGIN
    ei_exists := (SELECT COUNT(*) FROM EI WHERE id_ei = r_id_ei);
    IF ei_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', '0', 0;
    ELSE
        RETURN QUERY SELECT id_ei, name, short_name, code FROM EI WHERE id_ei = r_id_ei;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- ProductClassifier procedures

-- Create ProductClassifier
-- 1 if success, 0 if not
-- Создать()
CREATE OR REPLACE FUNCTION ins_pc(r_name TEXT, r_base_id INTEGER, r_parent_id INTEGER) RETURNS INTEGER AS $$
DECLARE
    productclassifier_exists INTEGER;
    ei_exists INTEGER;
    parent_exists INTEGER;
    productclassifier_empty INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE name = r_name);
    ei_exists := (SELECT COUNT(*) FROM EI WHERE id_ei = r_base_id);
    parent_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_parent_id);
    productclassifier_empty := (SELECT COUNT(*) FROM ProductClassifier);
    IF (parent_exists = 1 OR (r_parent_id IS NULL AND productclassifier_empty = 0)) AND ei_exists = 1 AND productclassifier_exists = 0 THEN
        INSERT INTO ProductClassifier (name, base_id, parent_id) VALUES (r_name, r_base_id, r_parent_id);
        RETURN 1;
    ELSE
        RETURN 0;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Select ProductClassifier by id
-- 0 if ProductClassifier not exists, ProductClassifier if success
-- Выбрать()
CREATE OR REPLACE FUNCTION select_pc(r_id_class INTEGER) RETURNS TABLE (a_id_class INTEGER, a_name TEXT, a_base_id INTEGER, a_parent_id INTEGER) AS $$
DECLARE
    productclassifier_exists INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0, 0;
    ELSE
        RETURN QUERY SELECT id_class, name, base_id, parent_id FROM ProductClassifier WHERE id_class = r_id_class;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Select a parent of ProductClassifier by id
-- 0 if ProductClassifier not exists, NULL if parent not exists, parent if success
-- ВыбратьРодителя()
CREATE OR REPLACE FUNCTION select_pc_parent(r_id_class INTEGER) RETURNS TABLE (a_id_class INTEGER, a_name TEXT, a_base_id INTEGER, a_parent_id INTEGER) AS $$
DECLARE
    productclassifier_exists INTEGER;
    parent_exists BOOLEAN;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 1 THEN
        parent_exists := (SELECT parent_id IS NOT NULL FROM ProductClassifier WHERE id_class = r_id_class);
        IF parent_exists = TRUE THEN
            RETURN QUERY SELECT id_class, name, base_id, parent_id FROM ProductClassifier WHERE id_class = (SELECT parent_id FROM ProductClassifier WHERE id_class = r_id_class);
        ELSE
            RETURN QUERY SELECT 0, 'NULL', 0, 0;
        END IF;
    ELSE
        RETURN QUERY SELECT 0, '0', 0, 0;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Change parent of ProductClassifier by id
-- 0 if failed, 1 if success
-- ИзменитьРодителя()
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

-- Delete a ProductClassifier by id
-- -1 if ProductClassifier not exists, 0 if ProductClassifier has children or products, 1 if success
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
        RETURN 0;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Delete a ProductClassifier by id with children and products
--
-- УдалитьСПотомками()
CREATE OR REPLACE FUNCTION del_pc_with_children(r_id_class INTEGER) RETURNS INTEGER AS $$
DECLARE
    productclassifier_exists INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 0 THEN
        RETURN 0;
    ELSE
        DELETE FROM ProductClassifier WHERE id_class = r_id_class;
        RETURN 1;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Change the ProductClassifier name by id
-- 0 if ProductClassifier not exists, 1 if success
-- Редактировать()
CREATE OR REPLACE FUNCTION update_pc_name(r_id_class INTEGER, r_name TEXT) RETURNS INTEGER AS $$
DECLARE
    productclassifier_exists INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 0 THEN
        RETURN 0;
    ELSE
        UPDATE ProductClassifier SET name = r_name WHERE id_class = r_id_class;
        RETURN 1;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Change the ProductClassifier base_id by id
-- 0 if ProductClassifier not exists, 1 if success
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
        UPDATE ProductClassifier SET base_id = r_base_ei WHERE id_class = r_id_class;
        RETURN 1;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Select a subgraph of children of a ProductClassifier by id
-- 0 if ProductClassifier not exists, subgraph if success
-- Найти потомков()
CREATE OR REPLACE FUNCTION select_pc_children(r_id_class INTEGER, sorttype TEXT) RETURNS TABLE (a_id_class INTEGER, a_name VARCHAR(255), a_base_ei INTEGER, a_parent_id INTEGER) AS $$
DECLARE
    productclassifier_exists INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0, 0;
    ELSE
        RETURN QUERY WITH RECURSIVE children AS (
            SELECT id_class, name, base_id, parent_id FROM ProductClassifier WHERE id_class = r_id_class
            UNION ALL
            SELECT pc.id_class, pc.name, pc.base_id, pc.parent_id FROM ProductClassifier pc, children c WHERE pc.parent_id = c.id_class
        ) SELECT * FROM children WHERE id_class != r_id_class
                     ORDER BY
                         CASE WHEN sorttype = 'name' THEN name END,
                         CASE WHEN sorttype = 'id' THEN id_class END,
                         CASE WHEN sorttype = 'base_ei' THEN base_id END,
                         CASE WHEN sorttype = 'parent_id' THEN parent_id END;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Select a subgraph of children of a ProductClassifier by id and include Products of those children
-- 0 if ProductClassifier not exists, subgraph if success
-- Найти потомков()
CREATE OR REPLACE FUNCTION select_pc_children_products(r_id_class INTEGER, sorttype TEXT) RETURNS TABLE (a_id_class INTEGER, a_name VARCHAR(255), a_base_ei INTEGER, a_parent_id INTEGER, a_id_product INTEGER, a_product_name VARCHAR(255), a_product_id_class INTEGER) AS $$
DECLARE
    productclassifier_exists INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0, 0, 0, '0', 0;
    ELSE
        RETURN QUERY WITH RECURSIVE children AS (
            SELECT id_class, name, base_id, parent_id FROM ProductClassifier WHERE id_class = r_id_class
            UNION ALL
            SELECT pc.id_class, pc.name, pc.base_id, pc.parent_id FROM ProductClassifier pc, children c WHERE pc.parent_id = c.id_class
        ) SELECT c.id_class, c.name, c.base_id, c.parent_id, p.id_product, p.name, p.id_class FROM children c LEFT JOIN Product p ON c.id_class = p.id_class
                     ORDER BY
                         CASE WHEN sorttype = 'name' THEN c.name END,
                         CASE WHEN sorttype = 'id' THEN c.id_class END,
                         CASE WHEN sorttype = 'base_ei' THEN c.base_id END,
                         CASE WHEN sorttype = 'parent_id' THEN c.parent_id END,
                         CASE WHEN sorttype = 'product_name' THEN p.name END,
                         CASE WHEN sorttype = 'product_id' THEN p.id_product END,
                         CASE WHEN sorttype = 'product_id_class' THEN p.id_class END;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Same as above but only include Products, no need for ProductClassifiers themselves
-- 0 if ProductClassifier not exists, subgraph if success
-- Найти потомков()
CREATE OR REPLACE FUNCTION select_pc_children_only_products(r_id_class INTEGER, sorttype TEXT) RETURNS TABLE (a_id_product INTEGER, a_name VARCHAR(255), a_id_class INTEGER) AS $$
DECLARE
    productclassifier_exists INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0;
    ELSE
        RETURN QUERY WITH RECURSIVE children AS (
            SELECT id_class, name, base_id, parent_id FROM ProductClassifier WHERE id_class = r_id_class
            UNION ALL
            SELECT pc.id_class, pc.name, pc.base_id, pc.parent_id FROM ProductClassifier pc, children c WHERE pc.parent_id = c.id_class
        ) SELECT p.id_product, p.name, p.id_class FROM children c, Product p WHERE c.id_class = p.id_class
                     ORDER BY
                         CASE WHEN sorttype = 'name' THEN p.name END,
                         CASE WHEN sorttype = 'id' THEN p.id_product END,
                         CASE WHEN sorttype = 'id_class' THEN p.id_class END;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Select all parents of a ProductClassifier by id
-- 0 if ProductClassifier not exists, parents if success
-- Найти родителей()
CREATE OR REPLACE FUNCTION select_pc_parents(r_id_class INTEGER, sorttype TEXT) RETURNS TABLE (a_id_class INTEGER, a_name VARCHAR(255), a_base_ei INTEGER, a_parent_id INTEGER) AS $$
DECLARE
    productclassifier_exists INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0, 0;
    ELSE
        RETURN QUERY WITH RECURSIVE parents AS (
            SELECT id_class, name, base_id, parent_id FROM ProductClassifier WHERE id_class = r_id_class
            UNION ALL
            SELECT pc.id_class, pc.name, pc.base_id, pc.parent_id FROM ProductClassifier pc, parents p WHERE pc.id_class = p.parent_id
        ) SELECT * FROM parents WHERE id_class != r_id_class
                     ORDER BY
                         CASE WHEN sorttype = 'name' THEN name END,
                         CASE WHEN sorttype = 'id' THEN id_class END,
                         CASE WHEN sorttype = 'base_ei' THEN base_id END,
                         CASE WHEN sorttype = 'parent_id' THEN parent_id END;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Product procedures

-- Create Product
-- -1 if ProductClassifier not exists, 0 if Product exists, 1 if success
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

-- Change parent of Product by id
-- -1 if ProductClassifier not exists, 0 if Product not exists, 1 if success
-- ИзменитьРодителя()
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

-- Delete Product by id
-- 0 if Product not exists, 1 if success
-- Удалить()
CREATE OR REPLACE FUNCTION del_product(r_id_product INTEGER) RETURNS INTEGER AS $$
DECLARE
    product_exists INTEGER;
BEGIN
    product_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_product);
    IF product_exists = 0 THEN
        RETURN 0;
    ELSE
        DELETE FROM Product WHERE id_product = r_id_product;
        RETURN 1;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Select parent of Product by id
-- 0 if Product not exists, parent if success
-- ВыбратьРодителя()
CREATE OR REPLACE FUNCTION select_product_parent(r_id_product INTEGER) RETURNS TABLE (a_id_class INTEGER, a_name TEXT, a_base_ei INTEGER, a_parent_id INTEGER) AS $$
DECLARE
    product_exists INTEGER;
BEGIN
    product_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_product);
    IF product_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0, 0;
    ELSE
        RETURN QUERY SELECT id_class, name, base_id, parent_id FROM ProductClassifier WHERE id_class = (SELECT id_class FROM Product WHERE id_product = r_id_product);
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Select Product by id
-- 0 if Product not exists, Product if success
-- Выбрать()
CREATE OR REPLACE FUNCTION select_product(r_id_product INTEGER) RETURNS TABLE (a_id_product INTEGER, a_name TEXT, a_id_class INTEGER) AS $$
DECLARE
    product_exists INTEGER;
BEGIN
    product_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_product);
    IF product_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0;
    ELSE
        RETURN QUERY SELECT id_product, name, id_class FROM Product WHERE id_product = r_id_product;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Change the Product name by id
-- 0 if Product not exists, 1 if success
-- Редактировать()
CREATE OR REPLACE FUNCTION update_product_name(r_id_product INTEGER, r_name TEXT) RETURNS INTEGER AS $$
DECLARE
    product_exists INTEGER;
BEGIN
    product_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_product);
    IF product_exists = 0 THEN
        RETURN 0;
    ELSE
        UPDATE Product SET name = r_name WHERE id_product = r_id_product;
        RETURN 1;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Select all parents of a Product by id
-- 0 if Product not exists, parents if success
-- Найти родителей()
CREATE OR REPLACE FUNCTION select_product_parents(r_id_product INTEGER, sorttype TEXT) RETURNS TABLE (a_id_class INTEGER, a_name VARCHAR(255), a_base_ei INTEGER, a_parent_id INTEGER) AS $$
DECLARE
    product_exists INTEGER;
BEGIN
    product_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_product);
    IF product_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0, 0;
    ELSE
        RETURN QUERY WITH RECURSIVE parents AS (
            SELECT id_class, name, base_id, parent_id FROM ProductClassifier WHERE id_class = (SELECT id_class FROM Product WHERE id_product = r_id_product)
            UNION ALL
            SELECT pc.id_class, pc.name, pc.base_id, pc.parent_id FROM ProductClassifier pc, parents p WHERE pc.id_class = p.parent_id
        ) SELECT * FROM parents
                     ORDER BY
                         CASE WHEN sorttype = 'name' THEN name END,
                         CASE WHEN sorttype = 'id' THEN id_class END,
                         CASE WHEN sorttype = 'base_ei' THEN base_id END,
                         CASE WHEN sorttype = 'parent_id' THEN parent_id END;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Select all Product by ProductClassifier id
-- 0 if ProductClassifier not exists, Products if success
-- НайтиПоКлассу()
CREATE OR REPLACE FUNCTION select_products_by_pc(r_id_class INTEGER, sorttype TEXT) RETURNS TABLE (a_id_product INTEGER, a_name TEXT, a_id_class INTEGER) AS $$
DECLARE
    productclassifier_exists INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0;
    ELSE
        RETURN QUERY SELECT id_product, name, id_class FROM Product WHERE id_class = r_id_class
                     ORDER BY
                         CASE WHEN sorttype = 'name' THEN name END,
                         CASE WHEN sorttype = 'id' THEN id_product END,
                         CASE WHEN sorttype = 'id_class' THEN id_class END;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Select all Product by ProductClassifier id (recursively)
-- 0 if ProductClassifier not exists, Products if success
-- НайтиПоКлассуРекурсивно()
CREATE OR REPLACE FUNCTION select_products_by_pc_recursively(r_id_class INTEGER, sorttype TEXT) RETURNS TABLE (a_id_product INTEGER, a_name TEXT, a_id_class INTEGER) AS $$
DECLARE
    productclassifier_exists INTEGER;
BEGIN
    productclassifier_exists := (SELECT COUNT(*) FROM ProductClassifier WHERE id_class = r_id_class);
    IF productclassifier_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0;
    ELSE
        RETURN QUERY WITH RECURSIVE children AS (
            SELECT pc.id_class, pc.name, pc.base_id, pc.parent_id FROM ProductClassifier pc WHERE pc.id_class = r_id_class
            UNION ALL
            SELECT pc.id_class, pc.name, pc.base_id, pc.parent_id FROM ProductClassifier pc, children c WHERE pc.parent_id = c.id_class
        ) SELECT p.id_product, p.name, p.id_class FROM Product p, children c WHERE p.id_class = c.id_class
                     ORDER BY
                         CASE WHEN sorttype = 'name' THEN p.name END,
                         CASE WHEN sorttype = 'id' THEN p.id_product END,
                         CASE WHEN sorttype = 'id_class' THEN p.id_class END;
    END IF;
END;
$$ LANGUAGE plpgsql;


-- ProductSpecification procedures

-- Create ProductSpecification
-- -2 if part not exists, -1 if Product not exists, 0 if ProductSpecification exists, 1 if success
-- Создать()
CREATE OR REPLACE FUNCTION ins_product_specification(r_id_product INTEGER, r_id_part INTEGER, r_quantity INTEGER) RETURNS INTEGER AS $$
DECLARE
    position INTEGER;
    product_exists INTEGER;
    product_specification_exists INTEGER;
    part_exists INTEGER;
BEGIN
    product_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_product);
    part_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_part);
    product_specification_exists := (SELECT COUNT(*) FROM ProductSpecification WHERE id_product = r_id_product AND id_part = r_id_part);
    IF product_exists = 0 THEN
        RETURN -1;
    END IF;
    IF part_exists = 0 THEN
        RETURN -2;
    END IF;
    IF product_specification_exists = 0 THEN
        position := (SELECT COUNT(*) FROM ProductSpecification WHERE id_product = r_id_product) + 1;
        INSERT INTO ProductSpecification (id_product, pos_num, id_part, quantity) VALUES (r_id_product, position, r_id_part, r_quantity);
        RETURN 1;
    ELSE
        RETURN 0;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Delete ProductSpecification by id (and shift positions of those after)
-- 0 if ProductSpecification not exists, 1 if success
-- Удалить()
CREATE OR REPLACE FUNCTION del_product_specification(r_id_product INTEGER, r_pos_num INTEGER) RETURNS INTEGER AS $$
DECLARE
    product_specification_exists INTEGER;
BEGIN
    product_specification_exists := (SELECT COUNT(*) FROM ProductSpecification WHERE id_product = r_id_product AND pos_num = r_pos_num);
    IF product_specification_exists = 0 THEN
        RETURN 0;
    ELSE
        DELETE FROM ProductSpecification WHERE id_product = r_id_product AND pos_num = r_pos_num;
        UPDATE ProductSpecification SET pos_num = pos_num - 1 WHERE id_product = r_id_product AND pos_num > r_pos_num;
        RETURN 1;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Delete ProductSpeicifications by id_product
-- 0 if Product not exists, 1 if success
-- Удалить()
CREATE OR REPLACE FUNCTION del_product_specifications_by_product(r_id_product INTEGER) RETURNS INTEGER AS $$
DECLARE
    product_exists INTEGER;
BEGIN
    product_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_product);
    IF product_exists = 0 THEN
        RETURN 0;
    ELSE
        DELETE FROM ProductSpecification WHERE id_product = r_id_product;
        RETURN 1;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Change the ProductSpecification quantity by id
-- 0 if ProductSpecification not exists, 1 if success
-- Редактировать()
CREATE OR REPLACE FUNCTION update_product_specification_quantity(r_id_product INTEGER, r_pos_num INTEGER, r_quantity INTEGER) RETURNS INTEGER AS $$
DECLARE
    product_specification_exists INTEGER;
BEGIN
    product_specification_exists := (SELECT COUNT(*) FROM ProductSpecification WHERE id_product = r_id_product AND pos_num = r_pos_num);
    IF product_specification_exists = 0 THEN
        RETURN 0;
    ELSE
        UPDATE ProductSpecification SET quantity = r_quantity WHERE id_product = r_id_product AND pos_num = r_pos_num;
        RETURN 1;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Change the ProductSpecification part by id
-- 0 if ProductSpecification not exists, 1 if success
-- Редактировать()
CREATE OR REPLACE FUNCTION update_product_specification_part(r_id_product INTEGER, r_pos_num INTEGER, r_id_part INTEGER) RETURNS INTEGER AS $$
DECLARE
    product_specification_exists INTEGER;
    part_exists INTEGER;
BEGIN
    product_specification_exists := (SELECT COUNT(*) FROM ProductSpecification WHERE id_product = r_id_product AND pos_num = r_pos_num);
    part_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_part);
    IF product_specification_exists = 0 OR part_exists = 0 THEN
        RETURN 0;
    ELSE
        UPDATE ProductSpecification SET id_part = r_id_part WHERE id_product = r_id_product AND pos_num = r_pos_num;
        RETURN 1;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Select ProductSpecification by id
-- 0 if ProductSpecification not exists, ProductSpecification if success
-- Выбрать()
CREATE OR REPLACE FUNCTION select_product_specification(r_id_product INTEGER, r_pos_num INTEGER) RETURNS TABLE (a_id_product INTEGER, a_pos_num INTEGER, a_id_part INTEGER, a_quantity INTEGER) AS $$
DECLARE
    product_specification_exists INTEGER;
BEGIN
    product_specification_exists := (SELECT COUNT(*) FROM ProductSpecification WHERE id_product = r_id_product AND pos_num = r_pos_num);
    IF product_specification_exists = 0 THEN
        RETURN QUERY SELECT 0, 0, 0, 0;
    ELSE
        RETURN QUERY SELECT id_product, pos_num, id_part, quantity FROM ProductSpecification WHERE id_product = r_id_product AND pos_num = r_pos_num;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Select ProductSpecification for Product by product id (recursively)
-- 0 if Product not exists, ProductSpecifications if success
-- ВывестиСпецификацию()
CREATE OR REPLACE FUNCTION select_product_specifications(r_id_product INTEGER, sorttype TEXT) RETURNS TABLE (a_id_product INTEGER, a_pos_num INTEGER, a_id_part INTEGER, a_quantity INTEGER) AS $$
DECLARE
    product_exists INTEGER;
BEGIN
    product_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_product);
    IF product_exists = 0 THEN
        RETURN QUERY SELECT 0, 0, 0, 0;
    ELSE
        RETURN QUERY WITH RECURSIVE specifications AS (
            SELECT id_product, pos_num, id_part, quantity FROM ProductSpecification WHERE id_product = r_id_product
            UNION ALL
            SELECT ps.id_product, ps.pos_num, ps.id_part, ps.quantity FROM ProductSpecification ps, specifications s WHERE ps.id_product = s.id_part
        ) SELECT * FROM specifications
                     ORDER BY
                         CASE WHEN sorttype = 'id_product' THEN id_product END,
                         CASE WHEN sorttype = 'pos_num' THEN pos_num END,
                         CASE WHEN sorttype = 'id_part' THEN id_part END,
                         CASE WHEN sorttype = 'quantity' THEN quantity END;
    END IF;
END;
$$ LANGUAGE plpgsql;


-- Same as above, but with names of id_part from table Product instead of id_part
-- 0 if Product not exists, ProductSpecifications if success
-- ВывестиСпецификацию()
CREATE OR REPLACE FUNCTION select_product_specifications_with_names(r_id_product INTEGER, sorttype TEXT) RETURNS TABLE (a_id_product INTEGER, a_pos_num INTEGER, a_id_part INTEGER, a_name TEXT, a_quantity INTEGER) AS $$
DECLARE
    product_exists INTEGER;
BEGIN
    product_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_product);
    IF product_exists = 0 THEN
        RETURN QUERY SELECT 0, 0, 0, '0', 0;
    ELSE
        RETURN QUERY WITH RECURSIVE specifications AS (
            SELECT id_product, pos_num, id_part, quantity FROM ProductSpecification WHERE id_product = r_id_product
            UNION ALL
            SELECT ps.id_product, ps.pos_num, ps.id_part, ps.quantity FROM ProductSpecification ps, specifications s WHERE ps.id_product = s.id_part
        ) SELECT s.id_product, s.pos_num, s.id_part, p.name, s.quantity FROM specifications s, Product p WHERE s.id_part = p.id_product
                     ORDER BY
                         CASE WHEN sorttype = 'id_product' THEN s.id_product END,
                         CASE WHEN sorttype = 'pos_num' THEN s.pos_num END,
                         CASE WHEN sorttype = 'id_part' THEN s.id_part END,
                         CASE WHEN sorttype = 'quantity' THEN s.quantity END;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- For a Product and certain amount, count all parts needed to make that amount of Product based on ProductSpecification
-- 0 if Product not exists, ProductSpecifications if success
-- ПосчитатьСпецификацию()
CREATE OR REPLACE FUNCTION count_product_specifications(r_id_product INTEGER, r_amount INTEGER) RETURNS TABLE (a_id_product INTEGER, a_pos_num INTEGER, a_id_part INTEGER, a_quantity INTEGER) AS $$
DECLARE
    product_exists INTEGER;
BEGIN
    product_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_product);
    IF product_exists = 0 THEN
        RETURN QUERY SELECT 0, 0, 0, 0;
    ELSE
        RETURN QUERY WITH RECURSIVE specifications AS (
            SELECT id_product, pos_num, id_part, quantity FROM ProductSpecification WHERE id_product = r_id_product
            UNION ALL
            SELECT ps.id_product, ps.pos_num, ps.id_part, ps.quantity FROM ProductSpecification ps, specifications s WHERE ps.id_product = s.id_part
        ) SELECT id_product, pos_num, id_part, quantity * r_amount FROM specifications
                     ORDER BY id_product, pos_num;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Same as above, but with names of id_part from table Product instead of id_part
-- 0 if Product not exists, ProductSpecifications if success
-- ПосчитатьСпецификацию()
CREATE OR REPLACE FUNCTION count_product_specifications_with_names(r_id_product INTEGER, r_amount INTEGER) RETURNS TABLE (a_id_product INTEGER, a_pos_num INTEGER, a_id_part INTEGER, a_name TEXT, a_quantity INTEGER) AS $$
DECLARE
    product_exists INTEGER;
BEGIN
    product_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_product);
    IF product_exists = 0 THEN
        RETURN QUERY SELECT 0, 0, 0, '0', 0;
    ELSE
        RETURN QUERY WITH RECURSIVE specifications AS (
            SELECT id_product, pos_num, id_part, quantity FROM ProductSpecification WHERE id_product = r_id_product
            UNION ALL
            SELECT ps.id_product, ps.pos_num, ps.id_part, ps.quantity FROM ProductSpecification ps, specifications s WHERE ps.id_product = s.id_part
        ) SELECT s.id_product, s.pos_num, s.id_part, p.name, s.quantity * r_amount FROM specifications s, Product p WHERE s.id_part = p.id_product
                     ORDER BY id_product, pos_num;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Wrap the above function to return only unique parts by their id with sum of quantities
-- 0 if Product not exists, ProductSpecifications if success
-- ПосчитатьСпецификацию()
CREATE OR REPLACE FUNCTION count_product_specifications_unique(r_id_product INTEGER, r_amount INTEGER) RETURNS TABLE (a_id_part INTEGER, a_quantity BIGINT) AS $$
DECLARE
    product_exists INTEGER;
BEGIN
    product_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_product);
    IF product_exists = 0 THEN
        RETURN QUERY SELECT 0, 0;
    ELSE
        RETURN QUERY WITH RECURSIVE specifications AS (
            SELECT id_product, pos_num, id_part, quantity FROM ProductSpecification WHERE id_product = r_id_product
            UNION ALL
            SELECT ps.id_product, ps.pos_num, ps.id_part, ps.quantity FROM ProductSpecification ps, specifications s WHERE ps.id_product = s.id_part
        ) SELECT id_part, SUM(quantity * r_amount) FROM specifications GROUP BY id_part ORDER BY id_part;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Same as above, but with names of id_part from table Product instead of id_part
-- 0 if Product not exists, ProductSpecifications if success
-- ПосчитатьСпецификацию()
CREATE OR REPLACE FUNCTION count_product_specifications_unique_with_names(r_id_product INTEGER, r_amount INTEGER) RETURNS TABLE (a_id_part INTEGER, a_name VARCHAR(255), a_quantity BIGINT) AS $$
DECLARE
    product_exists INTEGER;
BEGIN
    product_exists := (SELECT COUNT(*) FROM Product WHERE id_product = r_id_product);
    IF product_exists = 0 THEN
        RETURN QUERY SELECT 0, '0', 0;
    ELSE
        RETURN QUERY WITH RECURSIVE specifications AS (
            SELECT id_product, pos_num, id_part, quantity FROM ProductSpecification WHERE id_product = r_id_product
            UNION ALL
            SELECT ps.id_product, ps.pos_num, ps.id_part, ps.quantity FROM ProductSpecification ps, specifications s WHERE ps.id_product = s.id_part
        ) SELECT s.id_part, p.name, SUM(s.quantity * r_amount) FROM specifications s, Product p WHERE s.id_part = p.id_product GROUP BY s.id_part, p.name ORDER BY s.id_part;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Debug functions
-- Redo tables
CREATE OR REPLACE FUNCTION redo() RETURNS VOID AS $$
BEGIN
    DROP TABLE IF EXISTS ProductSpecification;
    DROP TABLE IF EXISTS Product;
    DROP TABLE IF EXISTS ProductClassifier;
    DROP TABLE IF EXISTS EI;

    CREATE TABLE EI (
        id_ei SERIAL PRIMARY KEY,
        name TEXT NOT NULL,
        short_name TEXT NOT NULL,
        code INTEGER NOT NULL
    );

    CREATE TABLE ProductClassifier (
        id_class SERIAL PRIMARY KEY,
        name TEXT NOT NULL,
        base_id INTEGER NOT NULL,
        FOREIGN KEY (base_id) REFERENCES EI(id_ei) ON DELETE CASCADE ON UPDATE CASCADE,
        parent_id INTEGER,
        FOREIGN KEY (parent_id) REFERENCES ProductClassifier(id_class) ON DELETE CASCADE ON UPDATE CASCADE
    );

    CREATE TABLE Product (
        id_product SERIAL PRIMARY KEY,
        name TEXT NOT NULL,
        id_class INTEGER NOT NULL,
        FOREIGN KEY (id_class) REFERENCES ProductClassifier(id_class) ON DELETE CASCADE ON UPDATE CASCADE
    );

    CREATE TABLE ProductSpecification (
        id_product INTEGER NOT NULL,
        FOREIGN KEY (id_product) REFERENCES Product(id_product) ON DELETE CASCADE ON UPDATE CASCADE,
        pos_num INTEGER NOT NULL,
        id_part INTEGER NOT NULL,
        FOREIGN KEY (id_part) REFERENCES Product(id_product) ON DELETE CASCADE ON UPDATE CASCADE,
        quantity INTEGER NOT NULL,
        CONSTRAINT pk_product_specification PRIMARY KEY (id_product, pos_num)
    );

    PERFORM ins_ei('штука', 'шт.', 1);
    PERFORM ins_ei('грамм', 'г', 2);
    PERFORM ins_ei('сантиметр', 'см', 3);

-- ProductClassifier
    PERFORM ins_pc('Изделие', 1, NULL);
    PERFORM ins_pc('Электронное изделие', 1, 1);
    PERFORM ins_pc('Микроконтроллер', 1, 2);
    PERFORM ins_pc('Arduino', 1, 3);
    PERFORM ins_pc('Raspberry Pi', 1, 3);

    PERFORM ins_pc('Составной элемент', 1, 3);
    PERFORM ins_pc('Печатная плата', 1, 6);
    PERFORM ins_pc('Резистор', 1, 6);
    PERFORM ins_pc('Контакт', 1, 6);
    PERFORM ins_pc('Модуль', 1, 6);



-- Product
    PERFORM ins_product('Arduino Uno', 4);
    PERFORM ins_product('Arduino Pro Mini', 4);

    PERFORM ins_product('Raspberry Pi Zero', 5);
    PERFORM ins_product('Raspberry Pi Pico', 5);

-- Составной элемент - Печатная плата - Односторонняя плата и Многослойная плата
    PERFORM ins_product('Односторонняя плата', 7);
    PERFORM ins_product('Многослойная плата', 7);

-- Составной элемент - Резистор - Постоянный резистор и Переменный резистор
    PERFORM ins_product('Постоянный резистор', 8);
    PERFORM ins_product('Переменный резистор', 8);

-- Составной элемент - Контакт - Клемма и Разъем
    PERFORM ins_product('Клемма', 9);
    PERFORM ins_product('Разъем', 9);

-- Составной элемент - Модуль - USB-контроллер и Wi-Fi модуль
    PERFORM ins_product('USB-контроллер', 10);
    PERFORM ins_product('Wi-Fi модуль', 10);


    -- ProductSpecification
-- Arduino Uno - Многослойная плата - 1 шт., Постоянный резистор - 10 шт., Клемма - 20 шт., Разъем - 5 шт., USB-контроллер - 1 шт.
    PERFORM ins_product_specification(1, 6, 1);
    PERFORM ins_product_specification(1, 7, 10);
    PERFORM ins_product_specification(1, 9, 20);
    PERFORM ins_product_specification(1, 10, 5);
    PERFORM ins_product_specification(1, 11, 1);
    PERFORM ins_product_specification(11, 8, 2);
    PERFORM ins_product_specification(11, 5, 1);
    PERFORM ins_product_specification(5, 10, 2);

-- Arduino Pro Mini - Односторонняя плата - 1 шт., Постоянный резистор - 7 шт., Разъем - 25 шт., USB-контроллер - 1 шт.
    PERFORM ins_product_specification(2, 5, 1);
    PERFORM ins_product_specification(2, 7, 7);
    PERFORM ins_product_specification(2, 10, 25);
    PERFORM ins_product_specification(2, 11, 1);

-- Raspberry Pi Zero - Многослойная плата - 1 шт., Постоянный резистор - 12 шт., Клемма - 15 шт., Разъем - 5 шт., USB-контроллер - 1 шт., Wi-Fi модуль - 1 шт.
    PERFORM ins_product_specification(3, 6, 1);
    PERFORM ins_product_specification(3, 7, 12);
    PERFORM ins_product_specification(3, 9, 15);
    PERFORM ins_product_specification(3, 10, 5);
    PERFORM ins_product_specification(3, 11, 1);
    PERFORM ins_product_specification(3, 12, 1);

-- Raspberry Pi Pico - Односторонняя плата - 1 шт., Постоянный резистор - 8 шт., Разъем - 25 шт., USB-контроллер - 1 шт., Wi-Fi модуль - 1 шт.
    PERFORM ins_product_specification(4, 5, 1);
    PERFORM ins_product_specification(4, 7, 8);
    PERFORM ins_product_specification(4, 10, 25);
    PERFORM ins_product_specification(4, 11, 1);
    PERFORM ins_product_specification(4, 12, 1);

END;
$$ LANGUAGE plpgsql;

SELECT redo();
-- Fill the database with test data using created functions

-- EI
SELECT ins_ei('штука', 'шт.', 1);
SELECT ins_ei('грамм', 'г', 2);
SELECT ins_ei('сантиметр', 'см', 3);

-- ProductClassifier
SELECT ins_pc('Изделие', 1, NULL);
SELECT ins_pc('Электронное изделие', 1, 1);
SELECT ins_pc('Микроконтроллер', 1, 2);
SELECT ins_pc('Arduino', 1, 3);
SELECT ins_pc('Raspberry Pi', 1, 3);

SELECT ins_pc('Составной элемент', 1, 3);
SELECT ins_pc('Печатная плата', 1, 6);
SELECT ins_pc('Резистор', 1, 6);
SELECT ins_pc('Контакт', 1, 6);
SELECT ins_pc('Модуль', 1, 6);



-- Product
SELECT ins_product('Arduino Uno', 4);
SELECT ins_product('Arduino Pro Mini', 4);

SELECT ins_product('Raspberry Pi Zero', 5);
SELECT ins_product('Raspberry Pi Pico', 5);

-- Составной элемент - Печатная плата - Односторонняя плата и Многослойная плата
SELECT ins_product('Односторонняя плата', 7);
SELECT ins_product('Многослойная плата', 7);

-- Составной элемент - Резистор - Постоянный резистор и Переменный резистор
SELECT ins_product('Постоянный резистор', 8);
SELECT ins_product('Переменный резистор', 8);

-- Составной элемент - Контакт - Клемма и Разъем
SELECT ins_product('Клемма', 9);
SELECT ins_product('Разъем', 9);

-- Составной элемент - Модуль - USB-контроллер и Wi-Fi модуль
SELECT ins_product('USB-контроллер', 10);
SELECT ins_product('Wi-Fi модуль', 10);


-- ProductSpecification
-- Arduino Uno - Многослойная плата - 1 шт., Постоянный резистор - 10 шт., Клемма - 20 шт., Разъем - 5 шт., USB-контроллер - 1 шт.
SELECT ins_product_specification(1, 6, 1);
SELECT ins_product_specification(1, 7, 10);
SELECT ins_product_specification(1, 9, 20);
SELECT ins_product_specification(1, 10, 5);
SELECT ins_product_specification(1, 11, 1);
SELECT ins_product_specification(11, 8, 2);
SELECT ins_product_specification(11, 5, 1);
SELECT ins_product_specification(5, 10, 2);

-- Arduino Pro Mini - Односторонняя плата - 1 шт., Постоянный резистор - 7 шт., Разъем - 25 шт., USB-контроллер - 1 шт.
SELECT ins_product_specification(2, 5, 1);
SELECT ins_product_specification(2, 7, 7);
SELECT ins_product_specification(2, 10, 25);
SELECT ins_product_specification(2, 11, 1);

-- Raspberry Pi Zero - Многослойная плата - 1 шт., Постоянный резистор - 12 шт., Клемма - 15 шт., Разъем - 5 шт., USB-контроллер - 1 шт., Wi-Fi модуль - 1 шт.
SELECT ins_product_specification(3, 6, 1);
SELECT ins_product_specification(3, 7, 12);
SELECT ins_product_specification(3, 9, 15);
SELECT ins_product_specification(3, 10, 5);
SELECT ins_product_specification(3, 11, 1);
SELECT ins_product_specification(3, 12, 1);

-- Raspberry Pi Pico - Односторонняя плата - 1 шт., Постоянный резистор - 8 шт., Разъем - 25 шт., USB-контроллер - 1 шт., Wi-Fi модуль - 1 шт.
SELECT ins_product_specification(4, 5, 1);
SELECT ins_product_specification(4, 7, 8);
SELECT ins_product_specification(4, 10, 25);
SELECT ins_product_specification(4, 11, 1);
SELECT ins_product_specification(4, 12, 1);