# A2A Server for Social Media Posting

This project is a companion to the [ADK Agent Examples](../adk/README.md). It implements a remote agent server using [Quarkus](https://quarkus.io/) and the [Agent-to-Agent (A2A) protocol](https://github.com/a2a-sdk/a2a-spec).

## What it does

This application acts as a mock social media agent. It exposes a skill that allows other agents to request social media posts. When called, it simply logs the request and returns a success message, simulating a real social media integration.

This server is used by the `MarketingSpecialist` agent in the Product Campaign example to demonstrate how agents can communicate and delegate tasks to other agents over a standardized protocol.

## How to run it

You can run the application in development mode, which enables live coding. From the `social-media-a2a-server` directory, run:

```sh
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/social-media-a2a-server-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
