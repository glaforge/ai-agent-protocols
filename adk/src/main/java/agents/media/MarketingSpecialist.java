/*
 * Copyright 2025 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package agents.media;

import agents.AgentProvider;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.FunctionTool;
import com.google.adk.web.AdkWebServer;

public class MarketingSpecialist implements AgentProvider {
    @Override
    public BaseAgent getAgent() {
        return LlmAgent.builder()
            .name("marketing_product_strategist")
            .model("gemini-2.5-flash")
            .description("marketing product strategist")
            .instruction("""
                You are a marketing product strategist.
                Given a product, you'll suggest 3 ideas of social media posts advertising the product.
                
                Here's the product brief:
                {product_brief?}
                
                Post each of these social media posts via the `post_to_social_media` tool.
                """)
            .tools(FunctionTool.create(SocialMediaClient.class, "postSocialMedia"))
            .build();
    }

    public static void main(String[] args) {
        AdkWebServer.start(new MarketingSpecialist().getAgent());
    }
}
