CREATE TABLE Freelancer (
    FreelancerID int Not Null PRIMARY KEY,
    Freelancer_First_Name VARCHAR(30) NOT NULL,
    Freelancer_Last_Name VARCHAR(30) NOT NULL,
    Skillset VARCHAR(20) NOT NULL, 
    HourlyRate DECIMAL(10,2) NOT NULL
);
