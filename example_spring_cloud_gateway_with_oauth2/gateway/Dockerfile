FROM eclipse-temurin:11
RUN mkdir /opt/app
COPY target/gateway-1.0-SNAPSHOT.jar /opt/app/app.jar
CMD ["java", "-jar", "/opt/app/app.jar"]
EXPOSE 8883
