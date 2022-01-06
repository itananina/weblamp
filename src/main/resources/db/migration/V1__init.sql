CREATE TABLE users (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  username TEXT NOT NULL,
  password TEXT NOT NULL,
  email TEXT NOT NULL
);
CREATE TABLE roles (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL
);
CREATE TABLE users_roles (
  user_id INTEGER NOT NULL,
  role_id INTEGER NOT NULL,
  PRIMARY KEY(user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (role_id) REFERENCES roles(id)
);
CREATE TABLE authorities (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL
);
create table roles_authorities (
    role_id INTEGER NOT NULL,
    auth_id INTEGER NOT NULL,
    primary key (role_id, auth_id),
    foreign key (role_id) references roles (id),
    foreign key (auth_id) references authorities (id)
);

CREATE TABLE products (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  title TEXT NOT NULL,
  price INTEGER NOT NULL
);
CREATE TABLE orders (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id INTEGER NOT NULL,
  status TEXT NOT NULL,
  total INTEGER,
  FOREIGN KEY (user_id) REFERENCES users(id)
);
create table orders_products (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    amount INTEGER,
    order_id INTEGER NOT NULL,
    prod_id INTEGER NOT NULL,
    foreign key (order_id) references orders (id),
    foreign key (prod_id) references products (id)
);

alter table orders_products add column price_per_product INTEGER NOT NULL DEFAULT 0;

insert into roles (name)
values
('ROLE_USER'), ('ROLE_ADMIN');

insert into authorities (name)
values
('READ_USER_INFO'), ('READ_ALL_USERS');

insert into users (username, password, email)
values
('user', '$2a$12$DluBTYzdAtzk/IDeJ/zb1uPeTFcUfPpRJSvz0FabBk.ZVuQ4IJSye', 'user@gmail.com'),
('admin', '$2a$12$DluBTYzdAtzk/IDeJ/zb1uPeTFcUfPpRJSvz0FabBk.ZVuQ4IJSye', 'admin@gmail.com'),
('eera', '$2a$12$DluBTYzdAtzk/IDeJ/zb1uPeTFcUfPpRJSvz0FabBk.ZVuQ4IJSye', 'eera@gmail.com'),
('autotestuser', '$2a$12$DluBTYzdAtzk/IDeJ/zb1uPeTFcUfPpRJSvz0FabBk.ZVuQ4IJSye', 'autotestuser@gmail.com')
;

insert into users_roles (user_id, role_id)
values
(1, 1), (2, 2), (3,1), (4,1);

insert into roles_authorities (role_id, auth_id)
values
(1, 1), (2, 1), (2, 2);

insert into products (title, price)
values
('Стол', 500),
('Стул', 800),
('Комод', 900),
('Подушка', 300),
('Картина', 300),
('Полка', 400),
('Ящик', 500),
('Коробка', 1500),
('Табуретка', 200),
('Матрас', 1000),
('Шкаф', 500),
('Стеллаж', 800),
('Вешалка', 900),
('Одеяло', 300),
('Лампочка', 300),
('Торшер', 400),
('Люстра', 500),
('Светильник', 1500),
('Горшок', 200),
('Плед', 1000);

