package edu.hanyang.trip_planning.tripHTBN.poi;

import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripData.poi.POIManager;

import edu.hanyang.trip_planning.tripData.preference.TouristAttractionType;
import org.math.array.util.Random;

import java.util.*;

/**
 * Created by wykwo on 2015-10-02.
 */
public class POITitleList {

    public static final String jejuHotels[] = {
            "하얏트리젠시 제주",
            "금호리조트 제주점",
            "제주KAL호텔",
            "라온호텔앤리조트",
            "롯데호텔 제주",
            "제주신라호텔",
            "메종글래드제주",
            "라마다프라자제주호텔",
            "켄싱턴제주호텔",
            "빌로우비치호텔",
            "선샤인호텔",
            "서귀포KAL호텔",
            "제주퍼시픽호텔",
            "롯데시티호텔 제주점",
            "해비치호텔앤리조트 제주"
    };

    public static final String jejuConventionHotels[] = {
            "하얏트리젠시 제주",
            "금호리조트 제주점",
            "제주KAL호텔",
            "라온호텔앤리조트",
            "롯데호텔 제주",
            "제주신라호텔",
            "메종글래드제주",
            "라마다프라자제주호텔",
            "켄싱턴제주호텔",
            "빌로우비치호텔",
            "선샤인호텔",
            "서귀포KAL호텔",
            "제주퍼시픽호텔",
            "롯데시티호텔 제주점",
            "해비치호텔앤리조트 제주"
    };
    public static final String jeju25[] = {"제주그랜드호텔", "섭지코지",
            "비자림",
            "성산일출봉",
            "용두암",
            "용연구름다리",
            "함덕서우봉해변",
            "대포주상절리",
            "쇠소깍",
            "사려니숲길",
            "만장굴",
            "미로공원",
            "천지연폭포",
            "정방폭포",
            "천제연폭포",
            "한림공원",
            "협재해변",
            "우도팔경유람선",
            "소인국테마파크",
            "중문색달해변",
            "외돌개",
            "제주절물자연휴양림",
            "월정리해수욕장",
            "따라비오름",
            "오설록티뮤지엄",
            "박물관은살아있다 제주중문점",
            "김영갑갤러리두모악",
            "테디베어뮤지엄 제주점"};

    public static final String jeju100[] = {"감귤 박물관",
            "곽지과물해변",
            "관덕정",
            "국립제주박물관",
            "금능석물원",
            "김영갑갤러리두모악",
            "남원큰엉해안경승지",
            "넥슨컴퓨터박물관",
            "노루생태관찰원",
            "다랑쉬",
            "대정향교",
            "대포주상절리",
            "도순설록다원",
            "돈내코유원지",
            "동백동산",
            "두산봉",
            "따라비오름",
            "만장굴",
            "메이즈랜드",
            "물영아리",
            "미로공원",
            "박물관은살아있다 제주중문점",
            "백약이오름",
            "별빛누리 공원",
            "붉은오름자연휴양림",
            "비양도",
            "비자림",
            "사계해변",
            "사라봉",
            "사려니숲길",
            "산굼부리",
            "산방산사랑의유람선",
            "산방산탄산온천",
            "삼성혈",
            "삼양검은모래해변",
            "새별오름",
            "새섬",
            "서귀포자연휴양림",
            "섭지코지",
            "성산일출봉",
            "성읍민속마을",
            "소인국테마파크",
            "소정방폭포",
            "송악공원",
            "송악산",
            "쇠소깍",
            "수월봉",
            "신산공원",
            "아부오름",
            "아프리카박물관",
            "안덕계곡",
            "알뜨르비행장",
            "엉또폭포",
            "여미지식물원",
            "오설록티뮤지엄",
            "외돌개",
            "요트투어샹그릴라",
            "용눈이오름",
            "용두암",
            "용머리해안",
            "용연구름다리",
            "우도",
            "우도팔경유람선",
            "월정리해수욕장",
            "유리의성",
            "이중섭미술관",
            "이호테우해변",
            "일출랜드",
            "저지오름",
            "정방폭포",
            "제주공룡랜드",
            "제주관음사",
            "제주도립미술관",
            "제주마방목지",
            "제주민속촌박물관",
            "제주절물자연휴양림",
            "제주해녀박물관",
            "제주현대미술관",
            "중문색달해변",
            "지미봉",
            "차귀도",
            "천제연폭포",
            "천지연폭포",
            "테디베어뮤지엄 제주점",
            "퍼시픽랜드 돌고래쇼",
            "한라산 어승생악",
            "한라산 웃세붉은오름",
            "한라산국립공원 관음사탐방로",
            "한라산국립공원 돈내코탐방로",
            "한라산국립공원 성판악탐방로",
            "한라산국립공원 어리목탐방로",
            "한라산국립공원 영실탐방로",
            "한라수목원",
            "한림공원",
            "한화아쿠아플라넷 제주",
            "함덕서우봉해변",
            "협재해변",
            "화순금모래해변",
            "환상숲곶자왈공원",
            "휴애리"};


