FROM amazoncorretto:19-alpine-jdk
COPY ./server/target/*.jar stats-service.jar
ENTRYPOINT ["java","-jar","/stats-service.jar"]