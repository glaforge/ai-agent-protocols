# MCP Tool Server for Product Briefs

This project is a companion to the [ADK Agent Examples](../adk/README.md). It implements a remote tool server using [Quarkus](https://quarkus.io/) and the [Model-Context-Protocol (MCP)](https://github.com/model-context-protocol/specification).

## What it does

This application exposes a single tool, `store-product-brief-in-database`, which allows an AI agent to store a product name and description in a fictitious database.

This server is used by the `ProductStrategist` agent in the Product Campaign example to demonstrate how agents can interact with remote tools and services over a standardized protocol.

## How to run it

You can run the application in development mode, which enables live coding. From the `quarkus-db-store-mcp` directory, run:

```bash
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./gradlew build
```

It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./gradlew build -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./gradlew build -Dquarkus.native.enabled=true
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./gradlew build -Dquarkus.native.enabled=true -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/quarkus-db-store-mcp-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/gradle-tooling>.

## Related Guides

- MCP Server - HTTP/SSE ([guide](https://docs.quarkiverse.io/quarkus-mcp-server/dev/index.html)): This extension enables
  developers to implement the MCP server features easily.
