package agents.media;

import agents.AgentProvider;
import agents.util.AgentRunner;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.web.AdkWebServer;

public class ProductCampaign implements AgentProvider {
    @Override
    public BaseAgent getAgent() {
        BaseAgent productStrategist = new ProductStrategist().getAgent();
        BaseAgent marketingSpecialist = new MarketingSpecialist().getAgent();

        return SequentialAgent.builder()
            .name("product_campaing")
            .description("product campaign sequence")
            .subAgents(
                productStrategist,
                marketingSpecialist
            )
            .build();
    }

    public static void main(String[] args) {
//        AdkWebServer.start(new ProductCampaign().getAgent());
        AgentRunner.run(new ProductCampaign().getAgent());
    }
}
