FROM java:8
WORKDIR /
ADD demo.jar demo.jar
ADD src/test/resources/jasml_0.10 /usr/lib/jasml_0.10
EXPOSE 8080
CMD ["java","-jar","demo.jar"]