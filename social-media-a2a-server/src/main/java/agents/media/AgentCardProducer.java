package agents.media;

import java.util.Collections;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import io.a2a.server.PublicAgentCard;
import io.a2a.spec.AgentCapabilities;
import io.a2a.spec.AgentCard;
import io.a2a.spec.AgentSkill;

@ApplicationScoped
public class AgentCardProducer {

    @Produces
    @PublicAgentCard
    public AgentCard agentCard() {
        return new AgentCard.Builder()
            .name("social-media-a2a-server")
            .description("Publish social media posts")
            .url("http://localhost:9999")
            .version("1.0.0")
            .documentationUrl("http://example.com/docs")
            .capabilities(new AgentCapabilities.Builder()
                .streaming(true)
                .pushNotifications(true)
                .stateTransitionHistory(true)
                .build())
            .defaultInputModes(Collections.singletonList("text"))
            .defaultOutputModes(Collections.singletonList("text"))
            .skills(Collections.singletonList(new AgentSkill.Builder()
                .id("publish-media-post")
                .name("publish-media-post")
                .description("publish social media posts")
                .tags(Collections.singletonList("social-media"))
                .examples(List.of(
                    "publish the following text to LinkedIn",
                    "send this message to X/Twitter",
                    "post this video to TikTok",
                    "create this social media post"))
                .build()))
            .protocolVersion("0.3.0")
            .build();
    }
}

