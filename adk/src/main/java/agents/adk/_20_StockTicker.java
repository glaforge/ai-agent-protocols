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
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.Annotations.Schema;
import com.google.adk.tools.FunctionTool;
import com.google.adk.web.AdkWebServer;

import java.util.Map;
import java.util.Random;

public class _20_StockTicker implements AgentProvider {
    public static final Random RANDOM = new Random();

    @Override
    public BaseAgent getAgent() {
        return LlmAgent.builder()
            .name("20-stock-ticker")
            .description("Stock exchange ticker expert")
            .instruction("""
                You are a stock exchange ticker expert.
                When asked about the stock price of a company,
                use the `lookup_stock_ticker` tool to find the information.
                """)
            .model("gemini-2.5-flash")
            .tools(FunctionTool.create(_20_StockTicker.class, "lookupStockTicker"))
            .build();
    }

    @Schema(
        name = "lookup_stock_ticker",
        description = "Lookup stock price for a given company or ticker"
    )
    public static Map<String, String> lookupStockTicker(
        @Schema(
            name = "company_name_or_stock_ticker",
            description = "The company name or stock ticker to lookup"
        )
        String ticker) {
        if (ticker.equalsIgnoreCase("DEVOXX")) {
            return Map.of(
                "error",
                "Impossible to look up the stock price for DEVOXX"
            );
        } else {
            return Map.of(
                "stock_price",
                String.valueOf(RANDOM.nextDouble() * 300)
            );
        }
    }

    public static void main(String[] args) {
        AdkWebServer.start(new _20_StockTicker().getAgent());
    }
}