    public static String test10_1[] = {"산방산탄산온천", "용눈이오름", "환상숲곶자왈공원", "만장굴", "천제연폭포", "테디베어뮤지엄 제주점", "여미지식물원", "별빛누리 공원", "중문색달해변", "한라산 어승생악", "지미봉", "금능석물원"};
    public static String test50_1[] = {"한화아쿠아플라넷 제주", "제주관음사", "별빛누리 공원", "중문색달해변", "한라산 어승생악", "지미봉", "금능석물원", "메이즈랜드", "곽지과물해변", "산방산사랑의유람선", "안덕계곡", "동백동산", "국립제주박물관", "외돌개", "미로공원", "비자림", "우도팔경유람선", "새섬", "남원큰엉해안경승지", "소정방폭포", "한라산 웃세붉은오름", "따라비오름", "붉은오름자연휴양림", "이호테우해변", "퍼시픽랜드 돌고래쇼", "제주도립미술관", "화순금모래해변", "김영갑갤러리두모악", "아부오름", "대포주상절리", "일출랜드", "한림공원", "유리의성", "제주해녀박물관", "함덕서우봉해변", "감귤 박물관", "다랑쉬", "한라산국립공원 성판악탐방로", "산방산탄산온천", "차귀도", "한라산국립공원 돈내코탐방로", "엉또폭포", "넥슨컴퓨터박물관", "용눈이오름", "환상숲곶자왈공원", "만장굴", "천제연폭포", "테디베어뮤지엄 제주점", "여미지식물원", "섭지코지", "성산일출봉"};
    public static String test50_2[] = {"메종글래드제주", "송악공원", "우도팔경유람선", "관덕정", "휴애리", "삼성혈", "이중섭미술관", "사라봉", "새섬", "메이즈랜드", "성읍민속마을", "대정향교", "엉또폭포", "남원큰엉해안경승지", "천지연폭포", "아프리카박물관", "따라비오름", "용머리해안", "김영갑갤러리두모악", "쇠소깍", "한라산국립공원 영실탐방로", "비양도", "제주민속촌박물관", "산방산사랑의유람선", "천제연폭포", "미로공원", "제주관음사", "아부오름", "노루생태관찰원", "새별오름", "함덕서우봉해변", "붉은오름자연휴양림", "우도", "환상숲곶자왈공원", "용두암", "퍼시픽랜드 돌고래쇼", "일출랜드", "두산봉", "서귀포자연휴양림", "이호테우해변", "성산일출봉", "여미지식물원", "한라산국립공원 어리목탐방로", "제주절물자연휴양림", "산굼부리", "알뜨르비행장", "도순설록다원", "신산공원", "용눈이오름", "한라산국립공원 성판악탐방로"};
    public static String test50_3[] = {"빌로우비치호텔", "따라비오름", "비양도", "금능석물원", "산방산사랑의유람선", "백약이오름", "외돌개", "노루생태관찰원", "월정리해수욕장", "사려니숲길", "한라산국립공원 어리목탐방로", "한라산국립공원 영실탐방로", "우도", "미로공원", "아부오름", "여미지식물원", "사계해변", "돈내코유원지", "제주절물자연휴양림", "두산봉", "한라산국립공원 관음사탐방로", "국립제주박물관", "성읍민속마을", "사라봉", "환상숲곶자왈공원", "한라산 웃세붉은오름", "한림공원", "한라수목원", "제주공룡랜드", "대정향교", "한라산국립공원 돈내코탐방로", "쇠소깍", "화순금모래해변", "이중섭미술관", "용머리해안", "만장굴", "퍼시픽랜드 돌고래쇼", "남원큰엉해안경승지", "감귤 박물관", "용두암", "제주현대미술관", "요트투어샹그릴라", "별빛누리 공원", "산방산탄산온천", "넥슨컴퓨터박물관", "서귀포자연휴양림", "제주민속촌박물관", "한라산국립공원 성판악탐방로", "성산일출봉", "천제연폭포"};
    public static String test50_4[] = {"제주KAL호텔", "안덕계곡", "따라비오름", "대정향교", "제주도립미술관", "외돌개", "차귀도", "동백동산", "지미봉", "감귤 박물관", "대포주상절리", "아프리카박물관", "중문색달해변", "도순설록다원", "새섬", "김영갑갤러리두모악", "협재해변", "산굼부리", "용연구름다리", "한라산 어승생악", "우도", "제주현대미술관", "화순금모래해변", "한라산국립공원 어리목탐방로", "만장굴", "제주해녀박물관", "백약이오름", "남원큰엉해안경승지", "두산봉", "오설록티뮤지엄", "우도팔경유람선", "국립제주박물관", "쇠소깍", "사려니숲길", "정방폭포", "용두암", "저지오름", "송악공원", "이중섭미술관", "소정방폭포", "사계해변", "박물관은살아있다 제주중문점", "한라산국립공원 영실탐방로", "삼성혈", "알뜨르비행장", "제주마방목지", "한라산국립공원 관음사탐방로", "함덕서우봉해변", "일출랜드", "천제연폭포"};
    public static String test50_5[] = {"빌로우비치호텔", "제주공룡랜드", "알뜨르비행장", "붉은오름자연휴양림", "송악공원", "용머리해안", "제주마방목지", "이호테우해변", "한화아쿠아플라넷 제주", "관덕정", "새섬", "백약이오름", "중문색달해변", "안덕계곡", "감귤 박물관", "대정향교", "함덕서우봉해변", "신산공원", "월정리해수욕장", "새별오름", "산굼부리", "오설록티뮤지엄", "돈내코유원지", "우도", "대포주상절리", "외돌개", "노루생태관찰원", "한라수목원", "다랑쉬", "따라비오름", "정방폭포", "한라산국립공원 돈내코탐방로", "제주관음사", "곽지과물해변", "도순설록다원", "아프리카박물관", "물영아리", "한라산국립공원 영실탐방로", "삼양검은모래해변", "미로공원", "산방산사랑의유람선", "제주도립미술관", "두산봉", "한라산 웃세붉은오름", "퍼시픽랜드 돌고래쇼", "소인국테마파크", "여미지식물원", "별빛누리 공원", "제주절물자연휴양림", "넥슨컴퓨터박물관"};


