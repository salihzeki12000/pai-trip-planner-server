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
}
