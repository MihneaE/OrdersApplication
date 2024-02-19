package Model;

import java.util.Objects;

public class DeliveryInfo implements Cloneable{
    private String name;
    private String phone;
    private String address;
    private String apartment;
    private String floor;

    public DeliveryInfo()
    {}

    public DeliveryInfo(String name, String phone, String address, String apartment, String floor)
    {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.apartment = apartment;
        this.floor = floor;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getFloor() {
        return floor;
    }

    public String getApartment() {
        return apartment;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryInfo that = (DeliveryInfo) o;
        return Objects.equals(name, that.name) && Objects.equals(phone, that.phone) && Objects.equals(address, that.address) && Objects.equals(apartment, that.apartment) && Objects.equals(floor, that.floor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, address, apartment, floor);
    }

    @Override
    public String toString() {
        return "DeliveryInfo{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", apartment='" + apartment + '\'' +
                ", floor='" + floor + '\'' +
                '}';
    }

    @Override
    public DeliveryInfo clone() {
        try {

            return (DeliveryInfo) super.clone();
        } catch (CloneNotSupportedException e) {

            throw new RuntimeException("This class does not implement Cloneable", e);
        }
    }
}
