FROM maven AS build
COPY src /home/app/src
COPY pom.xml /home/app/
RUN mvn -f /home/app clean package -DskipTests

#
# Package stage
#
FROM openjdk
COPY --from=build /home/app/target/api-biblioteca-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]