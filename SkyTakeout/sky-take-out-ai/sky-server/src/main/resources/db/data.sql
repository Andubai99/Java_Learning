insert into employee (id, username, password, name, phone, sex, id_number, status, create_time, update_time, create_user, update_user)
select 1, 'admin', '123456', '系统管理员', '13800000000', '1', '110101199001010000', 1, current_timestamp, current_timestamp, 1, 1
where not exists (select 1 from employee where username = 'admin');

insert into category (id, type, name, sort, status, create_time, update_time, create_user, update_user)
select 1, 1, '热销菜品', 1, 1, current_timestamp, current_timestamp, 1, 1
where not exists (select 1 from category where id = 1);

insert into category (id, type, name, sort, status, create_time, update_time, create_user, update_user)
select 2, 2, '商务套餐', 2, 1, current_timestamp, current_timestamp, 1, 1
where not exists (select 1 from category where id = 2);

insert into dish (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user)
select 10, '招牌黄焖鸡米饭', 1, 28.00, '', '经典热销单品', 1, current_timestamp, current_timestamp, 1, 1
where not exists (select 1 from dish where id = 10);

insert into dish_flavor (id, dish_id, name, value, create_time, update_time, create_user, update_user)
select 11, 10, '辣度', '不辣,微辣,中辣', current_timestamp, current_timestamp, 1, 1
where not exists (select 1 from dish_flavor where id = 11);

insert into setmeal (id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user)
select 20, '双人工作餐', 2, 58.00, '', '两份主食加汤品', 1, current_timestamp, current_timestamp, 1, 1
where not exists (select 1 from setmeal where id = 20);

insert into setmeal_dish (id, setmeal_id, dish_id, name, price, copies, create_time, update_time, create_user, update_user)
select 21, 20, 10, '招牌黄焖鸡米饭', 28.00, 2, current_timestamp, current_timestamp, 1, 1
where not exists (select 1 from setmeal_dish where id = 21);
