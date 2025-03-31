FROM azul/zulu-openjdk:17.0.11 AS build

WORKDIR /app
COPY . .

RUN apt-get update && apt-get install -y maven
RUN mvn clean install -DskipTests

FROM azul/zulu-openjdk:17.0.11

WORKDIR /app

COPY --from=build /app/target/studfit-0.0.1-SNAPSHOT.jar studfit-api.jar

EXPOSE 8080

CMD ["java", "-jar", "studfit-api.jar"]