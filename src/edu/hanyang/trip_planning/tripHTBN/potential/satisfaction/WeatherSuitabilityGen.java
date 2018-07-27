package edu.hanyang.trip_planning.tripHTBN.potential.satisfaction;

/**
 * Created by wykwon on 2015-10-20.
 */
public class WeatherSuitabilityGen {

    /**
     * 날씨가 맑고 쾌청해야 좋은데
     * @return
     */
    public static WeatherSuitabilityCPT type1_indoor() {

        /**
         * 날씨가 맑고 쾌청해야 좋은데
         공원
         테마파크
         해수욕장/해변
         관광농원
         섬
         휴양림/숲길걷기
         산봉우리/등산로
         폭포/계곡

         0 부적합
         1 적합

         */
        WeatherSuitabilityCPT cpt = new WeatherSuitabilityCPT();
        cpt.setProbability(2,4,1,0.9);
        cpt.setProbability(2,3,1,0.9);
        cpt.setProbability(2,2,1,0.9);
        cpt.setProbability(2,1,1,0.9);
        cpt.setProbability(2,0,1,0.9);

        cpt.setProbability(1,4,1,0.9);
        cpt.setProbability(1,3,1,0.9);
        cpt.setProbability(1,2,1,0.9);
        cpt.setProbability(1,1,1,0.9);
        cpt.setProbability(1,0,1,0.9);

        cpt.setProbability(0,4,1,0.9);
        cpt.setProbability(0,3,1,0.9);
        cpt.setProbability(0,2,1,0.9);
        cpt.setProbability(0,1,1,0.9);
        cpt.setProbability(0,0,1,0.9);



        cpt.setProbability(2,4,0,0.1);
        cpt.setProbability(2,3,0,0.1);
        cpt.setProbability(2,2,0,0.1);
        cpt.setProbability(2,1,0,0.1);
        cpt.setProbability(2,0,0,0.1);

        cpt.setProbability(1,4,0,0.1);
        cpt.setProbability(1,3,0,0.1);
        cpt.setProbability(1,2,0,0.1);
        cpt.setProbability(1,1,0,0.1);
        cpt.setProbability(1,0,0,0.1);

        cpt.setProbability(0,4,0,0.1);
        cpt.setProbability(0,3,0,0.1);
        cpt.setProbability(0,2,0,0.1);
        cpt.setProbability(0,1,0,0.1);
        cpt.setProbability(0,0,0,0.1);

        return cpt;
    }

    public static WeatherSuitabilityCPT type2_lessSensitive() {
        WeatherSuitabilityCPT cpt = new WeatherSuitabilityCPT();

        cpt.setProbability(2,4,1,0.05);
        cpt.setProbability(2,3,1,0.05);
        cpt.setProbability(2,2,1,0.05);
        cpt.setProbability(2,1,1,0.05);
        cpt.setProbability(2,0,1,0.05);

        cpt.setProbability(1,4,1,0.5);
        cpt.setProbability(1,3,1,0.6);
        cpt.setProbability(1,2,1,0.7);
        cpt.setProbability(1,1,1,0.6);
        cpt.setProbability(1,0,1,0.5);

        cpt.setProbability(0,4,1,0.8);
        cpt.setProbability(0,3,1,1.0);
        cpt.setProbability(0,2,1,1.0);
        cpt.setProbability(0,1,1,1.0);
        cpt.setProbability(0,0,1,0.8);



        cpt.setProbability(2,4,0,0.95);
        cpt.setProbability(2,3,0,0.95);
        cpt.setProbability(2,2,0,0.95);
        cpt.setProbability(2,1,0,0.95);
        cpt.setProbability(2,0,0,0.95);

        cpt.setProbability(1,4,0,0.5);
        cpt.setProbability(1,3,0,0.4);
        cpt.setProbability(1,2,0,0.3);
        cpt.setProbability(1,1,0,0.4);
        cpt.setProbability(1,0,0,0.5);

        cpt.setProbability(0,4,0,0.2);
        cpt.setProbability(0,3,0,0.0);
        cpt.setProbability(0,2,0,0.0);
        cpt.setProbability(0,1,0,0.0);
        cpt.setProbability(0,0,0,0.2);

        return cpt;
    }


