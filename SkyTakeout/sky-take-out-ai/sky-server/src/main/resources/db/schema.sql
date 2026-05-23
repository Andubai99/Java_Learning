create table if not exists employee (
    id bigint primary key auto_increment,
    username varchar(32) not null unique,
    password varchar(64) not null,
    name varchar(32) not null,
    phone varchar(20),
    sex varchar(2),
    id_number varchar(32),
    status tinyint not null default 1,
    create_time datetime not null,
    update_time datetime not null,
    create_user bigint,
    update_user bigint
);

create table if not exists category (
    id bigint primary key auto_increment,
    type tinyint not null,
    name varchar(32) not null unique,
    sort int not null default 0,
    status tinyint not null default 1,
    create_time datetime not null,
    update_time datetime not null,
    create_user bigint,
    update_user bigint
);

create table if not exists dish (
    id bigint primary key auto_increment,
    name varchar(64) not null,
    category_id bigint not null,
    price decimal(10,2) not null,
    image varchar(255),
    description varchar(255),
    status tinyint not null default 0,
    create_time datetime not null,
    update_time datetime not null,
    create_user bigint,
    update_user bigint
);

create table if not exists dish_flavor (
    id bigint primary key auto_increment,
    dish_id bigint not null,
    name varchar(32) not null,
    value varchar(255) not null,
    create_time datetime not null,
    update_time datetime not null,
    create_user bigint,
    update_user bigint
);

create table if not exists setmeal (
    id bigint primary key auto_increment,
    name varchar(64) not null,
    category_id bigint not null,
    price decimal(10,2) not null,
    image varchar(255),
    description varchar(255),
    status tinyint not null default 0,
    create_time datetime not null,
    update_time datetime not null,
    create_user bigint,
    update_user bigint
);

create table if not exists setmeal_dish (
    id bigint primary key auto_increment,
    setmeal_id bigint not null,
    dish_id bigint not null,
    name varchar(64) not null,
    price decimal(10,2) not null,
    copies int not null default 1,
    create_time datetime not null,
    update_time datetime not null,
    create_user bigint,
    update_user bigint
);

create table if not exists user (
    id bigint primary key auto_increment,
    openid varchar(64) not null unique,
    name varchar(32),
    phone varchar(20),
    avatar varchar(255),
    create_time datetime not null,
    update_time datetime not null,
    create_user bigint,
    update_user bigint
);

create table if not exists address_book (
    id bigint primary key auto_increment,
    user_id bigint not null,
    consignee varchar(32) not null,
    phone varchar(20) not null,
    province_name varchar(32),
    city_name varchar(32),
    district_name varchar(32),
    detail varchar(255) not null,
    label varchar(32),
    is_default tinyint not null default 0,
    create_time datetime not null,
    update_time datetime not null,
    create_user bigint,
    update_user bigint
);

create table if not exists shopping_cart (
    id bigint primary key auto_increment,
    user_id bigint not null,
    dish_id bigint,
    setmeal_id bigint,
    name varchar(64) not null,
    image varchar(255),
    amount decimal(10,2) not null,
    number int not null default 1,
    create_time datetime not null,
    update_time datetime not null,
    create_user bigint,
    update_user bigint
);

create table if not exists orders (
    id bigint primary key auto_increment,
    number varchar(64) not null unique,
    status tinyint not null,
    user_id bigint not null,
    address_book_id bigint,
    order_time datetime not null,
    checkout_time datetime,
    pay_method tinyint,
    pay_status tinyint not null default 0,
    amount decimal(10,2) not null,
    remark varchar(255),
    phone varchar(20),
    address varchar(255),
    consignee varchar(32),
    cancel_reason varchar(255),
    rejection_reason varchar(255),
    cancel_time datetime,
    estimated_delivery_time datetime,
    delivery_status tinyint not null default 1,
    delivery_time datetime,
    pack_amount decimal(10,2) not null default 0,
    tableware_number int not null default 0,
    tableware_status tinyint not null default 1,
    create_time datetime not null,
    update_time datetime not null,
    create_user bigint,
    update_user bigint
);

create table if not exists order_detail (
    id bigint primary key auto_increment,
    name varchar(64) not null,
    image varchar(255),
    order_id bigint not null,
    dish_id bigint,
    setmeal_id bigint,
    dish_flavor varchar(255),
    number int not null,
    amount decimal(10,2) not null,
    create_time datetime not null,
    update_time datetime not null,
    create_user bigint,
    update_user bigint
);

create index if not exists idx_category_type on category(type);
create index if not exists idx_dish_category on dish(category_id);
create index if not exists idx_setmeal_category on setmeal(category_id);
create index if not exists idx_address_user on address_book(user_id);
create index if not exists idx_cart_user on shopping_cart(user_id);
create index if not exists idx_orders_user on orders(user_id);
create index if not exists idx_orders_status on orders(status);
create index if not exists idx_order_detail_order on order_detail(order_id);
