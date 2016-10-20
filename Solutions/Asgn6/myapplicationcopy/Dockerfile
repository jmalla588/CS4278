FROM openjdk:8
WORKDIR /app/jhipster-project
ADD jhipster-0.0.1-SNAPSHOT.war /app/jhipster-project
MAINTAINER joseph@josephstahl.com
EXPOSE 8080
CMD java -jar jhipster-0.0.1-SNAPSHOT.war --spring.profiles.active=dev,swagger
