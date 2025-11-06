FROM azul/zulu-openjdk:17.0.11

WORKDIR /app

COPY wait-for-it.sh .
RUN sed -i 's/\r$//' wait-for-it.sh && chmod +x wait-for-it.sh

COPY pom.xml .
COPY src ./src
COPY .env .

RUN apt-get update && apt-get install -y locales \
    && locale-gen pt_BR.UTF-8 \
    && update-locale LANG=pt_BR.UTF-8

RUN apt-get install maven -y
RUN mvn clean install -DskipTests

ENV LANG=pt_BR.UTF-8
ENV LANGUAGE=pt_BR:pt
ENV LC_ALL=pt_BR.UTF-8
ENV TZ=America/Sao_Paulo

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8080

CMD ["java", "-jar", "target/studfit-0.0.1-SNAPSHOT.jar"]
