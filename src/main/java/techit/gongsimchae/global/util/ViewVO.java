package techit.gongsimchae.global.util;

public interface ViewVO {
    String SUBDIVISION_COOKIE_NAME = "view-validation";


    String SUBDIVISION_NAME = "subdivision";
    String DAY = ":1";
    String WEEK = ":7";
    String TODAY = ":" + CalculateTime.getToday();
    String TOTAL_VIEWS = "total_views";
}
