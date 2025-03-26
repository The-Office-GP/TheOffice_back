CREATE TABLE IF NOT EXISTS User
(
    id       INT AUTO_INCREMENT     NOT NULL PRIMARY KEY,
    email    VARCHAR(255) UNIQUE    NOT NULL,
    username VARCHAR(255)           NOT NULL,
    password VARCHAR(255)           NOT NULL,
    role     ENUM ('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    wallet    DECIMAL(10, 2)
);

CREATE TABLE IF NOT EXISTS Company
(
    id            INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    sector       VARCHAR(255)       NOT NULL,
    name          VARCHAR(35)       NOT NULL,
    creation_date DATE,
    popularity    INT                NOT NULL,
    id_local INT NOT NULL,
    id_machine INT,
    id_user       INT,
    FOREIGN KEY (id_user) REFERENCES User (id)
);

CREATE TABLE IF NOT EXISTS Loan
(
    id            INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    loan_amount   DECIMAL(10, 2)     NOT NULL,
    interest_rate DECIMAL(5, 2)      NOT NULL,
    duration      INT                NOT NULL,
    rest DECIMAL(10, 2)     NOT NULL,
    id_user       INT NOT NULL,
    FOREIGN KEY (id_user) REFERENCES User (id)
);

CREATE TABLE IF NOT EXISTS Supplier
(
    id         INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name       VARCHAR(255)       NOT NULL,
    price      DECIMAL(10, 2)     NOT NULL,
    quality    VARCHAR(100)       NOT NULL,
    id_company INT,
    FOREIGN KEY (id_company) REFERENCES Company (id)
);

CREATE TABLE IF NOT EXISTS Cycle
(
    id           INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    step    INT                NOT NULL,
    production_speed    INT                NOT NULL,
    priority_production INT                NOT NULL,
    priority_marketing INT                NOT NULL,
    count_good_sell         INT                NOT NULL,
    count_bad_sell         INT                NOT NULL,
    id_company   INT,
    trend   VARCHAR(50),
    FOREIGN KEY (id_company) REFERENCES Company (id)
);

CREATE TABLE IF NOT EXISTS MachineInCompany
(
    id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    id_machine INT NOT NULL,
    id_company INT,
    FOREIGN KEY (id_company) REFERENCES Company (id)
);

CREATE TABLE IF NOT EXISTS Employee
(
    id     INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name   VARCHAR(255)       NOT NULL,
    gender   ENUM ('HOMME', 'FEMME') NOT NULL,
    seniority INT                NOT NULL DEFAULT 0,
    salary DECIMAL(10, 2)     NOT NULL,
    level  INT                NOT NULL,
    mood ENUM ('MAUVAISE', 'BONNE', 'NEUTRE', 'HEUREUSE', 'BOF') NOT NULL DEFAULT 'BONNE',
    status   ENUM ('ACTIF', 'CONGES', 'GREVE') NOT NULL DEFAULT 'ACTIF',
    job ENUM ('MARKETING', 'VENTE', 'PRODUCTION') NOT NULL,
    health INT NOT NULL,
    priority_action ENUM ('Product1', 'Product2', 'Product3', 'Product4') NOT NULL DEFAULT 'Product1',
    image VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS EmployeeInCompany
(
    id_employee INT,
    id_company  INT,
    PRIMARY KEY (id_employee, id_company),
    FOREIGN KEY (id_employee) REFERENCES Employee (id),
    FOREIGN KEY (id_company) REFERENCES Company (id)
);

CREATE TABLE IF NOT EXISTS Event
(
    id     INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    recurrence INT DEFAULT 0,
    image VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS CompanyEvent
(
    id_company INT,
    id_event   INT,
    PRIMARY KEY (id_company, id_event),
    FOREIGN KEY (id_company) REFERENCES Company (id),
    FOREIGN KEY (id_event) REFERENCES Event (id)
);

CREATE TABLE IF NOT EXISTS StockMaterial
(
    id            INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name          VARCHAR(255)       NOT NULL,
    quantity_low      INT                NOT NULL,
    quantity_mid      INT                NOT NULL,
    quantity_high      INT                NOT NULL,
    id_company    INT,
    FOREIGN KEY (id_company) REFERENCES Company (id)
);

CREATE TABLE IF NOT EXISTS StockFinalMaterial
(
    id                INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name              VARCHAR(255)       NOT NULL,
    quantity_low          INT                NOT NULL,
    quantity_mid          INT                NOT NULL,
    quantity_high          INT                NOT NULL,
    proportion_product INT               NOT NULL,
    quantity_to_product INT             NOT NULL,
    month_production  INT                NOT NULL,
    sell              INT                NOT NULL,
    price        INT                NOT NULL,
    id_company        INT,
    FOREIGN KEY (id_company) REFERENCES Company (id)
);

CREATE TABLE IF NOT EXISTS Statistic
(
    id                 INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    year               INT NOT NULL,
    month              INT NOT NULL,
    product1_low_qty_sell   INT NOT NULL,
    product1_mid_qty_sell   INT NOT NULL,
    product1_high_qty_sell  INT NOT NULL,
    product2_low_qty_sell   INT NOT NULL,
    product2_mid_qty_sell   INT NOT NULL,
    product2_high_qty_sell  INT NOT NULL,
    product3_low_qty_sell   INT NOT NULL,
    product3_mid_qty_sell   INT NOT NULL,
    product3_high_qty_sell  INT NOT NULL,
    product4_low_qty_sell   INT NOT NULL,
    product4_mid_qty_sell   INT NOT NULL,
    product4_high_qty_sell  INT NOT NULL,
    product1_low_qty_prod   INT NOT NULL,
    product1_mid_qty_prod   INT NOT NULL,
    product1_high_qty_prod  INT NOT NULL,
    product2_low_qty_prod   INT NOT NULL,
    product2_mid_qty_prod   INT NOT NULL,
    product2_high_qty_prod  INT NOT NULL,
    product3_low_qty_prod   INT NOT NULL,
    product3_mid_qty_prod   INT NOT NULL,
    product3_high_qty_prod  INT NOT NULL,
    product4_low_qty_prod   INT NOT NULL,
    product4_mid_qty_prod   INT NOT NULL,
    product4_high_qty_prod  INT NOT NULL,
    material_low_qty   INT NOT NULL,
    material_mid_qty   INT NOT NULL,
    material_high_qty  INT NOT NULL,
    total_incomes      DECIMAL(10, 2) NOT NULL,
    total_expenses     DECIMAL(10, 2) NOT NULL,
    popularity         INT NOT NULL,
    id_company         INT NOT NULL,
    FOREIGN KEY (id_company) REFERENCES Company(id)
);

