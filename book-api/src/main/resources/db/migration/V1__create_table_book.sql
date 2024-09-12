create table if not exists tb_book (
	id bigint not null AUTO_INCREMENT, 
	title varchar(500) not null, 
	author varchar(500), 
	main_genre varchar(255), 
	sub_genre varchar(255), 
	type varchar(255), 
	currency varchar(10), 
	price float(53), 
	rating float(53),
	people_rated integer, 
	url varchar(500), 
	primary key (id)
);
create index if not exists IDX_book_author on tb_book (author);
create index if not exists IDX_book_main_genre on tb_book (main_genre);