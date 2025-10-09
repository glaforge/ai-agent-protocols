package agents.adk;

import com.google.adk.web.AdkWebServer;

public class _00_AllAgents {
    public static void main(String[] args) {
        AdkWebServer.start(
            new _10_ScienceTeacher().getAgent(),
            new _12_ScienceTeacher_Live().getAgent(),
            new _20_StockTicker().getAgent(),
            new _22_LatestNews_Search().getAgent(),
            new _24_PythonCoder().getAgent(),
            new _26_LocalGuide_Maps().getAgent(),
            new _28_MoonExpert_MCP().getAgent(),
            new _30_SearchAndTweet_SubAgents().getAgent(),
            new _32_TripPlanner_Sequential().getAgent(),
            new _34_CompanyDetective_Parallel().getAgent(),
            new _36_CodeRefiner_Loop_Exit().getAgent(),
            new _40_WeatherForecast_Callback().getAgent(),
            new _50_Coffee_LangChain4j().getAgent(),
            new _60_CapitalCity_YAML().getAgent(),
            new _70_ImageEditor_NanoBanana().getAgent()
        );
    }
}
