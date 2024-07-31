FROM openjdk:21-jdk
MAINTAINER Kuba2412
COPY target/medicalclinic-0.0.1-SNAPSHOT.jar MedicalClinic.jar
ENTRYPOINT ["java", "-jar", "MedicalClinic.jar"]