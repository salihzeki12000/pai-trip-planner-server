package database;

import com.google.gson.Gson;
import kr.hyosang.coordinate.CoordPoint;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import tripPlanning.tripData.dataType.BusinessHours;
import tripPlanning.tripData.dataType.Location;
import tripPlanning.tripData.dataType.PoiType;
import tripPlanning.tripData.poi.BasicPoi;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseManager {
    private static final String CHROME_DRIVER_PATH = "resources/chromedriver.exe";

    private static final Gson GSON = new Gson();
    private static final Map<String, String> AREA_URL_MAP = createAreaUrlMap(); // 네이버 여행 가볼만한곳 페이지
    private static final Map<String, String> AREA_RECTS = createAreaRects(); // 지역 좌표 사각형 (좌상단 x,y,우하단 x,y)

    private static final String API_SERVER_HOST = "https://dapi.kakao.com";
    private static final String API_KEY = "4de6f3195493e6e3110c34c2b34d7c8a";    // 이거 이대로 github 올라가면 보안 이슈 있음, 해결 필요
    private static final String SEARCH_KEYWORD_PATH = "/v2/local/search/keyword.json?";

    private static final String DATABASE_DIR = "database/";
    private static final String[] TITLE_FILENAMES = {"title_attraction", "title_restaurant", "title_shopping", "title_accommodation", "title_transportation"};
    private static final String[] KAKAOPOI_FILENAMES = {"kakaopoi_attraction", "kakaopoi_restaurant", "kakaopoi_shopping", "kakaopoi_accommodation", "kakaopoi_transportation"};
    private static final String[] KAKAOPOIPLUS_FILENAMES = {"kakaopoiplus_attraction", "kakaopoiplus_restaurant", "kakaopoiplus_shopping", "kakaopoiplus_accommodation", "kakaopoiplus_transportation"};
    private static final String[] BASICPOI_FILENAMES = {"basicpoi_attraction", "basicpoi_restaurant", "basicpoi_shopping", "basicpoi_accommodation", "basicpoi_transportation"};

    private static final String[] INVALID_CATEGORIES = {"카페", "간식", "관광안내소", "드라이브코스", "주차장", "술집", "패스트푸드", "문구,사무용품", "슈퍼마켓", "인터넷쇼핑몰"};

    private static final char[][] wcoToMobCode = {
            {'P', 'Q', 'R', 'S', 'L', 'M', 'N', 'O', 'X', 'Y'},
            {'O', 'N', 'M', 'L', 'S', 'R', 'Q', 'P', 'W', 'V'},
            {'N', 'O', 'L', 'M', 'R', 'S', 'P', 'Q', 'V', 'W'},
            {'M', 'L', 'O', 'N', 'Q', 'P', 'S', 'R', 'U', 'T'},
            {'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U'},
            {'M', 'L', 'O', 'N', 'Q', 'P', 'S', 'R', 'U', 'T'},
            {'N', 'O', 'L', 'M', 'R', 'S', 'P', 'Q', 'V', 'W'},
    };

    private static final double MIN_SCORE = 1;
    private static final int MIN_NUM_SCORED_REVIEWS = 0;
    private static final int MIN_NUM_REVIEWS = 10;

    private static Map<String, String> createAreaUrlMap() {
        Map<String, String> areaUrlMap = new HashMap<>();
        areaUrlMap.put("제주특별자치도", "https://m.search.naver.com/search.naver?query=%EC%A0%9C%EC%A3%BC%EB%8F%84+%EC%97%AC%ED%96%89#api=%3Fwhere%3Dbridge%26tab_prs%3Dcsa%26col_prs%3Dcsa%26query%3D%EC%A0%9C%EC%A3%BC%EB%8F%84%20%EC%97%AC%ED%96%89%26tab%3Dattraction%26format%3Dtext%26nqx_theme%3D%7B%22theme%22%3A%7B%22main%22%3A%7B%22name%22%3A%22travel_domestic%22%2C%22score%22%3A%221.000000%22%2C%22os%22%3A%225745943%22%2C%22pkid%22%3A%22368%22%7D%7D%7D&_lp_type=cm");
        return areaUrlMap;
    }

    private static Map<String, String> createAreaRects() {
        Map<String, String> rects = new HashMap<>();
        rects.put("제주특별자치도", "126.135964,33.584645,126.983702,33.160928");
        return rects;
    }

    private static WebDriver getWebDriver(boolean isMobile) {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        if (isMobile) {
            Map<String, String> mobileEmulation = new HashMap<>();
            mobileEmulation.put("deviceName", "iPad Pro");
            ChromeOptions chromeOptions = new ChromeOptions().setExperimentalOption("mobileEmulation", mobileEmulation);
            return new ChromeDriver(chromeOptions);
        } else {
            return new ChromeDriver();
        }
    }

    private static void delay(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            System.out.println("delay error");
        }
    }

    private static String getNewFilename(String name, String area) {
        LocalDateTime now = LocalDateTime.now();
        String YYMMddHHmmss = now.format(DateTimeFormatter.ofPattern("YYMMddHHmmss"));
        return DATABASE_DIR + YYMMddHHmmss + "_" + area + "_" + name + ".json";
    }

    private static void createJsonFile(String json, String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createTitleJsonFiles(String area) {
        WebDriver driver = getWebDriver(true);

        driver.get(AREA_URL_MAP.get(area)); // 네이버 여행 가볼만한곳 페이지
        delay(2);
        for (int i = 0; i < 4; i++) { // 명소, 맛집, 쇼핑, 숙박
            // driver -> elements
            List<WebElement> webElements = new ArrayList<>();
            if (i < 3) {
                driver.findElement(By.cssSelector("div.attraction_slect._attraction_subtab > ul > li:nth-child(" + (i + 2) + ")")).click(); // 명소, 맛집, 쇼핑
                delay(2);
                while (true) {
                    try {
                        driver.findElement(By.cssSelector("div.api_more_wrap._moreContentTrigger")).click(); //펼쳐보기
                        delay(2);
                    } catch (NoSuchElementException e) {
                        break;
                    }
                }
                webElements.addAll(driver.findElements(By.cssSelector("div.attraction_list_bridge > ul > li > a > div > div > strong"))); // 장소명
                webElements.addAll(driver.findElements(By.cssSelector("div.attraction_list_bridge > ul > li > a > div > div > div.main_info > strong"))); // 장소명
            } else {
                driver.findElement(By.cssSelector("div.header_lnb > div:nth-child(1) > ul > li:nth-child(1)")).click(); // 여행정보
                delay(2);
                driver.findElement(By.cssSelector("div.hotel_list._content > div")).click(); // 제주도 호텔 더보기
                delay(2);
                while (true) {
                    try {
                        driver.findElement(By.cssSelector("div.more_lst.ng-scope")).click(); //펼쳐보기
                        delay(2);
                    } catch (NoSuchElementException e) {
                        break;
                    }
                }
                webElements.addAll(driver.findElements(By.cssSelector("h3.stit.ng-binding"))); // 호텔명
            }

            // elements->titles
            List<String> titles = new ArrayList<>();
            for (WebElement webElement : webElements) {
                titles.add(webElement.getText());
            }

            // write json file
            String filename = getNewFilename(TITLE_FILENAMES[i], area);
            String json = GSON.toJson(titles);
            createJsonFile(json, filename);

            driver.navigate().refresh();
            delay(2);
        }
        driver.close();
    }

    private static String getDataFilename(String area, String name) {
        // get most recent title filename
        File[] files = new File(DATABASE_DIR).listFiles();
        List<String> filenames = new ArrayList<>();
        for (File file : files) {
            if (file.getName().contains(name) && file.getName().contains(area)) {
                filenames.add(file.getName());
            }
        }
        Collections.sort(filenames, Collections.reverseOrder());
        return filenames.get(0);
    }

    private static String[] getTitles(String area, String titleFilename) {
        String filename = getDataFilename(area, titleFilename);

        // filename -> titles
        String[] titles = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(DATABASE_DIR + filename));
            titles = GSON.fromJson(br, String[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return titles;
    }

    private static String urlEncodeUTF8(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    private static String mapParamToStrParam(Map<String, String> map) {
        StringBuilder paramBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            paramBuilder.append(paramBuilder.length() > 0 ? "&" : "");
            paramBuilder.append(String.format("%s=%s", urlEncodeUTF8(key), urlEncodeUTF8(map.get(key))));
        }
        return paramBuilder.toString();
    }

    private static String request(String apiPath, Map<String, String> params) {
        String requestUrl = API_SERVER_HOST + apiPath + mapParamToStrParam(params);

        BufferedReader reader = null;
        InputStreamReader isr = null;

        try {
            URL url = new URL(requestUrl);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "KakaoAK " + API_KEY);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("charset", "utf-8");

            int responseCode = con.getResponseCode();
            // System.out.println(String.format("\nSending GET request to URL : %s", requestUrl));
            // System.out.println("Response Code : " + responseCode);

            if (responseCode == 200)
                isr = new InputStreamReader(con.getInputStream());
            else
                isr = new InputStreamReader(con.getErrorStream());

            reader = new BufferedReader(isr);

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            // System.out.println(buffer.toString());
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (Exception ignore) {
            }
            if (isr != null) try {
                isr.close();
            } catch (Exception ignore) {
            }
        }
        return null;
    }

    private static void checkTitleFile(String area, String titleFilename) {
        String[] titles = getTitles(area, titleFilename);

        // titles vs json response
        int idx = 2;
        Map<String, String> params = new HashMap<>();
        params.put("rect", AREA_RECTS.get(area));
        params.put("sort", "accuracy");
        for (String title : titles) {
            params.put("query", title);
            String json = request(SEARCH_KEYWORD_PATH, params);
            SearchResponse response = GSON.fromJson(json, SearchResponse.class);

            if (response.getKakaoPois().length < 1) {
                System.out.println(idx);
                System.out.println("No Poi");   //TODO: 추후에는 자동으로 삭제 하도록 수정 필요
                System.out.println(title);
                System.out.println();
            } else {
                if (!response.getKakaoPois()[0].isCategoryValid(INVALID_CATEGORIES)) {
                    System.out.println(idx);
                    System.out.println("Invalid Category");
                    System.out.println(title);
                    System.out.println(response.getKakaoPois()[0].toString());
                    System.out.println();
                } else if (!title.equals(response.getKakaoPois()[0].getName())) {
                    System.out.println(idx);
                    System.out.println("Different Name");
                    System.out.println(title);
                    System.out.println(response.getKakaoPois()[0].getName());
                    System.out.println();
                }
            }
            idx++;
        }
    }

    private static void createKakaoPoiJsonFiles(String area) {
        for (int i = 0; i < 5; i++) {
            String[] titles = getTitles(area, TITLE_FILENAMES[i]);

            // titles->kakaoPoiSet
            Set<KakaoPoi> kakaoPoiSet = new HashSet<>();
            Map<String, String> params = new HashMap<>();
            params.put("rect", AREA_RECTS.get(area));
            params.put("sort", "accuracy");
            for (String title : titles) {
                params.put("query", title);
                String json = request(SEARCH_KEYWORD_PATH, params);
                SearchResponse response = GSON.fromJson(json, SearchResponse.class);
                if (response.getKakaoPois().length > 0) {
                    KakaoPoi kakaoPoi = response.getKakaoPois()[0];
                    if (kakaoPoi.isCategoryValid(INVALID_CATEGORIES) && kakaoPoi.isAddressValid(area)) {
                        kakaoPoi.setFromTitle(title);
                        kakaoPoiSet.add(kakaoPoi);
                    }
                }
            }

            // write json file
            String filename = getNewFilename(KAKAOPOI_FILENAMES[i], area);
            String json = GSON.toJson(kakaoPoiSet);
            createJsonFile(json, filename);
        }
    }

    private static KakaoPoi[] getKakaoPois(String area, String kakaoPoiFilename) {
        String filename = getDataFilename(area, kakaoPoiFilename);

        // filename -> titles
        KakaoPoi[] kakaoPois = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(DATABASE_DIR + filename));
            kakaoPois = GSON.fromJson(br, KakaoPoi[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return kakaoPois;
    }

    private static String filterCategoryStr(String name, String categoryStr) {
        String filteredCategoryStr = categoryStr;
        if (name.contains("유적지")) {
            filteredCategoryStr = "여행 \u003e 관광,명소 \u003e 문화유적";
        } else if (name.equals("VT스파")) {
            filteredCategoryStr = "여행 \u003e 체험 \u003e 스파";
        } else if (name.equals("신영영화박물관무비스타")) {
            filteredCategoryStr = "여행 \u003e 문화,예술 \u003e 박물관";
        } else if (name.equals("제주빅볼랜드")) {
            filteredCategoryStr = "여행 \u003e 스포츠,레저";
        } else if (name.equals("폴리파크")) {
            filteredCategoryStr = "여행 \u003e 스포츠,레저";
        } else if (name.equals("동부카트클럽")) {
            filteredCategoryStr = "여행 \u003e 스포츠,레저";
        } else if (name.equals("짚라인제주")) {
            filteredCategoryStr = "여행 \u003e 스포츠,레저";
        } else if (name.equals("송악카트체험장")) {
            filteredCategoryStr = "여행 \u003e 스포츠,레저";
        } else if (name.equals("제주오션파크")) {
            filteredCategoryStr = "여행 \u003e 스포츠,레저 \u003e 수상스포츠";
        } else if (name.equals("낙천리아홉굿마을")) {
            filteredCategoryStr = "여행 \u003e 체험 \u003e 농촌";
        } else if (name.equals("올레바당체험마을")) {
            filteredCategoryStr = "여행 \u003e 체험 \u003e 어촌";
        } else if (name.equals("세리월드 미로공원")) {
            filteredCategoryStr = "여행 \u003e 공원";
        } else if (name.equals("외돌개")) {
            filteredCategoryStr = "여행 \u003e 관광,명소 \u003e 바위";
        } else if (name.equals("표선수산마트")) {
            filteredCategoryStr = "음식점 \u003e 한식 \u003e 해물,생선";
        } else if (name.equals("당신의제주")) {
            filteredCategoryStr = "쇼핑 \u003e 기념품";
        } else if (name.equals("제주특산품전시판매장")) {
            filteredCategoryStr = "쇼핑 \u003e 기념품";
        } else if (name.equals("벨롱장")) {
            filteredCategoryStr = "쇼핑 \u003e 시장";
        } else if (name.equals("아로마제주")) {
            filteredCategoryStr = "쇼핑 \u003e 미술,공예";
        } else if (name.equals("아코제주")) {
            filteredCategoryStr = "쇼핑 \u003e 미술,공예";
        } else if (name.equals("못생김")) {
            filteredCategoryStr = "쇼핑 \u003e 인테리어장식";
        } else if (name.equals("중섭공방")) {
            filteredCategoryStr = "쇼핑 \u003e 미술,공예";
        } else if (name.equals("율랜드스쿠버앤게스트하우스")) {
            filteredCategoryStr = "숙박 \u003e 게스트하우스";
        } else if (name.equals("스쿠버스토리 \u0026게스트하우스")) {
            filteredCategoryStr = "숙박 \u003e 게스트하우스";
        } else if (name.equals("아덴힐리조트앤골프클럽")) {
            filteredCategoryStr = "숙박 \u003e 콘도,리조트";
        } else if (name.equals("캐슬렉스제주골프클럽")) {
            filteredCategoryStr = "숙박 \u003e 콘도,리조트";
        } else if (name.equals("한국폴로컨트리클럽")) {
            filteredCategoryStr = "숙박 \u003e 콘도,리조트";
        } else if (name.equals("하워드존슨제주호텔")) {
            filteredCategoryStr = "숙박 \u003e 호텔";
        } else if (filteredCategoryStr.contains("가정,생활 \u003e 패션 \u003e 패션잡화점 \u003e 액세서리")) {
            filteredCategoryStr = "쇼핑 \u003e 액세서리";
        } else if (filteredCategoryStr.contains("문화,예술 \u003e 도서 \u003e 서점 \u003e 중고서점")) {
            filteredCategoryStr = "쇼핑 \u003e 도서 \u003e 중고도서";
        } else if (filteredCategoryStr.contains("교통,수송 \u003e 교통시설 \u003e 선착장,항만시설 \u003e 유람선선착장")) {
            filteredCategoryStr = "여행 \u003e 투어 \u003e 유람선";
        } else if (filteredCategoryStr.contains("교통,수송 \u003e 교통시설 \u003e 항구,포구")) {
            filteredCategoryStr = "여행 \u003e 관광,명소 \u003e 항구,포구";
        } else if (filteredCategoryStr.contains("교통,수송 \u003e 도로시설 \u003e 교량,다리")) {
            filteredCategoryStr = "여행 \u003e 관광,명소 \u003e 교량,다리";
        } else if (filteredCategoryStr.contains("교육,학문 \u003e 교육단체 \u003e 체험학습장")) {
            filteredCategoryStr = "여행 \u003e 체험 \u003e 체험학습장";
        } else if (filteredCategoryStr.contains("스포츠,레저 \u003e 승마 \u003e 승마장")) {
            filteredCategoryStr = "여행 \u003e 스포츠,레저 \u003e 승마";
        } else if (filteredCategoryStr.contains("스포츠,레저 \u003e 경마 \u003e 경마장")) {
            filteredCategoryStr = "여행 \u003e 스포츠,레저 \u003e 경마";
        } else if (filteredCategoryStr.contains("스포츠,레저 \u003e 수영,수상 \u003e 수상스포츠")) {
            filteredCategoryStr = "여행 \u003e 스포츠,레저 \u003e 수상스포츠";
        } else if (filteredCategoryStr.contains("스포츠,레저 \u003e 스포츠시설 \u003e 사격,궁도")) {
            filteredCategoryStr = "여행 \u003e 스포츠,레저 \u003e 사격,궁도";
        } else if (filteredCategoryStr.contains("스포츠,레저 \u003e 골프 \u003e 골프연습장")) {
            filteredCategoryStr = "여행 \u003e 스포츠,레저 \u003e 골프";
        } else if (filteredCategoryStr.contains("문화,예술 \u003e 문화시설 \u003e 아쿠아리움")) {
            filteredCategoryStr = "여행 \u003e 체험 \u003e 아쿠아리움";
        } else if (filteredCategoryStr.contains("문화,예술 \u003e 문화시설 \u003e 박물관")) {
            filteredCategoryStr = "여행 \u003e 문화,예술 \u003e 박물관";
        } else if (filteredCategoryStr.contains("문화,예술 \u003e 문화시설 \u003e 미술관")) {
            filteredCategoryStr = "여행 \u003e 문화,예술 \u003e 미술관";
        } else if (filteredCategoryStr.contains("문화,예술 \u003e 문화시설 \u003e 전시관")) {
            filteredCategoryStr = "여행 \u003e 문화,예술 \u003e 전시관";
        } else if (filteredCategoryStr.contains("문화,예술 \u003e 문화시설 \u003e 기념관")) {
            filteredCategoryStr = "여행 \u003e 문화,예술 \u003e 기념관";
        } else if (filteredCategoryStr.contains("문화,예술 \u003e 문화시설 \u003e 공연장,연극극장")) {
            filteredCategoryStr = "여행 \u003e 문화,예술 \u003e 공연장,연극극장";
        } else if (filteredCategoryStr.contains("문화,예술 \u003e 미술,공예 \u003e 화랑")) {
            filteredCategoryStr = "여행 \u003e 문화,예술 \u003e 화랑";
        } else if (filteredCategoryStr.contains("문화,예술 \u003e 종교 \u003e 불교")) {
            filteredCategoryStr = "여행 \u003e 종교 \u003e 불교";
        } else if (filteredCategoryStr.contains("여행 \u003e 공원 \u003e 도시근린공원")) {
            filteredCategoryStr = "여행 \u003e 공원";
        } else if (filteredCategoryStr.contains("서비스,산업 \u003e 축산업 \u003e 농장,목장")) {
            filteredCategoryStr = "여행 \u003e 체험 \u003e 농장,목장";
        } else if (filteredCategoryStr.contains("서비스,산업 \u003e 농업 \u003e 과일,채소재배")) {
            filteredCategoryStr = "여행 \u003e 체험 \u003e 과일,채소재배";
        } else if (filteredCategoryStr.contains("교육,학문 \u003e 교육단체 \u003e 청소년수련원")) {
            filteredCategoryStr = "숙박 \u003e 청소년수련원";
        } else if (filteredCategoryStr.contains("교육,학문 \u003e 교육단체 \u003e 연수원")) {
            filteredCategoryStr = "숙박 \u003e 연수원";
        } else if (filteredCategoryStr.contains("문화,예술 \u003e 도서 \u003e 서점")) {
            filteredCategoryStr = "쇼핑 \u003e 도서";
        } else if (filteredCategoryStr.contains("가정,생활 \u003e 유아 \u003e 장난감,완구")) {
            filteredCategoryStr = "쇼핑 \u003e 장난감,완구";
        } else if (filteredCategoryStr.contains("가정,생활 \u003e 패션 \u003e 패션잡화점")) {
            filteredCategoryStr = "쇼핑 \u003e 패션잡화점";
        } else if (filteredCategoryStr.contains("가정,생활 \u003e 패션 \u003e 의류판매")) {
            filteredCategoryStr = "쇼핑 \u003e 패션잡화점";
        } else if (filteredCategoryStr.contains("가정,생활 \u003e 식품판매 \u003e 수산물판매")) {
            filteredCategoryStr = "쇼핑 \u003e 수산물";
        } else if (filteredCategoryStr.contains("가정,생활 \u003e 식품판매 \u003e 과일,채소가게")) {
            filteredCategoryStr = "쇼핑 \u003e 과일,채소";
        } else if (filteredCategoryStr.contains("가정,생활 \u003e 생활용품점 \u003e 인테리어장식판매")) {
            filteredCategoryStr = "쇼핑 \u003e 인테리어장식";
        } else if (filteredCategoryStr.equals("여행 \u003e 기념품판매")) {
            filteredCategoryStr = "쇼핑 \u003e 기념품";
        } else if (filteredCategoryStr.equals("가정,생활 \u003e 식품판매")) {
            filteredCategoryStr = "쇼핑 \u003e 식품";
        }

        filteredCategoryStr = filteredCategoryStr.replace("여행 \u003e 숙박", "숙박");
        filteredCategoryStr = filteredCategoryStr.replace("가정,생활", "쇼핑");
        filteredCategoryStr = filteredCategoryStr.replace("여행 \u003e 체험여행", "여행 \u003e 체험");

        return filteredCategoryStr;
    }

    private static Category getRootCategory(Set<KakaoPoi> KakaoPoiSet) {
        Category root = new Category("root");
        for (KakaoPoi kakaoPoi : KakaoPoiSet) {
            String filteredCategoryStr = filterCategoryStr(kakaoPoi.getName(), kakaoPoi.getCategoryStr());
            String[] categories = filteredCategoryStr.split(" > ");

            Category category = new Category(categories[0]);
            Category subCategory = new Category(categories.length > 1 ? categories[1] : "");
            Category subSubCategory = new Category(categories.length > 2 ? categories[2] : "");

            if (!root.getSubcategories().contains(category)) {
                category.addSubCategory(subCategory);
                subCategory.addSubCategory(subSubCategory);
                root.addSubCategory(category);
            } else {
                category = root.getSubcategory(category.getName());
                if (!category.getSubcategories().contains(subCategory)) {
                    subCategory.addSubCategory(subSubCategory);
                    category.addSubCategory(subCategory);
                } else {
                    subCategory = category.getSubcategory(subCategory.getName());
                    if (!subCategory.getSubcategories().contains(subSubCategory)) {
                        subCategory.addSubCategory(subSubCategory);
                    }
                }
            }
        }
        return root;
    }

    private static void checkKakaoPoiFile(String area, String kakaoPoiFilename) {
        KakaoPoi[] kakaoPois = getKakaoPois(area, kakaoPoiFilename);
        Set<KakaoPoi> kakaoPoiSet = new HashSet<>(Arrays.asList(kakaoPois));
        Category root = getRootCategory(kakaoPoiSet);
        for (Category category : root.getSubcategories()) {
            for (Category subCategory : category.getSubcategories()) {
                for (Category subSubCategory : subCategory.getSubcategories()) {
                    System.out.println(category.getName() + "\t\t\t" + subCategory.getName() + "\t\t\t" + subSubCategory.getName());
                }
            }
        }
    }

    private static void createCategoriesJsonFile(String area) {
        Set<KakaoPoi> kakaoPoiSet = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            KakaoPoi[] kakaoPois = getKakaoPois(area, KAKAOPOI_FILENAMES[i]);
            kakaoPoiSet.addAll(Arrays.asList(kakaoPois));
        }
        Category root = getRootCategory(kakaoPoiSet);

        // write json file
        String filename = getNewFilename("categories", area);
        String json = GSON.toJson(root);
        createJsonFile(json, filename);
    }

    private static String wcoToMob(double wco) {
        String str = String.valueOf((int) wco);
        String mob = "";
        for (int i = 0; i < str.length(); i++) {
            if (i == 0 && str.substring(0, 1).equals("-")) {
                mob += 'E';
            } else {
                mob += wcoToMobCode[i][Integer.parseInt(str.substring(i, i + 1))];
            }
        }
        return mob;
    }

    private static Matcher getMatcher(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(str);
    }

    private static String getSubStringByRegexp(String str, String regexp) {
        Matcher matcher = getMatcher(str, regexp);
        matcher.find();
        return matcher.group(1);
    }

    private static Set<KakaoPoiPlus> kakaoPoisToKakaoPoiPlusSet(KakaoPoi[] kakaoPois) {
        System.out.println("kakaoPois -> KakaoPoiPlusSet");
        Set<KakaoPoiPlus> kakaoPoiPlusSet = new HashSet<>();

        WebDriver driver = getWebDriver(false);
        for (KakaoPoi kakaoPoi : kakaoPois) {
            String filteredCategoryStr = filterCategoryStr(kakaoPoi.getName(), kakaoPoi.getCategoryStr());
            String[] categories = filteredCategoryStr.split(" > ");

            CoordPoint coord = new CoordPoint(kakaoPoi.getX(), kakaoPoi.getY());
            coord.convertWGS2WCONG();
            double wcoX = coord.x;
            double wcoY = coord.y;
            String mobX = wcoToMob(wcoX);
            String mobY = wcoToMob(wcoY);

            double score = 0;
            int numScoredReviews = 0;
            int numReviews = 0;
            String businessHours;
            List<String> facilities = new ArrayList<>();

            driver.navigate().to(kakaoPoi.getPlaceUrl());
            delay(7);
            List<WebElement> scoreElements = driver.findElements(By.cssSelector("div.location_evaluation > a,link_evaluation > span.color_b"));
            if (scoreElements.size() > 0) {
                String scoreStr0 = scoreElements.get(0).getText();
                String scoreStr1 = scoreElements.get(1).getText();

                String scoreStr = getSubStringByRegexp(scoreStr0, "평점 (.*?)\n");
                String numScoredReviewsStr = getSubStringByRegexp(scoreStr0, "\\((.*?)\\)");
                String numReviewStr = scoreStr1.split("\\s")[1];

                score = Double.parseDouble(scoreStr);
                numScoredReviews = Integer.parseInt(numScoredReviewsStr);
                numReviews = Integer.parseInt(numReviewStr);
            }

            List<WebElement> businessHoursWrapperElement = driver.findElements(By.cssSelector("div.openhour_wrap"));
            if (businessHoursWrapperElement.size() > 0) {
                List<WebElement> businessHoursBtnElement = driver.findElements(By.cssSelector("ul.list_operation > li > a.btn_more"));
                if (businessHoursBtnElement.size() > 0) {
                    businessHoursBtnElement.get(0).click();
                    delay(2);
                    WebElement businessHoursElement = businessHoursWrapperElement.get(0).findElement(By.cssSelector("div.fold_floor > div.inner_floor"));
                    businessHours = businessHoursElement.getText().replace("\n닫기", "");
                } else {
                    WebElement businessHoursElement = businessHoursWrapperElement.get(0).findElement(By.cssSelector("div.location_present"));
                    businessHours = businessHoursElement.getText();
                }
            } else {
                businessHours = "";
            }

            List<WebElement> facilitiesWrapperElements = driver.findElements(By.cssSelector("ul.list_facility > li"));
            if (facilitiesWrapperElements.size() > 0) {
                for (WebElement facilityElement : facilitiesWrapperElements) {
                    facilities.add(facilityElement.getText());
                }
            }

            KakaoPoiPlus kakaoPoiPlus = new KakaoPoiPlus();
            kakaoPoiPlus.setId(kakaoPoi.getId());
            kakaoPoiPlus.setName(kakaoPoi.getName());
            kakaoPoiPlus.setCategoryStr(filteredCategoryStr);
            kakaoPoiPlus.setCategory(categories[0]);
            kakaoPoiPlus.setSubCategory(categories.length > 1 ? categories[1] : "");
            kakaoPoiPlus.setSubSubcategory(categories.length > 2 ? categories[2] : "");
            kakaoPoiPlus.setAddress(kakaoPoi.getAddress());
            kakaoPoiPlus.setRoadAddress(kakaoPoi.getRoadAddress());
            kakaoPoiPlus.setPhoneNumber(kakaoPoi.getPhoneNumber());
            kakaoPoiPlus.setPlaceUrl(kakaoPoi.getPlaceUrl());
            kakaoPoiPlus.setWgsX(kakaoPoi.getX());
            kakaoPoiPlus.setWgsY(kakaoPoi.getY());
            kakaoPoiPlus.setWcoX(wcoX);
            kakaoPoiPlus.setWcoY(wcoY);
            kakaoPoiPlus.setMobX(mobX);
            kakaoPoiPlus.setMobY(mobY);
            kakaoPoiPlus.setScore(score);
            kakaoPoiPlus.setNumScoredReviews(numScoredReviews);
            kakaoPoiPlus.setNumReviews(numReviews);
            kakaoPoiPlus.setBusinessHours(businessHours);
            kakaoPoiPlus.setFacilities(facilities);

            kakaoPoiPlusSet.add(kakaoPoiPlus);
            System.out.println(kakaoPoiPlusSet.size() + "/" + kakaoPois.length);
        }
        driver.close();

        return kakaoPoiPlusSet;
    }

    private static void createKakaoPoiPlusJsonFiles(String area) {
        for (int i = 0; i < 5; i++) {
            KakaoPoi[] kakaoPois = getKakaoPois(area, KAKAOPOI_FILENAMES[i]);
            Set<KakaoPoiPlus> kakaoPoiPlusSet = kakaoPoisToKakaoPoiPlusSet(kakaoPois);

            // write json file
            String filename = getNewFilename(KAKAOPOIPLUS_FILENAMES[i], area);
            String json = GSON.toJson(kakaoPoiPlusSet);
            createJsonFile(json, filename);
        }
    }

    private static KakaoPoiPlus[] getKakaoPoiPluses(String area, String kakaoPoiPlusFilename) {
        String filename = getDataFilename(area, kakaoPoiPlusFilename);

        // filename -> titles
        KakaoPoiPlus[] kakaoPoiPluses = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(DATABASE_DIR + filename));
            kakaoPoiPluses = GSON.fromJson(br, KakaoPoiPlus[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return kakaoPoiPluses;
    }

    private static BusinessHours getBusinessHours(KakaoPoiPlus kakaoPoiPlus) {
        Matcher matcher1;
        Matcher matcher2;

        String businessHours = kakaoPoiPlus.getBusinessHours();
        Set<String> closedDaySet = new HashSet<>();

        /* split */
        String[] businessHoursAndClosedDaysArray = businessHours.split("(\n)*휴무일\n");
        if (businessHoursAndClosedDaysArray.length > 1) {
            businessHours = businessHoursAndClosedDaysArray[0];
            closedDaySet.addAll(Arrays.asList(businessHoursAndClosedDaysArray[1].split("\n")));
        } else {
            businessHours = businessHoursAndClosedDaysArray[0];
        }

        /* businessHours replace */
        businessHours = businessHours.replaceAll("영업시간\n", "");
        businessHours = businessHours.replaceAll("영업시간 영업중\n", "");
        businessHours = businessHours.replaceAll("영업시간 영업종료\n", "");
        businessHours = businessHours.replaceAll("상담시간 ", "");
        businessHours = businessHours.replaceAll(" \\(고객센터\\)", "");
        businessHours = businessHours.replaceAll("\n홈페이지 공지", "");
        businessHours = businessHours.replaceAll("기타 \\(홈페이지 공지\\)", "");
        businessHours = businessHours.replaceAll("\n비정기 휴무", "");
        businessHours = businessHours.replaceAll("\n비정기적 휴무", "");
        businessHours = businessHours.replaceAll("\n연말연시", "");
        businessHours = businessHours.replaceAll("개장기간 - 상시", "");
        businessHours = businessHours.replaceAll(" \\(관람 당일 통제 확인 필요\\)", "");
        businessHours = businessHours.replaceAll(" \\(예약문의 오후3시부터 가능\\)", "");
        businessHours = businessHours.replaceAll(" \\(일출, 일몰시간에 따라 변경될 수 있음\\)", "");
        businessHours = businessHours.replaceAll(" \\(일몰시간에 따라 변경될 수 있음\\)", "");
        businessHours = businessHours.replaceAll(" \\(일몰시간에 따라 변경될수 있음\\)", "");
        businessHours = businessHours.replaceAll(" \\(문의 시간\\)", "");
        businessHours = businessHours.replaceAll(" \\(예약문의\\)", "");
        businessHours = businessHours.replaceAll(" \\(마감 후 죽포장만 가능\\)", "");
        businessHours = businessHours.replaceAll(" \\(매표\\)", "");
        businessHours = businessHours.replaceAll("입장시간은 계절에 따라 변경될수 있음\n", "");
        businessHours = businessHours.replaceAll("공휴일\n", "공휴일 ");
        businessHours = businessHours.replaceAll("연장운영\n", "");
        businessHours = businessHours.replaceAll("\\s?까지", "");
        // 00시00분 -> 00:00
        matcher1 = getMatcher(businessHours, "\\d\\d시\\d\\d분");
        while (matcher1.find()) {
            String start = businessHours.substring(0, matcher1.start());
            String end = businessHours.substring(matcher1.end());
            String body = matcher1.group();
            body = body.replaceAll("시", ":");
            body = body.replaceAll("분", "");

            businessHours = start + body + end;
            matcher1 = getMatcher(businessHours, "\\d\\d시\\d\\d분");
        }
        // 요일
        businessHours = businessHours.replaceAll("매일", "월,화,수,목,금,토,일");
        businessHours = businessHours.replaceAll("월~금", "월,화,수,목,금");
        businessHours = businessHours.replaceAll("월~토", "월,화,수,목,금,토");
        businessHours = businessHours.replaceAll("화~토", "화,수,목,금,토");
        businessHours = businessHours.replaceAll("화~일", "화,수,목,금,토,일");
        businessHours = businessHours.replaceAll("수~토", "수,목,금,토");
        businessHours = businessHours.replaceAll("수~일", "수,목,금,토,일");
        // 월,화,수,목,금,토,일 10:00 ~ 22:30 (식사 21:30) -> 월,화,수,목,금,토,일 10:00 ~ 21:30
        matcher1 = getMatcher(businessHours, "\\d\\d:\\d\\d \\((체험이용시간|매표마감|입장|식사|대기 마감|매표가능시간) \\d\\d:\\d\\d\\)");
        while (matcher1.find()) {
            String start = businessHours.substring(0, matcher1.start());
            String end = businessHours.substring(matcher1.end());
            String body = matcher1.group();
            body = body.replaceAll("\\d\\d:\\d\\d \\((체험이용시간|매표마감|입장|식사|대기 마감|매표가능시간) ", "");
            body = body.replaceAll("\\)", "");

            businessHours = start + body + end;
            matcher1 = getMatcher(businessHours, "\\d\\d:\\d\\d \\((체험이용시간|매표마감|입장|식사|대기 마감|매표가능시간) \\d\\d:\\d\\d\\)");

        }
        // special 처리 (general 하지 못한 경우들 처리)
        businessHours = businessHours.replaceAll("월,화,수,목,금,토,일 ~ 18:00", "월,화,수,목,금,토,일 10:30 ~ 18:00");
        businessHours = businessHours.replaceAll("월,화,수,목,금,토,일 11:00 ~ 22:50 \\(21:30 이후 입장은 미리 예약 필요\\)", "월,화,수,목,금,토,일 11:00 ~ 21:30");
        businessHours = businessHours.replaceAll("월,화,수,목,금,토,일 09:00 ~ 18:00 \\(1/1,설,추석당일은 10:00 개장\\)", "월,화,수,목,금,토,일 10:00 ~ 18:00");
        businessHours = businessHours.replaceAll(" \\(사전 예약시 오전 10:00~24:00\\)", "");
        businessHours = businessHours.replaceAll(" \\(단체손님 있을 시 06:00 ~ 재료 소진시\\)", "");
        businessHours = businessHours.replaceAll("\n2018년 7월 20일 ~ 8월 25일 : 19시 운영", "");
        businessHours = businessHours.replaceAll("기타 \\(입장시간 : 08:30 ~ 일몰 한사간 전\\)", "월,화,수,목,금,토,일 08:30 ~ 16:00");
        businessHours = businessHours.replaceAll("기타 \\(08:30 ~ 일몰 1시간 전 입장마감\\)", "월,화,수,목,금,토,일 08:30 ~ 16:00");
        businessHours = businessHours.replaceAll("기타 \\(8:30~일몰시\\)", "월,화,수,목,금,토,일 08:30 ~ 16:00");
        businessHours = businessHours.replaceAll("기타 \\(09:00 ~ 13:00 출발시간\\)", "월,화,수,목,금,토,일 09:00 ~ 13:00");
        businessHours = businessHours.replaceAll("기타 \\(주문시간 11시~21시\\)", "월,화,수,목,금,토,일 11:00 ~ 21:00");
        businessHours = businessHours.replaceAll("기타 \\(매장 오픈시간 오전 9시\\)", "월,화,수,목,금,토,일 09:00 ~ 13:00");
        businessHours = businessHours.replaceAll("기타 \\(일출~22시 이용 가능 \\(21:20 입장마감\\)\\)", "월,화,수,목,금,토,일 08:00 ~ 21:20");
        businessHours = businessHours.replaceAll("기타 \\(주문시간 10:00~20:00 / 7,8월 주문시간 9:00~21:00\\)", "월,화,수,목,금,토,일 10:00 ~ 20:00");
        businessHours = businessHours.replaceAll("기타 \\(매월 끝자리 4,9일 장날 ~17:00\\)", "기타 \\(매월 끝자리 4, 9일\\)");
        businessHours = businessHours.replaceAll("매월 2일, 7일, 12일, 17일, 22일, 27일", "기타 \\(매월 끝자리 2, 7일\\)");
        businessHours = businessHours.replaceAll("\n?개장기간 - \\d\\d\\d\\d년 \\d{1,2}월 \\d{1,2}일 ~ \\d{1,2}월 \\d{1,2}일", "");
        businessHours = businessHours.replaceAll("공휴일 매월 끝자리 3, 8일\n", "공휴일 ");
        businessHours = businessHours.replaceAll("[^(]매월 끝자리 3, 8일", "기타 \\(매월 끝자리 3, 8일\\)");
        businessHours = businessHours.replaceAll("공휴일 매월 끝자리 1, 6일\n", "공휴일 ");
        businessHours = businessHours.replaceAll("[^(]매월 끝자리 1, 6일", "기타 \\(매월 끝자리 1, 6일\\)");
        businessHours = businessHours.replaceAll("매월 1, 6, 11, 16, 21, 26일\\(31일이 있는 달은 31일에 열고 1일에 휴무\\)", "기타 \\(매월 끝자리 1, 6일\\)");
        businessHours = businessHours.replaceAll("월,화,수,금,토,일 11:00 ~ 17:00 \\(4시30분 ~ 5시 포장주문만 가능\\)", "월,화,수,금,토,일 11:00 ~ 16:30");
        businessHours = businessHours.replaceAll("월,화,수,목,금,토,일 06:00 ~ 24:00 \\(실내온천\\)\n기타 \\(찜질방 24시간 / 노천탕\\(수영장\\) 11:00 ~ 23:00\\)", "월,화,수,목,금,토,일 11:00 ~ 23:00");
        businessHours = businessHours.replaceAll("월,화,수,목,금,토,일 08:30 ~ 22:00\n설날이랑 추석날은 오후부터 식당운영", "월,화,수,목,금,토,일 12:00 ~ 22:00");
        // 기타 (매월 끝자리 0, 0일) -> closedDays 로
        matcher1 = getMatcher(businessHours, "기타 \\(매월 끝자리 \\d, \\d일\\)");
        if (matcher1.find()) {
            String start = businessHours.substring(0, matcher1.start());
            String end = businessHours.substring(matcher1.end());
            String body = matcher1.group();

            closedDaySet.add(body);
            businessHours = start + end;
            businessHours = businessHours.replaceAll("^\n", "");
            businessHours = businessHours.replaceAll("\n$", "");
        }
        // 브레이크타임, 디너타임, 런치타임 (브레이크타임은 식당에서만 사용함, 식당은 브레이크타임에는 플래닝되지 않음, 따라서 삭제)
        businessHours = businessHours.replaceAll("\n.+브레이크타임.+0", "");
        businessHours = businessHours.replaceAll("\n.+디너타임.+0", "");
        businessHours = businessHours.replaceAll("\n.+런치타임.+0", "");
        businessHours = businessHours.replaceAll("\n기타 \\(브레이크타임 수시변동\\)", "");
        // 재료소진시 마감 or 입장은 마감 1시간 전 (정해진 시간보다 한시간 앞당겨 마감으로 설정 or 13:00)
        businessHours = businessHours.replaceAll("\\(단체손님 있을 시 06:00 ~ 재료 소진시까지\\)", "(재료소진시 마감)");
        businessHours = businessHours.replaceAll("\\(재료.+\\)", "(재료소진시 마감)");
        businessHours = businessHours.replaceAll("재료소진시\\)", "재료소진시 마감)");
        businessHours = businessHours.replaceAll("재료소진시까지\\)", "재료소진시 마감)");
        businessHours = businessHours.replaceAll("재료 소진시 마감\\)", "재료소진시 마감)");
        matcher1 = getMatcher(businessHours, "([월화수목금토일],)+[월화수목금토일] \\d\\d:\\d\\d ~ \\d\\d:\\d\\d \\((재료소진시 마감|입장은 마감 1시간 전)\\)");
        while (matcher1.find()) {
            String start = businessHours.substring(0, matcher1.start());
            String end = businessHours.substring(matcher1.end());
            String body = matcher1.group();
            body = body.replaceAll(" \\((재료소진시 마감|입장은 마감 1시간 전)\\)", "");

            String bodyStart = body.substring(0, body.length() - 5);
            String bodyEnd = body.substring(body.length() - 3);
            String bodyBody = body.substring(body.length() - 5, body.length() - 3);
            int newBodyBody = (Integer.parseInt(bodyBody) - 1);

            businessHours = start + bodyStart + newBodyBody + bodyEnd + end;
            matcher1 = getMatcher(businessHours, "([월화수목금토일],)+[월화수목금토일] \\d\\d:\\d\\d ~ \\d\\d:\\d\\d \\((재료소진시 마감|입장은 마감 1시간 전)\\)");
        }
        matcher1 = getMatcher(businessHours, "기타 \\((오전 )?\\d\\d:\\d\\d ~ 재료소진시 마감\\)");
        while (matcher1.find()) {
            String start = businessHours.substring(0, matcher1.start());
            String end = businessHours.substring(matcher1.end());
            String body = matcher1.group();
            body = body.replaceAll("기타 \\((오전 )?", "월,화,수,목,금,토,일 ");
            body = body.replaceAll("재료소진시 마감\\)", "13:00");

            businessHours = start + body + end;
            matcher1 = getMatcher(businessHours, "기타 \\((오전 )?\\d\\d:\\d\\d ~ 재료소진시 마감\\)");
        }
        // 입장시간, 접수시간, 라스트오더
        businessHours = businessHours.replaceAll("\n.+라스트오더1.+0", ""); // 라스트오더1,2로 나뉜경우는 1이 점심라스트 오더
        businessHours = businessHours.replaceAll("라스트오더2", "라스트오더"); // 브레이크타임과 같은 이유로 삭제가능
        matcher1 = getMatcher(businessHours, "([월화수목금토일],?)+ \\d\\d:\\d\\d ~ \\d\\d:\\d\\d\\n([월화수목금토일],?)+ (라스트오더|입장시간|접수시간) (\\d\\d:\\d\\d )?~ \\d\\d:\\d\\d");
        while (matcher1.find()) {
            String start = businessHours.substring(0, matcher1.start());
            String end = businessHours.substring(matcher1.end());
            String body = matcher1.group();
            body = body.replaceAll("\\d\\d:\\d\\d\\n([월화수목금토일],?)+ (라스트오더|입장시간|접수시간) (\\d\\d:\\d\\d )?~ ", "");

            businessHours = start + body + end;
            matcher1 = getMatcher(businessHours, "([월화수목금토일],?)+ \\d\\d:\\d\\d ~ \\d\\d:\\d\\d\\n([월화수목금토일],?)+ (라스트오더|입장시간|접수시간) (\\d\\d:\\d\\d )?~ \\d\\d:\\d\\d");
        }
        businessHours = businessHours.replaceAll(" 입장시간", "");
        // 동절기, 하절기, 00월~00월, 요일 등 기간에 따라 다른경우 짧은 쪽으로 합침
        matcher1 = getMatcher(businessHours, "(.+\\n)?(.+\\n)?(((월|화|수|목|금|토|일|공휴일),?)+ \\d\\d:\\d\\d ~ \\d\\d:\\d\\d(\\n)?(.+\\n)?(.+\\n)?){2,}");
        if (matcher1.find()) {
            // 동절기, 하절기, 00월~00월 등 삭제
            matcher2 = getMatcher(businessHours, "((월|화|수|목|금|토|일|공휴일),?)+ \\d\\d:\\d\\d ~ \\d\\d:\\d\\d");
            matcher2.find();
            businessHours = matcher2.group();
            while (matcher2.find()) {
                businessHours += "\n";
                businessHours += matcher2.group();
            }
            // 요일 합치기
            String daysOfTheWeek = "";
            for (String dayOfTheWeek : new String[]{"월", "화", "수", "목", "금", "토", "일"}) {
                if (businessHours.contains(dayOfTheWeek)) {
                    daysOfTheWeek += dayOfTheWeek + ",";
                }
            }
            daysOfTheWeek = daysOfTheWeek.substring(0, daysOfTheWeek.length() - 1);
            // 시간 합치기
            List<String> timeList = new ArrayList<>();
            for (String bh : businessHours.split("\n")) {
                timeList.add(bh.substring(bh.length() - 13));
            }
            String time = timeList.get(0);
            for (int j = 1; j < timeList.size(); j++) {
                String newTime = timeList.get(j);
                int timeStart = Integer.parseInt(time.substring(0, 5).replace(":", ""));
                int timeEnd = Integer.parseInt(time.substring(8).replace(":", ""));
                int newTimeStart = Integer.parseInt(newTime.substring(0, 5).replace(":", ""));
                int newTimeEnd = Integer.parseInt(newTime.substring(8).replace(":", ""));

                if (timeStart < newTimeStart) { // new time start is bigger
                    time = time.replaceAll("^\\d\\d:\\d\\d", newTime.substring(0, 5));
                }
                if (timeEnd > newTimeEnd) { // new time end is smaller
                    time = time.replaceAll("\\d\\d:\\d\\d$", newTime.substring(8));
                }
            }
            businessHours = daysOfTheWeek + " " + time;
        }
        businessHours = businessHours.replaceAll("동절기\n", "");
        businessHours = businessHours.replaceAll("하절기\n", "");
        // "" (아무것도 없는경우)
        if (businessHours.length() == 0) {
            businessHours = "월,화,수,목,금,토,일 10:30 ~ 17:00";
        }
        // 월,화,수,목,금,토,일 확인하여 없는 요일 closedDays 로 옮기고 월,화,수,목,금,토,일 삭제
        for (String dayOfTheWeek : new String[]{"월", "화", "수", "목", "금", "토", "일"}) {
            if (!businessHours.contains(dayOfTheWeek)) {
                closedDaySet.add(dayOfTheWeek + "요일");
            }
        }
        businessHours = businessHours.replaceAll("([월화수목금토일],?)+ ", "");

        /* closedDays replace */
        Set<String> closedDaySetTemp = new HashSet<>(closedDaySet);
        for (String closedDay : closedDaySetTemp) {
            if (closedDay.equals("계절별 이용시간이 상이하므로 정확한 시간은 대표번호로 문의") ||
                    closedDay.equals("연중무휴 (만조 및 기상악화 시 통제)") ||
                    closedDay.equals("태풍 등 기상특보 발효 시 휴관") ||
                    closedDay.equals("연중무휴") ||
                    closedDay.equals("우천시") ||
                    closedDay.equals("2018년 02월 12일 ~ 2018년 02월 20일 휴무")) {
                closedDaySet.remove(closedDay);
            } else if (closedDay.matches("((첫|둘|셋|넷|다섯)째,?){2,} [월화수목금토일]요일")) { // 첫째,둘째 월요일
                closedDaySet.remove(closedDay);
                String[] ordinalNumbers = closedDay.split(" ")[0].split(",");
                String dayOfTheWeek = closedDay.split(" ")[1];
                for (String ordinalNumber : ordinalNumbers) {
                    closedDaySet.add(ordinalNumber + " " + dayOfTheWeek);
                }
            } else if (closedDay.matches("([월화수목금토일],?){2,}요일")) { // 화,수요일
                closedDaySet.remove(closedDay);
                String[] daysOfTheWeek = closedDay.split(",");
                for (int j = 0; j < daysOfTheWeek.length - 1; j++) {
                    closedDaySet.add(daysOfTheWeek[j] + "요일");
                }
                closedDaySet.add(daysOfTheWeek[daysOfTheWeek.length - 1]);
            } else if (closedDay.matches("(명절|연중무휴 \\(명절연휴 제외\\))")) { // 명절
                closedDaySet.remove(closedDay);
                closedDaySet.add("설연휴");
                closedDaySet.add("추석연휴");
            } else if (closedDay.matches("기타 \\(매월 끝자리 \\d, \\d일\\)")) { // 기타 (매월 끝자리 1, 6일)
                closedDaySet.remove(closedDay);
                closedDaySet.add(closedDay.replace("기타 (매월 ", "").replace(")", "만 영업"));
            } else if (closedDay.matches("\\d\\d월 ~ \\d\\d월 휴무 [월화수목금토일]요일 \\(매주 \\)")) {
                closedDaySet.remove(closedDay);
                closedDaySet.add(closedDay.replaceAll("\\d\\d월 ~ \\d\\d월 휴무 ", "").replaceAll(" \\(매주 \\)", ""));
            } else if (closedDay.matches("[월화수목금토일]요일 \\(오전\\)")) { // 일요일 (오전)
                closedDaySet.remove(closedDay);
                closedDaySet.add(closedDay.replaceAll(" \\(오전\\)", ""));
            } else if (closedDay.matches("[월화수목금토일]요일 \\(명절 연휴기간 제외\\)")) { // 화요일 (명절 연휴기간 제외)
                closedDaySet.remove(closedDay);
                closedDaySet.add(closedDay.replaceAll(" \\(명절 연휴기간 제외\\)", ""));
            } else if (closedDay.equals("임시 공휴일")) { // 임시 공휴일
                closedDaySet.remove(closedDay);
                closedDaySet.add("공휴일");
            } else if (closedDay.equals("월요일 (단, 월요일이 공휴일인 경우 다음날)")) { // 월요일 (단, 월요일이 공휴일인 경우 다음날)
                closedDaySet.remove(closedDay);
                closedDaySet.add("월요일");
                closedDaySet.add("화요일");
            } else if (closedDay.equals("첫째 월요일 (첫째주 월요일이 공휴일인 경우 둘째주 월요일이 정기휴무)")) { // 첫째 월요일 (첫째주 월요일이 공휴일인 경우 둘째주 월요일이 정기휴무)
                closedDaySet.remove(closedDay);
                closedDaySet.add("첫째 월요일");
                closedDaySet.add("둘째 월요일");
            } else if (closedDay.equals("일요일 (명절과 연휴가 끼어있는 일요일은 정상운영)")) { // 일요일 (명절과 연휴가 끼어있는 일요일은 정상운영)
                closedDaySet.remove(closedDay);
                closedDaySet.add("일요일");
            } else if (closedDay.equals("2018년 03월 ~ 휴무 화요일 (매주)")) { // 2018년 03월 ~ 휴무 화요일 (매주)
                closedDaySet.remove(closedDay);
                closedDaySet.add("화요일");
            } else if (closedDay.equals("개관기념일 (5월24일),훈증소독기간(상반기3일,하반기3일)")) { // 개관기념일 (5월24일),훈증소독기간(상반기3일,하반기3일)
                closedDaySet.remove(closedDay);
                closedDaySet.add("5월24일");
            }
        }

        return new BusinessHours(businessHours, closedDaySet);
    }

    private static void checkKakaoPoiPlusFiles(String area) {
        Set<String> unknownTypeOfBusinessHoursSet = new HashSet<>();
        Set<String> unknownTypeOfClosedDaysSet = new HashSet<>(); //TODO: closed Days도 처리 필요

        for (int i = 0; i < 3; i++) {
            KakaoPoiPlus[] kakaoPoiPluses = getKakaoPoiPluses(area, KAKAOPOIPLUS_FILENAMES[i]);
            for (KakaoPoiPlus kakaoPoiPlus : kakaoPoiPluses) {
                BusinessHours businessHours = getBusinessHours(kakaoPoiPlus);

                // unknown type of businessHours
                if (!businessHours.getBusinessHours().matches("\\d\\d:\\d\\d ~ \\d\\d:\\d\\d")) {
                    unknownTypeOfBusinessHoursSet.add(businessHours.getBusinessHours());
                }

                // unknown type of closedDays
                for (String closedDay : businessHours.getClosedDaySet()) {
                    if (!closedDay.matches("[월화수목금토일]요일") &&
                            !closedDay.matches("(추석|설)(전날|당일|다음날|연휴)") &&
                            !closedDay.matches("(첫|둘|셋|넷|다섯)째 [월화수목금토일]요일") &&
                            !closedDay.matches("공휴일") &&
                            !closedDay.matches("\\d+월\\d+일") &&
                            !closedDay.matches("끝자리 \\d, \\d일만 영업")) {
                        unknownTypeOfClosedDaysSet.add(closedDay);
                    }
                }
            }
        }

        if (unknownTypeOfBusinessHoursSet.size() > 0) {
            System.out.println("Bad: Unknown types of businessHours!!");
            for (String utBusinessHours : unknownTypeOfBusinessHoursSet) {
                System.out.println(utBusinessHours);
                System.out.println();
            }
        } else {
            System.out.println("Good: No unknown types of businessHours!!");
        }

        if (unknownTypeOfClosedDaysSet.size() > 0) {
            System.out.println("Bad: Unknown types of closedDay!!");
            for (String utClosedDay : unknownTypeOfClosedDaysSet) {
                System.out.println(utClosedDay);
                System.out.println();
            }
        } else {
            System.out.println("Good: No unknown types of closedDay!!");
        }
    }

    private static void createBasicPoiJsonFiles(String area) {
        for (int i = 0; i < 5; i++) {
            KakaoPoiPlus[] kakaoPoiPluses = getKakaoPoiPluses(area, KAKAOPOIPLUS_FILENAMES[i]);
            List<BasicPoi> basicPoiList = new ArrayList<>();
            for (KakaoPoiPlus kakaoPoiPlus : kakaoPoiPluses) {
                if (kakaoPoiPlus.getScore() >= MIN_SCORE && kakaoPoiPlus.getNumScoredReviews() >= MIN_NUM_SCORED_REVIEWS && kakaoPoiPlus.getNumReviews() >= MIN_NUM_REVIEWS || i == 4) { // i == 4 -> transportation
                    int id = kakaoPoiPlus.getId();
                    String name = kakaoPoiPlus.getName();
                    PoiType poiType = new PoiType(kakaoPoiPlus.getCategory(), kakaoPoiPlus.getSubCategory(), kakaoPoiPlus.getSubSubcategory());
                    String address = kakaoPoiPlus.getAddress();
                    Location location = new Location(kakaoPoiPlus.getWgsX(), kakaoPoiPlus.getWgsY());
                    double score = kakaoPoiPlus.getScore() < 3 ? kakaoPoiPlus.getScore() + 0.5 : kakaoPoiPlus.getScore(); // 0~3점 애들도 조금이라도 점수 갖을 수 있도록...
                    BusinessHours businessHours;
                    if (i < 3) {
                        businessHours = getBusinessHours(kakaoPoiPlus);
                    } else {
                        String bh = "00:00 ~ 24:00";
                        Set<String> cd = new HashSet<>();
                        businessHours = new BusinessHours(bh, cd);
                    }

                    BasicPoi basicPoi = new BasicPoi(id, name, poiType, address, location, score, businessHours);
                    basicPoiList.add(basicPoi);
                }
            }

            // write json file
            String filename = getNewFilename(BASICPOI_FILENAMES[i], area);
            String json = GSON.toJson(basicPoiList);
            createJsonFile(json, filename);
        }
    }

    private static List<KakaoPoiPlus> getAllKakaoPoiPlusList(String area) {
        List<KakaoPoiPlus> kakaoPoiPlusList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String filename = getDataFilename(area, KAKAOPOIPLUS_FILENAMES[i]);
            KakaoPoiPlus[] kakaoPoiPluses = null;
            try {
                BufferedReader br = new BufferedReader(new FileReader(DATABASE_DIR + filename));
                kakaoPoiPluses = GSON.fromJson(br, KakaoPoiPlus[].class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            kakaoPoiPlusList.addAll(Arrays.asList(kakaoPoiPluses));
        }
        return kakaoPoiPlusList;
    }

    private static List<BasicPoi> getAllBasicPoiList(String area) {
        List<BasicPoi> basicPoiList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String filename = getDataFilename(area, BASICPOI_FILENAMES[i]);
            BasicPoi[] basicPois = null;
            try {
                BufferedReader br = new BufferedReader(new FileReader(DATABASE_DIR + filename));
                basicPois = GSON.fromJson(br, BasicPoi[].class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            basicPoiList.addAll(Arrays.asList(basicPois));
        }
        return basicPoiList;
    }

    private static KakaoPoiPlus getKakaoPoiPlusById(int id, List<KakaoPoiPlus> kakaoPoiPlusList) {
        for (KakaoPoiPlus kakaoPoiPlus : kakaoPoiPlusList) {
            if (id == kakaoPoiPlus.getId()) {
                return kakaoPoiPlus;
            }
        }
        return null;
    }

    private static void createRouteJsonFile(String area) {
        WebDriver driver = getWebDriver(false);
        Matcher matcher;

        List<KakaoPoiPlus> kakaoPoiPlusList = getAllKakaoPoiPlusList(area);
        List<BasicPoi> basicPoiList = getAllBasicPoiList(area);

        for (BasicPoi fromBP : basicPoiList) {
            for (BasicPoi toBP : basicPoiList) {
                if (!fromBP.equals(toBP)) {
                    int fromId = fromBP.getId();
                    int toId = toBP.getId();

                    KakaoPoiPlus fromKPP = getKakaoPoiPlusById(fromId, kakaoPoiPlusList);
                    KakaoPoiPlus toKPP = getKakaoPoiPlusById(toId, kakaoPoiPlusList);
                    String daumMobileMapUrl = "https://m.map.daum.net/actions/carRoute?&sxEnc=" + fromKPP.getMobX() + "&syEnc=" + fromKPP.getMobY() + "&exEnc=" + toKPP.getMobX() + "&eyEnc=" + toKPP.getMobY();
                    driver.navigate().to(daumMobileMapUrl);
                    String pageSource = driver.getPageSource();

                    String routeType;
                    double distance;
                    int time;
                    int taxiFare;
                    int tollFare;
                    List<Location> points = new ArrayList<>();  // wgs84

                    if(pageSource.contains("자동차 길찾기 결과가 없습니다.")){
                        routeType = "car";
                        distance = 10000;
                        time = 10000;
                        taxiFare = 1000000;
                        tollFare = 1000000;
                    }else{
                        routeType = getSubStringByRegexp(pageSource, "routeType : '(.*)',");

                        String distanceStr = getSubStringByRegexp(pageSource, "distance : '(.*)m',");
                        if (!distanceStr.contains("k")) {
                            distance = Double.parseDouble(distanceStr) / 1000;
                        } else {
                            distance = Double.parseDouble(distanceStr.replace("k", ""));
                        }

                        String timeStr = getSubStringByRegexp(pageSource, "time : '(.*)',");
                        if (timeStr.contains("시간")) {
                            String hourStr = getSubStringByRegexp(timeStr, "(.*)시간.*");
                            String minStr = getSubStringByRegexp(timeStr, ".* (.*)분");
                            time = Integer.parseInt(hourStr) * 60 + Integer.parseInt(minStr);
                        } else {
                            String minStr = getSubStringByRegexp(timeStr, "(.*)분");
                            time = Integer.parseInt(minStr);
                        }

                        String taxiFareStr = getSubStringByRegexp(pageSource, "taxiFare : '(.*)',");
                        taxiFareStr = taxiFareStr.length() == 0 ? "0" : taxiFareStr;
                        taxiFare = Integer.parseInt(taxiFareStr.replaceAll(",", ""));

                        String tollFareStr = getSubStringByRegexp(pageSource, "tollFare : '(.*)',");
                        tollFareStr = tollFareStr.length() == 0 ? "0" : tollFareStr;
                        tollFare = Integer.parseInt(tollFareStr.replaceAll(",", ""));

                        String pointsStr = getSubStringByRegexp(pageSource, "points : '(.*)',");    // TODO:수정필요
                    }

                    System.out.println();
                }
            }
        }

    }

    public static void main(String[] args) {
        /* 새로운 지역 추가 방법 */
        // 1. createAreaUrlMap 에 새로운 지역명 및  네이버 여행 가볼만한곳 페이지 url 추가

        // 2. createAreaRects 에 새로운 지역명 및 새로운 지역을 포함하는 지역 좌표 사각형 추가 (좌상단 x,y,우하단 x,y - 구글지도 통해 확인가능)

        // 3. 아래 area 변수 새로운 지역명으로 변경
        String area = "제주특별자치도";

        // 4. createTitleJsonFiles 실행
//        createTitleJsonFiles(area);

        // 5. YYMMddHHmmss_지역명_title_transportation_new.json 파일 생성 및 수작업으로 교통관련 poi title 추가 (지하철, 공항, 터미널, 기차역 등)
        //TODO: 추후에 transportation 파일 자동생성 필요

        // 6. checkTitleFile 실행하여 기존 타이틀과 다른 이름의 poi 또는 없는 poi 확인 후 title.json 파일 수작업으로 수정 (다음맵 검색을 통해 올바른 title로 변경 또는 삭제)
//        checkTitleFile(area, TITLE_FILENAMES[0]); // 0: attraction
//        checkTitleFile(area, TITLE_FILENAMES[1]); // 1: restaurant
//        checkTitleFile(area, TITLE_FILENAMES[2]); // 2: shopping
//        checkTitleFile(area, TITLE_FILENAMES[3]); // 3: accommodation
//        checkTitleFile(area, TITLE_FILENAMES[4]); // 4: transportation

        // 7. createKakaoPoiJsonFiles 실행
//        createKakaoPoiJsonFiles(area);

        // 8. checkKakaoPoiFile 실행하여 category 체크 및 잘못된 검색결과들 title.json 파일 수작업으로 수정     (다음맵 검색을 통해 올바른 title로 변경 또는 삭제)
//        checkKakaoPoiFile(area, KAKAOPOI_FILENAMES[0]); // 0: attraction
//        checkKakaoPoiFile(area, KAKAOPOI_FILENAMES[1]); // 1: restaurant
//        checkKakaoPoiFile(area, KAKAOPOI_FILENAMES[2]); // 2: shopping
//        checkKakaoPoiFile(area, KAKAOPOI_FILENAMES[3]); // 3: accommodation
//        checkKakaoPoiFile(area, KAKAOPOI_FILENAMES[4]); // 4: transportation

        // 9. createKakaoPoiPlusJsonFiles 실행
//        createKakaoPoiPlusJsonFiles(area);

        // 10. checkKakaoPoiPlusFiles 실행하여 unknown type 이 발견되면 trimming 하는 부분에서 처리
//        checkKakaoPoiPlusFiles(area);

        // 11. createBasicPoiJsonFiles 실행
//        createBasicPoiJsonFiles(area);

        // 12. createCategoriesJsonFile 실행
//        createCategoriesJsonFile(area);

        // 13. createRouteJsonFile 실행
        createRouteJsonFile(area); //TODO:
    }
}