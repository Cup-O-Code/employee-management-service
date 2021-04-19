#FROM adoptopenjdk:11-jre-hotspot
#WORKDIR application
#ARG JAR_FILE=target/service-registry.jar
##ENV SERVER_PORT=8761
##ENV EUREKA_SERVICE_URL=http://localhost:8765
#ADD $JAR_FILE service-registry.jar
#ENTRYPOINT ["java", "-jar", "service-registry.jar"]

######## Layered/Exploded Jar Docker image creation
#FROM adoptopenjdk:8-jdk-hotspot as builder
#WORKDIR workspace/app
#COPY mvnw .
#COPY .mvn .mvn
#COPY pom.xml .
#COPY src src
#RUN ./mvnw dependency:go-offline
#RUN ./mvnw install -DskipTests
##RUN ./mvnw -Dmaven.repo.local=root/.m2/repository install -DskipTests
#RUN java -Djarmode=layertools -jar target/service-registry.jar extract
#
#FROM adoptopenjdk:8-jre-hotspot
#WORKDIR application
#ARG BUILD_DIR=/workspace/app
#COPY --from=builder $BUILD_DIR/dependencies/ ./
#COPY --from=builder $BUILD_DIR/spring-boot-loader/ ./
#COPY --from=builder $BUILD_DIR/snapshot-dependencies/ ./
#COPY --from=builder $BUILD_DIR/application/ ./
#ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

#########N ative Image creation using Graalvm
#FROM ghcr.io/graalvm/graalvm-ce:java11-21.0.0.2 as builder
## For java 8 compatible source code
##FROM ghcr.io/graalvm/graalvm-ce:java8-21.0.0.2 as builder
#WORKDIR /app
#COPY mvnw .
#COPY .mvn .mvn
#COPY pom.xml .
#COPY src src
#
### If you don't want to generate the jar inside container image
#COPY target/service-registry.jar ./target/service-registry.jar
#
#RUN gu install native-image
#
## If you want to generate the jar inside container image
##RUN chmod a+rx ./mvnw
##RUN ./mvnw clean install
#
#RUN native-image \
#    --static \
#    --no-fallback \
#    --no-server \
#    --verbose \
#    --install-exit-handlers \
#    -H:Name=service-registry \
#    -cp /app/target/service-registry.jar \
#    -H:Class=org.springframework.boot.loader.JarLauncher \
##    The above two line can be replaced with -jar /app/target/service-registry.jar \
#    -H:+ReportUnsupportedElementsAtRuntime \
#    --allow-incomplete-classpath
#
#FROM scratch
#
#COPY --from=builder /app/service-registry /service-registry
#
#CMD ["/service-registry"]


#####For Devspace to work
FROM adoptopenjdk:11-jre-hotspot
WORKDIR /usr/app
COPY ./src .
COPY ./target .
EXPOSE 9090
ENV SERVER_PORT: 9090
ENTRYPOINT ["java", "-jar", "target/service-registry.jar"]
