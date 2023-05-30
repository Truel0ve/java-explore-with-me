FROM amazoncorretto:11-alpine-jdk
COPY ./server/target/*.jar stats-service.jar
ENTRYPOINT ["java","-jar","/stats-service.jar"]