DROP TABLE IF EXISTS animals CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS ages CASCADE;
DROP TABLE IF EXISTS evaluates CASCADE;
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS evaluate_reviews CASCADE;
DROP TABLE IF EXISTS shops CASCADE;
DROP TABLE IF EXISTS shop_reviews CASCADE;

CREATE TABLE IF NOT EXISTS ages (
 id SERIAL NOT NULL,
 name VARCHAR(255) NOT NULL,
 created_at  TIMESTAMP NOT NULL default CURRENT_TIMESTAMP,
 updated_at TIMESTAMP NOT NULL default CURRENT_TIMESTAMP,
 PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS users (
  id SERIAL NOT NULL,
  name VARCHAR(255) NOT NULL,
  avatar VARCHAR(255) NOT NULL,
  age_id INT NOT NULL,
  pet_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  authority VARCHAR(255) NOT NULL,
  PR VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE users ADD CONSTRAINT FK_users_ages FOREIGN KEY (age_id) REFERENCES ages;

CREATE TABLE IF NOT EXISTS animals (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  path VARCHAR(255) NOT NULL,
  description VARCHAR(1000) NOT NULL,
  latitude VARCHAR(20),
  longitude VARCHAR(20),
  type INT NOT NULL,
  evaluate_count INT ,
  review_count INT ,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE animals ADD CONSTRAINT FK_users_animals FOREIGN KEY (user_id) REFERENCES users;

CREATE TABLE IF NOT EXISTS evaluates (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  animal_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE evaluates ADD CONSTRAINT FK_evaluates_users FOREIGN KEY (user_id) REFERENCES users;
ALTER TABLE evaluates ADD CONSTRAINT FK_evaluates_animals FOREIGN KEY (animal_id) REFERENCES animals;

CREATE TABLE IF NOT EXISTS reviews (
  id SERIAL NOT NULL,
  animal_id INT NOT NULL,
  user_id INT NOT NULL,
  evaluatereview_count INT,
  review VARCHAR(500) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE reviews ADD CONSTRAINT FK_reviews_users FOREIGN KEY (user_id) REFERENCES users;
ALTER TABLE reviews ADD CONSTRAINT FK_reviews_animals FOREIGN KEY (animal_id) REFERENCES animals;

CREATE TABLE IF NOT EXISTS evaluate_reviews (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  review_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE evaluate_reviews ADD CONSTRAINT FK_reviews_evaluate_reviews FOREIGN KEY (review_id) REFERENCES reviews;

CREATE TABLE IF NOT EXISTS shops (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  shop_path VARCHAR(255) NOT NULL,
  latitude VARCHAR(20),
  longitude VARCHAR(20),
  address VARCHAR(255) NOT NULL,
  access VARCHAR(50) NOT NULL,
  money INT NOT NULL,
  shop_description VARCHAR(1000) NOT NULL,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  PRIMARY KEY (id)
);

ALTER TABLE shops ADD CONSTRAINT FK_users_shops FOREIGN KEY (user_id) REFERENCES users;

CREATE TABLE IF NOT EXISTS shop_reviews (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  shop_id INT NOT NULL,
  shop_review VARCHAR(500),
  shop_evaluate DOUBLE PRECISION,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE shop_reviews ADD CONSTRAINT FK_shop_reviews_shops FOREIGN KEY (shop_id) REFERENCES shops;
ALTER TABLE shop_reviews ADD CONSTRAINT FK_shop_reviews_users FOREIGN KEY (user_id) REFERENCES users;

INSERT INTO ages
(id,name)
VALUES(0,'選択なし'),(1,'20才未満'),(2,'20～39才'),(3,'40～59才'),(4,'60才以上');

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO animram;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO animram;