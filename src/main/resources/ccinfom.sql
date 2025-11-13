SELECT * FROM customer;

SELECT * FROM location;
INSERT INTO location (locationID,cityName,provinceName,RegionName) VALUES (101,"BulCity","BulProvince","BulRegion");

SELECT * FROM branch;
INSERT INTO branch (branchID,branchName,branchAddress,locationID) VALUES (10001,"BRentals","Bulacan",101); 
INSERT INTO branch (branchID,branchName,branchAddress,locationID) VALUES (10002,"CRentals","Caloocan",101); 

SELECT * FROM bike;
INSERT INTO bike (bikeID,branchIDNum,bikeAvailability,bikeModel,hourlyRate,dailyRate) VALUES (120001,10001,"Yes","Red Bike",30,450);

Select * from payment;

SELECT * FROM reservation;
INSERT INTO reservation (reservationReferenceNum,customerAccID, bikeID, branchID,reservationDate, startDate, endDate, dateReturned, reservationStatus) 
VALUES (130001,100001, 120001, 10001,"2025-11-11 12:00:00", "2025-11-12 14:30:00", "2025-11-13 16:45:00","2025-11-13 16:45:00" ,'ongoing');
INSERT INTO reservation (reservationReferenceNum,customerAccID, bikeID, branchID,reservationDate, startDate, endDate, dateReturned, reservationStatus) 
VALUES (130002,100002, 120001, 10001,"2025-11-12 12:00:00", "2025-11-13 14:00:00", "2025-11-14 16:00:00","2025-11-14 16:00:00" ,'ongoing');

