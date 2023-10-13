FROM amazoncorretto:17.0.8-alpine3.18

LABEL authors="Volodymyr Motrechko"

ENV APP_DIR=/app

RUN mkdir $APP_DIR
WORKDIR $APP_DIR

COPY build/libs/airGuardian-*.jar $APP_DIR/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
