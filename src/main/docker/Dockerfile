FROM openjdk:17-jdk-slim
MAINTAINER DrSocram
ADD restful-remoteconfig-1.0.jar restfulapp.jar
#EXPOSE 8080
ENTRYPOINT ["java","-jar","/restfulapp.jar","--spring.cloud.bootstrap.location=file:/conf/bootstrap.yml"]
