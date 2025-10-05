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

package agents.adk;

import agents.AgentProvider;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.ConfigAgentUtils;
import com.google.adk.agents.ConfigAgentUtils.ConfigurationException;
import com.google.adk.web.AdkWebServer;

import java.nio.file.Path;

public class _60_CapitalCity_YAML implements AgentProvider {
    @Override
    public BaseAgent getAgent() {
        try {
            Path path = Path.of("src/main/resources/capital-city/capital-city.yaml");
            return ConfigAgentUtils.fromConfig(path.toString());
        } catch (ConfigurationException ce) {
            throw new RuntimeException(ce);
        }
    }

    public static void main(String[] args) {
        AdkWebServer.start(new _60_CapitalCity_YAML().getAgent());
    }
}
