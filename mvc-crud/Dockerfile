# Use Maven image with Java 17 to build the project
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY Wallet_damg6210db ./Wallet_damg6210db
# Build the application
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app
# Install unzip utility
RUN apt-get update && apt-get install -y unzip && rm -rf /var/lib/apt/lists/*
# Copy the wallet zip from host (assumes it's in the project root)
COPY Wallet_damg6210db.zip ./
# Unzip the wallet
RUN unzip Wallet_damg6210db.zip -d Wallet_damg6210db && rm Wallet_damg6210db.zip
# Set TNS_ADMIN to point to the wallet directory
ENV TNS_ADMIN=/app/Wallet_damg6210db
# Copy the built WAR
COPY --from=build /app/target/mvc-crud-1.0-SNAPSHOT.war app.war
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.war"]
