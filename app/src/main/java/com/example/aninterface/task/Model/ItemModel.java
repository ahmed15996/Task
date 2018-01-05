package com.example.aninterface.task.Model;

import android.os.Parcel;
import android.os.Parcelable;



import java.util.List;

public class ItemModel {

    private List<Result> result = null;

    private Status status;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static class Result implements Parcelable{
        private int orderId;

        private int orderUserId;

        private int status;

        private String lat;

        private String _long;

        private String orderDate;

        private List<Product> products = null;

        public Result(int orderId, int orderUserId, int status, String lat, String _long, String orderDate, List<Product> products) {
            this.orderId = orderId;
            this.orderUserId = orderUserId;
            this.status = status;
            this.lat = lat;
            this._long = _long;
            this.orderDate = orderDate;
            this.products = products;
        }

        protected Result(Parcel in) {
            orderId = in.readInt();
            orderUserId = in.readInt();
            status = in.readInt();
            lat = in.readString();
            _long = in.readString();
            orderDate = in.readString();
        }

        public static final Creator<Result> CREATOR = new Creator<Result>() {
            @Override
            public Result createFromParcel(Parcel in) {
                return new Result(in);
            }

            @Override
            public Result[] newArray(int size) {
                return new Result[size];
            }
        };

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getOrderUserId() {
            return orderUserId;
        }

        public void setOrderUserId(int orderUserId) {
            this.orderUserId = orderUserId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLong() {
            return _long;
        }

        public void setLong(String _long) {
            this._long = _long;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(orderId);
            parcel.writeInt(orderUserId);
            parcel.writeInt(status);
            parcel.writeString(lat);
            parcel.writeString(_long);
            parcel.writeString(orderDate);
        }
    }

    public class Status {


        private int succeed;

        private List<String> message = null;

        public int getSucceed() {
            return succeed;
        }

        public void setSucceed(int succeed) {
            this.succeed = succeed;
        }

        public List<String> getMessage() {
            return message;
        }

        public void setMessage(List<String> message) {
            this.message = message;
        }

    }

    public static class Product implements Parcelable{


        private String productName;

        private String productImage;

        private int productRate;

        private String userName;

        private String userMobile;

        private String userImage;

        private int orderPhone;

        public Product(String productName, String productImage, int productRate, String userName, String userMobile, String userImage, int orderPhone) {
            this.productName = productName;
            this.productImage = productImage;
            this.productRate = productRate;
            this.userName = userName;
            this.userMobile = userMobile;
            this.userImage = userImage;
            this.orderPhone = orderPhone;
        }

        protected Product(Parcel in) {
            productName = in.readString();
            productImage = in.readString();
            productRate = in.readInt();
            userName = in.readString();
            userMobile = in.readString();
            userImage = in.readString();
            orderPhone = in.readInt();
        }

        public static final Creator<Product> CREATOR = new Creator<Product>() {
            @Override
            public Product createFromParcel(Parcel in) {
                return new Product(in);
            }

            @Override
            public Product[] newArray(int size) {
                return new Product[size];
            }
        };

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public int getProductRate() {
            return productRate;
        }

        public void setProductRate(int productRate) {
            this.productRate = productRate;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserMobile() {
            return userMobile;
        }

        public void setUserMobile(String userMobile) {
            this.userMobile = userMobile;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public int getOrderPhone() {
            return orderPhone;
        }

        public void setOrderPhone(int orderPhone) {
            this.orderPhone = orderPhone;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(productName);
            parcel.writeString(productImage);
            parcel.writeInt(productRate);
            parcel.writeString(userName);
            parcel.writeString(userMobile);
            parcel.writeString(userImage);
            parcel.writeInt(orderPhone);
        }
    }
}

