--DB
CREATE DATABASE "rideshare";


--Users
CREATE TABLE IF NOT EXISTS "rs_users" (
  "user_Id" uuid NOT NULL UNIQUE,
  "email" VARCHAR (100) UNIQUE NOT NULL,
  "first_name" VARCHAR (50) NOT NULL,
  "last_name" VARCHAR (50) NOT NULL,
  "phone_Number" VARCHAR (15) NOT NULL,
  "Image_Id" VARCHAR (50) DEFAULT NULL,
  "degree" VARCHAR (50) NOT NULL,
  "gender" VARCHAR (15) NOT NULL,
  "password" VARCHAR (50) NOT NULL,
  "createdAt" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  "updateAt" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY ("user_Id", "email")
);

--path
CREATE TABLE IF NOT EXISTS "rs_paths" (
  "path_Id" uuid NOT NULL UNIQUE,
  "edges" JSON,
  PRIMARY KEY ("path_Id")
);

--geoLocation
CREATE TABLE IF NOT EXISTS "rs_geoLocation" (
  "geoLocation_Id" uuid NOT NULL UNIQUE,
  "latitude" NUMERIC NOT NULL,
  "longitude" NUMERIC NOT NULL,
  "nameLocation" VARCHAR(100) NOT NULL,
  PRIMARY KEY ("geoLocation_Id", "latitude", "longitude")
);

--Drives
CREATE TABLE IF NOT EXISTS "rs_drives" (
  "drive_Id" uuid NOT NULL UNIQUE,
  "geoLocationSrc_Id" uuid NOT NULL,
  "geoLocationDest_Id" uuid NOT NULL,
  "passengers" VARCHAR(250) DEFAULT NULL,
  "type" VARCHAR(15) DEFAULT NULL,
  "num_seat_available" INT DEFAULT 4,
  "price" VARCHAR(45) DEFAULT NULL,
  "AVG_Price" VARCHAR(45) DEFAULT NULL,
  "upcoming_Drives" VARCHAR(45) DEFAULT NULL,
  "leaveTime" TIMESTAMP DEFAULT NULL,
  "path_Id" uuid NOT NULL,
  "createdAt" DATE DEFAULT NULL,
  FOREIGN KEY("path_Id") REFERENCES "rs_paths"("path_Id"),
  FOREIGN KEY("geoLocationSrc_Id") REFERENCES "rs_geoLocation"("geoLocation_Id"),
  FOREIGN KEY("geoLocationDest_Id") REFERENCES "rs_geoLocation"("geoLocation_Id"),
  PRIMARY KEY (
    "drive_Id",
    "geoLocationSrc_Id",
    "geoLocationDest_Id"
  )
);

--Universities
CREATE TABLE IF NOT EXISTS "rs_universities" (
  "university_Id" uuid PRIMARY KEY NOT NULL UNIQUE,
  "university_name" VARCHAR(100) NOT NULL,
  "geoLocation_Id" uuid NOT NULL,
  FOREIGN KEY ("geoLocation_Id") REFERENCES "rs_geoLocation"("geoLocation_Id")
);

--Users_Universities
CREATE TABLE IF NOT EXISTS "rs_users_universities" (
  "user_Id" uuid NOT NULL,
  "university_Id" uuid NOT NULL,
  FOREIGN KEY ("user_Id") REFERENCES "rs_users"("user_Id"),
  FOREIGN KEY ("university_Id") REFERENCES "rs_universities"("university_Id"),
  PRIMARY KEY ("user_Id", "university_Id")
);

--Users_Drives
CREATE TABLE IF NOT EXISTS "rs_users_drives" (
  "user_Id" uuid NOT NULL,
  "drive_Id" uuid NOT NULL,
  PRIMARY KEY ( "user_Id", "drive_Id"),
  FOREIGN KEY ("user_Id") REFERENCES "rs_users"("user_Id"),
  FOREIGN KEY ("drive_Id") REFERENCES "rs_drives"("drive_Id")
);

--nodes
CREATE TABLE IF NOT EXISTS "rs_nodes" (
  "osm_id" bigint NOT NULL UNIQUE,
  "node_Id" uuid NOT NULL UNIQUE,
  "latitude" NUMERIC NOT NULL,
  "longitude" NUMERIC NOT NULL,
  "degree" NUMERIC DEFAULT NULL,
  "edges" JSON,
  "tags" JSON,
  PRIMARY KEY ("node_Id","osm_id", "latitude", "longitude")
);

--edges
CREATE TABLE IF NOT EXISTS "rs_edges" (
  "edge_Id" uuid NOT NULL UNIQUE,
  "startNodeId" uuid NOT NULL,
  "endNodeId" uuid NOT NULL,
  "distance" NUMERIC DEFAULT NULL,
  "weight" NUMERIC DEFAULT NULL,
  "name" VARCHAR(100) DEFAULT NULL,
  "highwayType" VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY ("edge_Id", "startNodeId", "endNodeId"),
  FOREIGN KEY ("startNodeId") REFERENCES "rs_nodes"("node_Id"),
  FOREIGN KEY ("endNodeId") REFERENCES "rs_nodes"("node_Id")
);


