package su.balynsky.android.atms.model.atm;

public class AtmInfo {
    private String Code = null;
    private String Latitude = null;
    private String Longitude = null;
    private Boolean CommissionFlag = null;
    private Boolean DollarsFlag = null;
    private String Hours = null;
    private String Telephone = null;
    private String Name = null;
    private String AddressL1 = null;
    private String AddressL2 = null;
    private String City = null;
    private String State = null;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public Boolean getCommissionFlag() {
        return CommissionFlag;
    }

    public void setCommissionFlag(Boolean commissionFlag) {
        CommissionFlag = commissionFlag;
    }

    public Boolean getDollarsFlag() {
        return DollarsFlag;
    }

    public void setDollarsFlag(Boolean dollarsFlag) {
        DollarsFlag = dollarsFlag;
    }

    public String getHours() {
        return Hours;
    }

    public void setHours(String hours) {
        Hours = hours;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddressL1() {
        return AddressL1;
    }

    public void setAddressL1(String addressL1) {
        AddressL1 = addressL1;
    }

    public String getAddressL2() {
        return AddressL2;
    }

    public void setAddressL2(String addressL2) {
        AddressL2 = addressL2;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AtmInfo [Code=");
        builder.append(Code);
        builder.append(", Latitude=");
        builder.append(Latitude);
        builder.append(", Longitude=");
        builder.append(Longitude);
        builder.append(", CommissionFlag=");
        builder.append(CommissionFlag);
        builder.append(", DollarsFlag=");
        builder.append(DollarsFlag);
        builder.append(", Hours=");
        builder.append(Hours);
        builder.append(", Telephone=");
        builder.append(Telephone);
        builder.append(", Name=");
        builder.append(Name);
        builder.append(", AddressL1=");
        builder.append(AddressL1);
        builder.append(", AddressL2=");
        builder.append(AddressL2);
        builder.append(", City=");
        builder.append(City);
        builder.append(", State=");
        builder.append(State);
        builder.append("]");
        return builder.toString();
    }
}