    public static Set<BasicPOI> getPOIs(String titles[]) {
        Set<BasicPOI> poiSet = new HashSet<BasicPOI>();
        for (String title : titles) {
            poiSet.add(POIManager.getInstance().getPOIByTitle(title));
        }
        return poiSet;

    }

    public static String[] randomTitle(int size) {
        List<Integer> ranNumber = new ArrayList<Integer>();

        for (int i = 0; i < jeju100.length; i++) {
            ranNumber.add(i);
        }
        Collections.shuffle(ranNumber);
        String ret[] = new String[size + 1];
        int hotelIdx = Random.randInt(0, 3);
        ret[0] = jejuHotels[hotelIdx];
        for (int i = 0; i < size; i++) {
            ret[i + 1] = jeju100[ranNumber.get(i)];
        }
        return ret;

    }

    public static String print(String src, String array[]) {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("{");
        strbuf.append("\"" + src + "\",");
        for (int i = 0; i < array.length - 1; i++) {
            strbuf.append("\"" + array[i] + "\"" + ",");
        }
        strbuf.append("\"" + array[array.length - 1] + "\"" + "}");
        return strbuf.toString();
    }


    public static void generateEvaluationSet(int size) {
        int hotelIdx = Random.randInt(0, jejuHotels.length - 1);
        System.out.println(print(jejuHotels[hotelIdx], randomTitle(size - 1)));
    }


    public static void verification() {
        for (String title : jeju100) {
            BasicPOI basicPOI = POIManager.getInstance().getPOIByTitle(title);
            TouristAttractionType touristAttractionType = TouristAttractionType.parse(basicPOI.getPoiType().subSubCategory);
            System.out.println(title + "\t" + touristAttractionType);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            generateEvaluationSet(50);
        }

        verification();
//
        for (TouristAttractionType type : TouristAttractionType.values()) {
            System.out.println(type);
        }
//        for (int i = 0; i < 10; i++) {
//            System.out.println(print(randomTitle(9)));
//        }

    }
}
