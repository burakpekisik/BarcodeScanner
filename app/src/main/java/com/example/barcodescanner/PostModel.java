package com.example.barcodescanner;

public class PostModel {
    private String order_id;
    private String letter_name;
    private int customer_id;
    private String date_for_transport;
    private String order_price;
    private String order_date;
    private String phone_number;
    private String customer_name;
    private String email;
    private String track_id;

    public String getID() {
        return order_id;
    }

    public void setID(String ID) {
        this.order_id = ID;
    }

    public String getLetterName() {
        return letter_name;
    }

    public void setLetterName(String letterName) {
        this.letter_name = letterName;
    }

    public int getCustomerId() {
        return customer_id;
    }

    public void setCustomerId(int customerId) {
        this.customer_id = customerId;
    }

    public String getDateForTransport() {
        return date_for_transport;
    }

    public void setDateForTransport(String dateForTransport) {
        this.date_for_transport = dateForTransport;
    }

    public String getOrderPrice() {
        return order_price;
    }

    public void setOrderPrice(String orderPrice) {
        this.order_price = orderPrice;
    }

    public String getOrderDate() {
        return order_date;
    }

    public void setOrderDate(String orderDate) {
        this.order_date = orderDate;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phone_number = phoneNumber;
    }

    public String getCustomerName() {
        return customer_name;
    }

    public void setCustomerName(String customerName) {
        this.customer_name = customerName;
    }

    public String getEmail() {
        if (!email.isEmpty()) {
            return email;
        }
        else {
            email = "Bu Bir WhatsApp Siparişidir.";
            return email;
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTrackId() {
        return track_id;
    }

    public void setTrackId(String trackId) {
        this.track_id = trackId;
    }
}
