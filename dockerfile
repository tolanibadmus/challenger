# ---------- BUILD STAGE ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q clean package -DskipTests

#Extract layers for better caching/faster deployment
RUN java -Djarmode=layertools -jar target/*.jar extract

# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Set up minimal user without shell access
RUN useradd -ms /sbin/nologin springuser
USER springuser

# Copy extracted layers
COPY --from=build /app/dependencies/ ./
COPY --from=build /app/spring-boot-loader/ ./
COPY --from=build /app/snapshot-dependencies/ ./
COPY --from=build /app/application/ ./

EXPOSE 8080

# Using JarLauncher to start the app from the extracted layers
ENTRYPOINT ["java", \
"-XX:MaxRAMPercentage=75", \
"-XX:+UseG1GC", \
"org.springframework.boot.loader.launch.JarLauncher"]