# Sharing Moments

## Project idea / intention

Providing a service for users to share their moments / photos of events with their friends, familie and colleagues.
The main intention of developing this app for me is to keep enhancing my skills and learn new stuff.

## Currently used Frameworks / Services

Backend:
* Java Spring
* JJWT
* Zuul
* wro4j
* AWS S3

Frontend:
* Bootstrap 3
* LESS
* AngularJS


## Status

The project is still in development.

Next features / tasks to fulfill:
* Drag & Drop Upload for Photos
* Finish Templates: User, Profile, Event, Index
* Integrating GruntJS / Bower
* FB Login
* Logger / Log4j2
* Seperate Development / Production Configs

## Setup / Pre-Configuration

### Database

Run a local mysql server on port 3306 with a database 'sharingmoments'.
Fill out the database configs for the server:
* jdbc.user
* jdbc.pass

You find these config files at:
* /sharing-moments/authserver/src/main/resources/persistence.properties
* /sharing-moments/resource/src/main/resources/persistence.properties

### AWS S3

Create an AWS S3 Instance and fill out the missing server configs:
* aws.access.key.id
* aws.secret.access.key
* aws.bucket.name
* aws.region.url

You find the config file at:
/sharing-moments/resource/src/main/resources/amazonS3.properties

### SMTP / Mail

You need a google mail account or another smtp service or server.
Then fill out the missing config parameter:
* mail.from
* mail.username
* mail.password

You find the config file at:
/sharing-moments/authserver/src/main/resources/email.properties

### Dependency

Please also note that the auth module needs the resource module as an dependency:
```bash
$ mvn install
```

## Running the Server

Run the following command line: $ mvn spring-boot:run for the project modules auth, ressource and ui.

* auth runs on: http://localhost:9999
* ressource runs on: http://localhost:9000
* ui runs on: http://localhost:8080

## Getting Started

Just load http://localhost:8080/.