package com.example.barcodescanner;

public class DataModal {
    private int customerId;
    private String customerName;
    private String dateForTransport;
    private String letterName;
    private String orderDate;
    private String orderPrice;
    private String phoneNumber;
    private boolean isWhatsApp;
    private String message;

    public DataModal(int customerId, String customerName, String dateForTransport, String letterName, String orderDate, String orderPrice, String phoneNumber, boolean isWhatsApp) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.dateForTransport = dateForTransport;
        this.letterName = letterName;
        this.orderDate = orderDate;
        this.orderPrice = orderPrice;
        this.phoneNumber = phoneNumber;
        this.isWhatsApp = isWhatsApp;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDateForTransport() {
        return dateForTransport;
    }

    public void setDateForTransport(String dateForTransport) {
        this.dateForTransport = dateForTransport;
    }

    public String getLetterName() {
        return letterName;
    }

    public void setLetterName(String letterName) {
        this.letterName = letterName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isWhatsApp() {
        return isWhatsApp;
    }

    public void setWhatsApp(boolean isWhatsApp) {
        this.isWhatsApp = isWhatsApp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
