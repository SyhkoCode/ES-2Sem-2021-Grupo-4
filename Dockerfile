FROM java:8
WORKDIR /
ADD CodeQualityAssessor.jar CodeQualityAssessor.jar
EXPOSE 8080
CMD ["java","-jar","CodeQualityAssessor.jar"]