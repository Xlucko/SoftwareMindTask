FROM eclipse-temurin:25
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV spring_profiles_active=dev
ENTRYPOINT ["java","-jar","/app.jar"]

#building docker image: docker build -t mind .
#runing docker image: docker run -d -p 8080:8080 -t mind