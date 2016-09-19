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
* AWS S3 and RDS

Frontend:
* Bootstrap 3
* LESS
* AngularJS


## Status

The project is still in development.

Next features / tasks to fulfill:
* FB Login

## Setup / Pre-Configuration

### Database - AWS RDS

Create an AWS RDS Instance and fill out the missing server configs:
* cloud.aws.credentials.accessKey
* cloud.aws.credentials.secretKey
* cloud.aws.rds.database.name
* cloud.aws.rds.database.password
* cloud.aws.rds.database.instance.id
* cloud.aws.region.static

You find these config files at:
* /sharing-moments/authserver/src/main/resources/application.properties
* /sharing-moments/resource/src/main/resources/application.properties

### AWS S3

Create an AWS S3 Instance and fill out the missing server configs:
* cloud.aws.s3.bucket.name
* cloud.aws.s3.region.url

You find the config file at:
/sharing-moments/resource/src/main/resources/application.properties

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