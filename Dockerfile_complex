FROM maven:3.5-jdk-8 AS build  
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package

FROM java:8
COPY --from=0 /usr/src/app/target/ES-2Sem-2021-Grupo-4-0.0.1-SNAPSHOT.jar /opt/demo.jar
CMD ["java","-jar","/opt/demo.jar"]