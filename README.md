# AI Agent Protocols and Agent Development Kit

This repository is a deep dive into the protocols and frameworks for building modern, interconnected AI agents. It showcases how different components can work together to create sophisticated, multi-agent systems.

The project demonstrates:
*   **Agent Development**: Using the [Google Agent Development Kit (ADK)](https://github.com/google/adk-java) to build various types of agents, from simple chatbots to complex, collaborative workflows.
*   **Remote Tool Usage**: How an agent can use tools exposed by a remote server over the [Model-Context-Protocol (MCP)](https://modelcontextprotocol.io/docs/getting-started/intro).
*   **Agent-to-Agent Communication**: How agents can delegate tasks and communicate with each other using the [Agent-to-Agent (A2A) protocol](https://a2a-protocol.org/latest/).
*   **Modern Java Frameworks**: Building the remote servers with [Quarkus](https://quarkus.io/) for fast, efficient services.

## 📂 Project Structure

The repository is organized into three main sub-projects:

### 1. [`adk/`](./adk/README.md)

This directory contains a comprehensive collection of agent examples built with the Google ADK. It demonstrates a wide range of features, from basic agents and tool usage to advanced multi-agent collaboration patterns (sequential, parallel, and loops).

➡️ **See the ADK README for a detailed list of all agent examples.**

### 2. `quarkus-db-store-mcp/`

This is a Quarkus application that implements an **MCP server**. It exposes a simple tool that allows an agent to store data in a database. This server is used by the [`ProductStrategist`](./adk/src/main/java/agents/media/ProductStrategist.java) agent in the `adk` project to show how agents can interact with external services via a standardized protocol.

➡️ **See the MCP Server README for more details.**

### 3. `social-media-a2a-server/`

This is a Quarkus application that implements an **A2A agent server**. It acts as a mock "social media" agent that can receive and process requests from other agents. It is used by the [`MarketingSpecialist`](./adk/src/main/java/agents/media/MarketingSpecialist.java) agent in the `adk` project to demonstrate inter-agent communication.

➡️ **See the A2A Server README for more details.**

## 🚀 How to Run the Full Demo

The most comprehensive example is the [`ProductCampaign`](./adk/src/main/java/agents/media/ProductCampaign.java) agent, which utilizes all parts of this repository. To run it, you first need to start the two companion servers.

### Step 1: Start the MCP Tool Server

In a terminal, navigate to the `quarkus-db-store-mcp` directory and run:
```bash
./gradlew quarkusDev
```
This will start the MCP server on `http://localhost:8080`.

### Step 2: Start the A2A Agent Server

In a second terminal, navigate to the `social-media-a2a-server` directory and run:
```bash
./mvnw quarkus:dev
```
This will start the A2A server on `http://localhost:9999`.

### Step 3: Run the Product Campaign Agent

Open the `adk` project in your IDE. Navigate to the [`agents.media.ProductCampaign`](./adk/src/main/java/agents/media/ProductCampaign.java) class and run its `main` method. This will start a command-line interface where you can interact with the agent.

**Example interaction:**
> Tell the agent: `Create a product campaign for a new brand of coffee for developers.`

The [`ProductCampaign`](./adk/src/main/java/agents/media/ProductCampaign.java) agent will then orchestrate the [`ProductStrategist`](./adk/src/main/java/agents/media/ProductStrategist.java) and [`MarketingSpecialist`](./adk/src/main/java/agents/media/MarketingSpecialist.java) sub-agents. You will see it call the MCP server to store the product brief, generate an image, and then call the A2A server to post social media updates.

---

## License

This project is licensed under the Apache 2.0 License.

## Disclaimer

This is not an official Google product.
