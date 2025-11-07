BEGIN;

CREATE TABLE IF NOT EXISTS item (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name TEXT NOT NULL,
price NUMERIC(10,2) NOT NULL CHECK (price >= 0),
supermarket TEXT,
notes TEXT
);

INSERT INTO item (name, price, supermarket, notes) VALUES
  ('Milk 1L',                   1.19, 'Lidl',       'Semi-skimmed'),
  ('Whole Wheat Bread',         0.99, 'Aldi',       'Sliced, 500g'),
  ('Eggs 12-pack',              2.49, 'Carrefour',  'Free-range'),
  ('Olive Oil 1L',              6.99, 'Coop',       'Extra virgin'),
  ('Spaghetti 500g',            0.79, 'Esselunga',  'Durum wheat'),
  ('Tomatoes 1kg',              2.10, 'Conad',      'Ripe'),
  ('Chicken Breast 1kg',        7.50, 'Carrefour',  'Fresh'),
  ('Arborio Rice 1kg',          2.39, 'Conad',      'For risotto'),
  ('Ground Coffee 250g',        3.79, 'Coop',       'Espresso roast'),
  ('Yogurt Natural 4x125g',     1.49, 'Lidl',       'Plain'),
  ('Apples Gala 1kg',           1.99, 'Esselunga',  'Class 1'),
  ('Butter 250g',               2.10, 'Conad',      'Unsalted'),
  ('Parmigiano Reggiano 200g',  3.99, 'Coop',       '24 months'),
  ('Tuna Cans 3x80g',           2.69, 'Carrefour',  'In olive oil'),
  ('Toilet Paper 12 rolls',     3.99, 'Pam',        '3-ply');

COMMIT;