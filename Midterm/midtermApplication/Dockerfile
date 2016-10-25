FROM openjdk:8
MAINTAINER jmalla
WORKDIR /app/midtermApplication
ADD midterm-application-0.0.1-SNAPSHOT.war /app/midtermApplication/midterm-application-0.0.1-SNAPSHOT.war
EXPOSE 8080
CMD java -jar midterm-application-0.0.1-SNAPSHOT.war --spring.profiles.active=dev,swagger
