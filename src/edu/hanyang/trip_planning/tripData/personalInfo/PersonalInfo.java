package edu.hanyang.trip_planning.tripData.personalInfo;

import edu.hanyang.trip_planning.trip_question.PreferenceOfPOIType;

public class PersonalInfo {
//    public Gender gender = Gender.UNKNOWN;       // 성별: 남성, 여성
//    public int age = -1;                         // 나이: -1: 정보없음, 0이상 : 실제 나이
//    public String job = null;                    // 직업
//    public int numChildren = -1;                 // 자녀수: -1: 정보없음, 0 : 자녀없음, 1이상: 자녀수
//    public Married married = Married.Unknown;    // 결혼여부: -1: 정보없음, 0 : 미혼, 1: 결혼
//    public Religion religion = Religion.Unknown;
    public PreferenceOfPOIType preferenceOfPOIType;

//    public void setAge(int age){
//        this.age =age;
//    }
//    public void setJob(String job){
//        this.job = job;
//    }
//    public void setNumChildren(int numChildren){
//        this.numChildren=numChildren;
//    }
//    public void setMarried(Married married){
//        this.married=married;
//    }
//    public void setGender(Gender gender){
//        this.gender=gender;
//    }

    public void setPreferenceOfPOIType(PreferenceOfPOIType preferenceOfPOIType){
        this.preferenceOfPOIType = preferenceOfPOIType;
    }

    public PreferenceOfPOIType getPreferenceOfPOIType() {
        return preferenceOfPOIType;
    }

    public String toString() {
        StringBuffer strBuf = new StringBuffer();
//        strBuf.append("나이: " + age + "세\n");
//        strBuf.append("성별: " + gender + "\n");
//        strBuf.append("직업:" + job + "\n");
//        strBuf.append("자녀수:" + numChildren + "\n");
//        strBuf.append("종교:" + religion + "\n");
        strBuf.append("선호도:" + preferenceOfPOIType);
        return strBuf.toString();
    }

    public static void main(String[] args) {
        System.out.println(PersonalInfoFactory.personalInfoExample1());
    }
}

