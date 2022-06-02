--DB
-- CREATE DATABASE "rideshare";


--Users
CREATE TABLE IF NOT EXISTS "rs_users" (
  "user_Id" uuid NOT NULL UNIQUE,
  "email" TEXT UNIQUE NOT NULL,
  "first_name" TEXT NOT NULL,
  "last_name" TEXT NOT NULL,
  "phone_Number" VARCHAR (15) NOT NULL,
  "image_Id" TEXT DEFAULT NULL,
  "degree" TEXT NOT NULL,
  "gender" TEXT NOT NULL,
  "password" TEXT NOT NULL,
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
  "nameLocation" TEXT NOT NULL,
  PRIMARY KEY ("geoLocation_Id", "latitude", "longitude")
);

--Drives
CREATE TABLE IF NOT EXISTS "rs_drives" (
  "drive_Id" uuid NOT NULL UNIQUE,
  "geoLocationSrc_Id" uuid NOT NULL,
  "geoLocationDest_Id" uuid NOT NULL,
  "passengers" TEXT DEFAULT NULL,
  "type" TEXT DEFAULT NULL,
  "num_seat_available" INT DEFAULT 4,
  "price" NUMERIC DEFAULT NULL,
  "AVG_Price" NUMERIC DEFAULT NULL,
  "upcoming_Drives" TEXT DEFAULT NULL,
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
  "university_name" TEXT NOT NULL,
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
  FOREIGN KEY ("user_Id") REFERENCES "rs_users"("user_Id"),
  FOREIGN KEY ("drive_Id") REFERENCES "rs_drives"("drive_Id")
);

--nodes
CREATE TABLE IF NOT EXISTS "rs_nodes" (
  "osm_Id" bigint NOT NULL UNIQUE,
  "node_Id" uuid NOT NULL UNIQUE,
  "latitude" NUMERIC NOT NULL,
  "longitude" NUMERIC NOT NULL,
  "degree" NUMERIC DEFAULT NULL,
  "edges" JSON,
  "tags" JSON,
  PRIMARY KEY ("node_Id","osm_Id", "latitude", "longitude")
);

--edges
CREATE TABLE IF NOT EXISTS "rs_edges" (
  "edge_Id" uuid NOT NULL UNIQUE,
  "startNodeId" uuid NOT NULL,
  "endNodeId" uuid NOT NULL,
  "distance" NUMERIC DEFAULT NULL,
  "weight" NUMERIC DEFAULT NULL,
  "name" TEXT DEFAULT NULL,
  "highwayType" TEXT DEFAULT NULL,
  PRIMARY KEY ("edge_Id", "startNodeId", "endNodeId"),
  FOREIGN KEY ("startNodeId") REFERENCES "rs_nodes"("node_Id"),
  FOREIGN KEY ("endNodeId") REFERENCES "rs_nodes"("node_Id")
);


