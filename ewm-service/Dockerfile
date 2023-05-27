FROM amazoncorretto:19-alpine-jdk
COPY target/*.jar ewm-service.jar
ENTRYPOINT ["java","-jar","/ewm-service.jar"]