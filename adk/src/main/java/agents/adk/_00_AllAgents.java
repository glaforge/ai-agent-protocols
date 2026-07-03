package agents.adk;

import com.google.adk.web.AdkWebServer;

import agents.adk.one._23_UrlContextGrounding;
import agents.adk.one._38_HITL;

public class _00_AllAgents {
    public static void main(String[] args) {
        System.setProperty(
            "org.apache.tomcat.websocket.DEFAULT_BUFFER_SIZE",
            String.valueOf(10 * 1024 * 1024)
        );
        System.setProperty(
            "spring.mvc.async.request-timeout",
            String.valueOf(60 * 60 * 1000)
        );

        AdkWebServer.start(
            new _10_ScienceTeacher().getAgent(),
            new _12_ScienceTeacher_Live().getAgent(),
            new _20_StockTicker().getAgent(),
            new _22_LatestNews_Search().getAgent(),
            new _23_UrlContextGrounding().getAgent(),
            new _24_PythonCoder().getAgent(),
            new _26_LocalGuide_Maps().getAgent(),
            new _28_MoonExpert_MCP().getAgent(),
            new _30_SearchAndTweet_SubAgents().getAgent(),
            new _32_TripPlanner_Sequential().getAgent(),
            new _34_CompanyDetective_Parallel().getAgent(),
            new _36_CodeRefiner_Loop_Exit().getAgent(),
            new _38_HITL().getAgent(),
            new _40_WeatherForecast_Callback().getAgent(),
            new _50_Coffee_LangChain4j().getAgent(),
            new _60_CapitalCity_YAML().getAgent(),
            new _70_ImageEditor_NanoBanana().getAgent(),
            new _80_AppAndPlugin().getAgent()
        );
    }
}
