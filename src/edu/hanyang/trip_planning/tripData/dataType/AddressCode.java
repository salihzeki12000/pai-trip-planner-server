package edu.hanyang.trip_planning.tripData.dataType;

/**
 * 주소를 표현하는 클래스
 * 모든 항목은 null 이 될수 있지만,
 */
public class AddressCode {
    public String countryCode = null;   // 국가코드
    public String provinceCode = null;  // 특별/광역시, 도
    public String cityCode = null;      // 시, 군

    public AddressCode() {
        this.countryCode = null;
        this.provinceCode = null;
        this.cityCode = null;
    }

    public AddressCode(String countryCode) {
        this.countryCode = countryCode;
        this.provinceCode = null;
        this.cityCode = null;
    }

    public AddressCode(String countryCode, String provinceCode) {
        this.countryCode = countryCode;
        this.provinceCode = provinceCode;
        this.cityCode = null;
    }

    public AddressCode(String countryCode, String provinceCode, String cityCode) {
        this.countryCode = countryCode;
        this.provinceCode = provinceCode;
        this.cityCode = cityCode;
    }

    public AddressCode(String strs[]) {
        if (strs.length==1){
            this.countryCode = strs[0];
            this.provinceCode = null;
            this.cityCode = null;
        }
        else if (strs.length==2){
            this.countryCode = strs[0];
            this.provinceCode = strs[1];
            this.cityCode = null;
        }
        else if (strs.length==3){
            this.countryCode = strs[0];
            this.provinceCode = strs[1];
            this.cityCode =  strs[2];
        }
    }

    public AddressCode(AddressCode addressCode) {
        this.countryCode = addressCode.countryCode;
        this.provinceCode = addressCode.provinceCode;
        this.cityCode = addressCode.cityCode;
    }

    public boolean contain(AddressCode addressCode) {
        if (this.countryCode.equals(addressCode.countryCode)) {
            if (this.provinceCode == null || this.provinceCode.length() == 0) {
                return true;
            } else if (this.provinceCode.equals(addressCode.provinceCode)) {
                if (this.cityCode == null || this.cityCode.length() == 0) {
                    return true;
                } else return this.cityCode.equals(addressCode.cityCode);

            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public AddressCode deepCopy() {
        return new AddressCode(this.countryCode, this.provinceCode, this.cityCode);
    }

    @Override
    public String toString() {
        if (provinceCode == null && cityCode == null) {
            return countryCode;
        } else if (cityCode == null) {
            return countryCode + "-" + provinceCode;
        } else {
            return countryCode + "-" + provinceCode + "-" + cityCode;
        }
    }

    public String toShortString() {
        if (provinceCode == null && cityCode == null) {
            return "";
        } else if (cityCode == null) {
            return provinceCode;
        } else {
            return provinceCode + "-" + cityCode;
        }
    }

    public static void main(String[] args) {
        AddressCode a1 = new AddressCode("대한민국", "서울특별시", "성동구");
        AddressCode a2 = new AddressCode("대한민국", "서울특별시");

        System.out.println(a2.contain(a1));
    }
}
