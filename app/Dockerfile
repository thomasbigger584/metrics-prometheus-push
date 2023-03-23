FROM maven:3-amazoncorretto-17 AS maven_build

COPY pom.xml /tmp/
COPY src /tmp/src/

WORKDIR /tmp/

RUN mvn package

FROM amazoncorretto:17

EXPOSE 8080

CMD java -jar /data/app-0.1.0.jar

COPY --from=maven_build /tmp/target/app-0.1.0.jar /data/app-0.1.0.jar
