use sourcing;

CREATE TABLE dbo.Stock_Source  
   (rec_id bigint PRIMARY KEY IDENTITY ,
	finance_source varchar(225)
	 )  
GO 

insert into dbo.Stock_Source(finance_source) values('Yahoo Finance');
insert into dbo.Stock_Source(finance_source) values('Temp');


 CREATE TABLE dbo.Stock_Company_Info  
   (rec_id bigint PRIMARY KEY IDENTITY ,  
    company_id bigint , 
	company_name varchar(225),
	company_symbol varchar(225) Not Null,
	last_update_date datetime,  
    stock_source_id bigint FOREIGN KEY REFERENCES dbo.Stock_Source(rec_id))  
GO 

