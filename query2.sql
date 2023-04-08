alter table nhresident 
   add column facility_id bigint;                    

alter table nhresident 
   add column user_id bigint;                      

alter table notice 
   add column nc_id bigint;                       

alter table notice 
   add column facility_id bigint;                      

alter table notice 
   add column target_id bigint;
   
alter table notice 
   add column user_id bigint;
   
alter table nhresident 
   add constraint FKnc3cjtrturjr63jfahcvirmtr 
   foreign key (facility_id) 
   references facility (facility_id) ON DELETE CASCADE;
   
alter table nhresident 
   add constraint FKdnmpm247w9y2f20hfs4ep3494 
   foreign key (user_id) 
   references users (user_id) ON DELETE CASCADE;
   
alter table notice 
   add constraint FK6mwldfvbmdh6mms0gv6vc51sw 
   foreign key (nc_id) 
   references notice_content (nc_id) ON DELETE CASCADE;
   
alter table notice 
   add constraint FK1hh8ndxgu1rocdd32uqp7d8wx 
   foreign key (facility_id) 
   references facility (facility_id);
   
alter table notice 
   add constraint FK95xlp7hphvnvuq6sjjq1jck7u 
   foreign key (target_id) 
   references users (user_id);
   
alter table notice 
   add constraint FK6hu3mfrsmpbqjk44w2fq5t5us 
   foreign key (user_id) 
   references users (user_id);
   
   
