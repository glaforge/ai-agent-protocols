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
import com.google.adk.web.AdkWebServer;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.PrebuiltVoiceConfig;
import com.google.genai.types.SpeechConfig;
import com.google.genai.types.VoiceConfig;

/**
 * This second science teacher agent uses the "live" mode
 * which lets users ask questions and hear the answers.
 * It's using the Chirp 3 HD voices.
 */
public class _12_ScienceTeacher_Live implements AgentProvider {
    @Override
    public BaseAgent getAgent() {
        return LlmAgent.builder()
            .name("12-science-teacher-live")
            .description("A friendly science teacher")
            .instruction("""
                You are a science teacher for teenagers.
                You explain science concepts in a simple, concise and direct way.
                """)
            .model("gemini-2.5-flash-preview-native-audio-dialog")
            .generateContentConfig(GenerateContentConfig.builder()
                .speechConfig(SpeechConfig.builder()
//                    .languageCode("fr-FR")
                    .languageCode("en-US")
                    .voiceConfig(VoiceConfig.builder()
                        .prebuiltVoiceConfig(PrebuiltVoiceConfig.builder()
                            // All Chirp 3 HD voices
                            // https://cloud.google.com/text-to-speech/docs/chirp3-hd
//                            .voiceName("fr-FR-Chirp3-HD-Charon")
                            .voiceName("en-US-Chirp3-HD-Fenrir")
                            .build())
                        .build())
                    .build())
                .build())
            .build();
    }

    public static void main(String[] args) {
        System.setProperty(
            "org.apache.tomcat.websocket.DEFAULT_BUFFER_SIZE",
            String.valueOf(10 * 1024 * 1024)
        );

        AdkWebServer.start(new _12_ScienceTeacher_Live().getAgent());
    }
}
