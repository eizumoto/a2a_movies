# A2A Movies Example

## Database:

Uses postgres 16

Create image
```
docker build -t a2a-postgres
```
Create container and run on port 5432
```
docker run --name pgdb -p 5432:5432 -d a2a-postgres
```
Restore db from backup
```
docker exec -i pgdb2 psql -U admin -d mydb -U admin -d mydb < backup2.sql
```
Note: only dates 5/30/2025 and 5/31/2025 have data for showings

## Programs:

### Python

Uses Python 3.13.3
```
pip install -r requirements.txt
python movie_sales.py
```

### Java

Uses Java 21

runs from java/movies
build:
```
run mvn clean package
```
this builds a jar including the postgres driver under target/movies-1.0-SNAPSHOT.jar

run:
```
java -jar ./target/movies-1.0-SNAPSHOT.jar
```
There is also just a jar for executing without having to build it under the java folder
```
java -jar ./java/movies-1.0-SNAPSHOT.jar