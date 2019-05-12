/*#1 Finds the top 10 most expensive products follow Product_ID descending order. */
SELECT TOP 10  ProductID,Name,Quantity
  FROM Product 
   Order by ProductID desc;

 /*#2 Finds the number of customers in every city*/
 SELECT count ( CustomerID ) as "Number of customer ",Address
  FROM Customer
 group by Address
 Order by Address asc;

/*#3	Finds the products whose list prices are greater than or equal to the maximum list price of any product brand*/
SELECT Name, ProductID,Price
FROM Product
WHERE Price >= 3000 (
        SELECT AVG (Price)
        FROM Product
        GROUP BY ProductID )
