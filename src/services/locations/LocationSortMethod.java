package services.locations;

/**
 * Created by wykwon on 2015-11-12.
 */
public enum LocationSortMethod {
    Distance,// 개인적 선호 장소와의 Distance (집하고, 직장)
    Score, //선호도
    Distance_Score, // 두개 합친거
    LowCost,
    LowCost_Score
}
