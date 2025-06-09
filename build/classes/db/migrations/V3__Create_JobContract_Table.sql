CREATE TABLE JobContract (
    ContractID INT NOT NULL PRIMARY KEY,
    ClientID INT NOT NULL,
    FreelancerID INT NOT NULL,
    JobDescription VARCHAR(255) NOT NULL,
    PaymentStatus VARCHAR(20) NOT NULL,
    FOREIGN KEY (ClientID) REFERENCES Client(ClientID),
    FOREIGN KEY (FreelancerID) REFERENCES Freelancer(FreelancerID)
);
