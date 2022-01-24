FROM adoptopenjdk/openjdk8
MAINTAINER HyeongJun Kim <junnikym@gmail.com>

VOLUME /tmp/ConsumptionHistory
EXPOSE 8080

ARG JAR_FILES=build/libs/*.jar
COPY ${JAR_FILES} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]