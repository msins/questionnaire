FROM amazoncorretto:11-alpine
ARG JDBC_URL
ENV JDBC_URL $JDBC_URL
ARG JAR_FILE=target/questionnaire.jar
COPY ${JAR_FILE} /questionnaire.jar
ENTRYPOINT ["java","-Dspring.","-Dspring.datasource.url=${JDBC_URL}","-jar","/questionnaire.jar"]