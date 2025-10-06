# Agent Examples with the Google Agent Development Kit (ADK)

This repository showcases a collection of example agents built using the [Google Agent Development Kit (ADK)](https://developers.google.com/ai/agents/adk). The ADK is a framework for building, running, and evaluating AI agents. These examples are designed to demonstrate various features and patterns for creating sophisticated agentic applications.

## 🚀 Getting Started

This project is a standard Maven project. You can build it using:

```bash
mvn clean install
```

Each example is a self-contained class with a `main` method that starts a web server (`AdkWebServer`) to host the agent. You can run any example directly from your IDE.

## 🤖 Agent Examples

The examples are organized by complexity and features, located in the `src/main/java/agents/` directory.

### Basic Agents

*   **[`_10_ScienceTeacher.java`](./src/main/java/agents/_10_ScienceTeacher.java)**: A fundamental example of an `LlmAgent`. This agent is a simple persona-based chatbot that acts as a science teacher for teenagers, with no tools attached.

*   **[`_12_ScienceTeacher_Live.java`](./src/main/java/agents/_12_ScienceTeacher_Live.java)**: An extension of the science teacher agent, this example demonstrates how to configure an agent for voice interaction. It uses a model that supports native audio dialog and is configured for French speech output.

### Agents with Tools

*   **[`_20_StockTicker.java`](./src/main/java/agents/_20_StockTicker.java)**: Demonstrates how to create and attach a custom `FunctionTool` to an agent. This agent can look up (fake) stock prices for a given company ticker.

*   **[`_22_LatestNews_Search.java`](./src/main/java/agents/_22_LatestNews_Search.java)**: Shows how to use the built-in `GoogleSearchTool` to give an agent access to up-to-date information from the web.

*   **[`_24_PythonCoder.java`](./src/main/java/agents/_24_PythonCoder.java)**: An agent that can write and execute Python code using the `BuiltInCodeExecutionTool`. This is useful for tasks requiring complex logic or calculations.

*   **[`_26_LocalGuide_Maps.java`](./src/main/java/agents/_26_LocalGuide_Maps.java)**: This example showcases how to integrate with Google Maps. It uses a custom `Gemini` model wrapper to add the `GoogleMaps` tool, allowing the agent to answer location-based questions.

*   **[`_28_MoonExpert_MCP.java`](./src/main/java/agents/_28_MoonExpert_MCP.java)**: An advanced example of tool usage. This agent connects to a remote tool server using the **Model-Context-Protocol (MCP)**. It dynamically fetches tools from the remote server and uses them to answer questions about moon phases.

### Multi-Agent Collaboration

The ADK allows you to compose multiple agents to solve complex problems.

*   **[`_30_SearchAndTweet_SubAgents.java`](./src/main/java/agents/_30_SearchAndTweet_SubAgents.java)**: Introduces the concept of sub-agents. A primary "content-companion" agent delegates tasks to a `topic-search-agent` and a `social-media-agent`. This demonstrates the "agent as a tool" pattern.

*   **[`_32_TripPlanner_Sequential.java`](./src/main/java/agents/_32_TripPlanner_Sequential.java)**: Implements a `SequentialAgent` that orchestrates a series of sub-agents to plan a trip. It follows a strict order: researching a destination, creating an itinerary, and then suggesting restaurants.

*   **[`_34_CompanyDetective_Parallel.java`](./src/main/java/agents/_34_CompanyDetective_Parallel.java)**: Demonstrates the `ParallelAgent`, which executes multiple sub-agents concurrently to gather information about a company (profile, news, financials). A final `SequentialAgent` then compiles the results into a single report.

*   **[`_36_CodeRefiner_Loop_Exit.java`](./src/main/java/agents/_36_CodeRefiner_Loop_Exit.java)**: Showcases the `LoopAgent` for iterative tasks. This agent refines a piece of code by looping between a `code-generator` and a `code-reviewer` agent until the reviewer is satisfied and calls the `exit_loop` tool.

### Advanced Features

*   **[`_40_WeatherForecast_Callback.java`](./src/main/java/agents/_40_WeatherForecast_Callback.java)**: A deep dive into the agent execution lifecycle. This example uses various callbacks (`beforeModelCallback`, `afterToolCallback`, etc.) to log and even modify data at different stages of a turn.

*   **[`_50_Coffee_LangChain4j.java`](./src/main/java/agents/_50_Coffee_LangChain4j.java)**: Demonstrates the extensibility of the ADK by plugging in a different model provider. This agent uses a model from LangChain4j (specifically, a local Ollama model) instead of the default Gemini.

*   **[`_60_CapitalCity_YAML.java`](./src/main/java/agents/_60_CapitalCity_YAML.java)**: Shows how to define an agent declaratively using a YAML configuration file. This separates the agent's configuration (name, instructions, tools) from the application code.

### Media & Multi-Agent Workflows

This more complex scenario, located in `src/main/java/agents/media/`, combines multiple agents and protocols to simulate a product marketing campaign.

*   **[`ProductCampaign.java`](./src/main/java/agents/media/ProductCampaign.java)**: The main entry point for a sequential agent that first runs the `ProductStrategist` and then the `MarketingSpecialist`.

    1.  **[`ProductStrategist.java`](./src/main/java/agents/media/ProductStrategist.java)**: This agent's role is to define a new product.
        *   It uses an MCP tool to store the product brief in a database.
        *   It then calls the `MediaCreative` sub-agent to generate a product image.

    2.  **[`MediaCreative.java`](./src/main/java/agents/media/MediaCreative.java)**: An agent that uses an image generation model (`gemini-2.5-flash-image-preview`) to create a photograph of the product. It uses an `afterModelCallback` to process the generated image (save it to a file, display it in the terminal).

    3.  **[`MarketingSpecialist.java`](./src/main/java/agents/media/MarketingSpecialist.java)**: This agent takes the product brief, generates three social media post ideas, and then uses a custom tool to post them.

    4.  **[`SocialMediaClient.java`](./src/main/java/agents/media/SocialMediaClient.java)**: A tool that demonstrates using the **Agent-to-Agent (A2A)** protocol to communicate with another agent responsible for posting to social media.

---

Feel free to explore, run, and modify these examples to learn more about building powerful agents with the Google ADK.