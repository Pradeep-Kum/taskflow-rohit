FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /workspace
COPY . .
RUN chmod +x ./gradlew && ./gradlew :app:installDist -x test

FROM eclipse-temurin:17-jre-alpine AS runtime

WORKDIR /app
COPY --from=build /workspace/app/build/install/app/ /app/

ENV PORT=8080
EXPOSE 8080

CMD ["/app/bin/app"]