    public static WeatherSuitabilityCPT type3_moreSensitive() {
        WeatherSuitabilityCPT cpt = new WeatherSuitabilityCPT();

        cpt.setProbability(2,4,1,0.05);
        cpt.setProbability(2,3,1,0.05);
        cpt.setProbability(2,2,1,0.05);
        cpt.setProbability(2,1,1,0.05);
        cpt.setProbability(2,0,1,0.05);

        cpt.setProbability(1,4,1,0.1);
        cpt.setProbability(1,3,1,0.5);
        cpt.setProbability(1,2,1,0.8);
        cpt.setProbability(1,1,1,0.5);
        cpt.setProbability(1,0,1,0.1);

        cpt.setProbability(0,4,1,0.6);
        cpt.setProbability(0,3,1,1.0);
        cpt.setProbability(0,2,1,1.0);
        cpt.setProbability(0,1,1,1.0);
        cpt.setProbability(0,0,1,0.6);



        cpt.setProbability(2,4,0,0.95);
        cpt.setProbability(2,3,0,0.95);
        cpt.setProbability(2,2,0,0.95);
        cpt.setProbability(2,1,0,0.95);
        cpt.setProbability(2,0,0,0.95);

        cpt.setProbability(1,4,0,0.9);
        cpt.setProbability(1,3,0,0.5);
        cpt.setProbability(1,2,0,0.2);
        cpt.setProbability(1,1,0,0.5);
        cpt.setProbability(1,0,0,0.9);

        cpt.setProbability(0,4,0,0.4);
        cpt.setProbability(0,3,0,0.0);
        cpt.setProbability(0,2,0,0.0);
        cpt.setProbability(0,1,0,0.0);
        cpt.setProbability(0,0,0,0.4);

        return cpt;
    }

    public static WeatherSuitabilityCPT type4_sensitive() {
        WeatherSuitabilityCPT cpt = new WeatherSuitabilityCPT();

        cpt.setProbability(2,4,1,0.05);
        cpt.setProbability(2,3,1,0.05);
        cpt.setProbability(2,2,1,0.05);
        cpt.setProbability(2,1,1,0.05);
        cpt.setProbability(2,0,1,0.05);

        cpt.setProbability(1,4,1,0.1);
        cpt.setProbability(1,3,1,0.2);
        cpt.setProbability(1,2,1,0.3);
        cpt.setProbability(1,1,1,0.2);
        cpt.setProbability(1,0,1,0.1);

        cpt.setProbability(0,4,1,0.4);
        cpt.setProbability(0,3,1,1.0);
        cpt.setProbability(0,2,1,1.0);
        cpt.setProbability(0,1,1,1.0);
        cpt.setProbability(0,0,1,0.4);



        cpt.setProbability(2,4,0,0.95);
        cpt.setProbability(2,3,0,0.95);
        cpt.setProbability(2,2,0,0.95);
        cpt.setProbability(2,1,0,0.95);
        cpt.setProbability(2,0,0,0.95);

        cpt.setProbability(1,4,0,0.9);
        cpt.setProbability(1,3,0,0.8);
        cpt.setProbability(1,2,0,0.7);
        cpt.setProbability(1,1,0,0.8);
        cpt.setProbability(1,0,0,0.9);

        cpt.setProbability(0,4,0,0.6);
        cpt.setProbability(0,3,0,0.0);
        cpt.setProbability(0,2,0,0.0);
        cpt.setProbability(0,1,0,0.0);
        cpt.setProbability(0,0,0,0.6);

        return cpt;
    }
}
