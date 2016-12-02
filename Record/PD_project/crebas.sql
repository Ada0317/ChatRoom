/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2016/12/2 21:02:22                           */
/*==============================================================*/


drop table if exists Collection;

drop table if exists User;

drop table if exists collection_entry;

/*==============================================================*/
/* Table: Collection                                            */
/*==============================================================*/
create table Collection
(
   name                 text not null,
   collection_id        int not null,
   user_id              int,
   primary key (collection_id)
);

/*==============================================================*/
/* Table: User                                                  */
/*==============================================================*/
create table User
(
   user_id              int not null,
   nick_name            text not null,
   pass_word            text not null,
   avatar               int not null,
   primary key (user_id)
);

alter table User comment 'User data';

/*==============================================================*/
/* Table: collection_entry                                      */
/*==============================================================*/
create table collection_entry
(
   user_id              int not null,
   collection_id        int not null,
   primary key (user_id, collection_id)
);

alter table Collection add constraint FK_user_collection foreign key (user_id)
      references User (user_id) on delete restrict on update restrict;

alter table collection_entry add constraint FK_collection_entry foreign key (user_id)
      references User (user_id) on delete restrict on update restrict;

alter table collection_entry add constraint FK_collection_entry2 foreign key (collection_id)
      references Collection (collection_id) on delete restrict on update restrict;

