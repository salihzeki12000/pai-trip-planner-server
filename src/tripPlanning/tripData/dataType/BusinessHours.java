package tripPlanning.tripData.dataType;

import java.util.Set;

public class BusinessHours {
    private String businessHours;
    private Set<String> closedDaySet;

    public BusinessHours(String businessHours, Set<String> closedDaySet) {
        this.businessHours = businessHours;
        this.closedDaySet = closedDaySet;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public Set<String> getClosedDaySet() {
        return closedDaySet;
    }

    public void setClosedDaySet(Set<String> closedDaySet) {
        this.closedDaySet = closedDaySet;
    }

    @Override
    public String toString() {
        return "BusinessHours{" +
                "businessHours='" + businessHours + '\'' +
                ", closedDaySet=" + closedDaySet +
                '}';
    }
}
