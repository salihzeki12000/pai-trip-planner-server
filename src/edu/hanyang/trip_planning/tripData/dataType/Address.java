package edu.hanyang.trip_planning.tripData.dataType;

/**
 * 주소를 표현하는 클래스
 * 모든 항목은 null 이 될수 있지만,
 */
public class Address {
    public AddressCode addressCode; // 국가, 도, 시
    public String detailedAddress;  // 상세주소

    public Address() {
        this.addressCode = new AddressCode();
        this.detailedAddress = "";
    }

    public Address(String countryCode, String provinceCode, String cityCode, String detailedAddress) {
        this.addressCode = new AddressCode(countryCode, provinceCode, cityCode);
        this.detailedAddress = detailedAddress;
    }

    public Address(AddressCode addressCode, String detailedAddress) {
        this.addressCode = addressCode.deepCopy();
        this.detailedAddress = detailedAddress;
    }

    public Address(Address arg) {
        this.addressCode = new AddressCode(arg.addressCode);
        this.detailedAddress = arg.detailedAddress;
    }

    public Address deepCopy() {
        return new Address(this);
    }

    @Override
    public String toString() {
        return addressCode.toString() + ", " + detailedAddress;
    }

    public String toShortString() {
        return addressCode.toShortString();
    }
    public static Address dumyGen() {
        Address address = new Address("대한민국", "서울특별시", "성동구", "왕십리로 222");
        return address;
    }

    public static void main(String[] args) {
        System.out.println("dumyGen() = " + dumyGen());
    }

}
