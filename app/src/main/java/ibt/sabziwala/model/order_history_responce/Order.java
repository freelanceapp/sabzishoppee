
package ibt.sabziwala.model.order_history_responce;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order implements Parcelable
{

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("order_number")
    @Expose
    private String orderNumber;
    @SerializedName("order_amount")
    @Expose
    private String orderAmount;
    @SerializedName("order_payment_type")
    @Expose
    private String orderPaymentType;
    @SerializedName("order_transaction_id")
    @Expose
    private String orderTransactionId;
    @SerializedName("order_location")
    @Expose
    private String orderLocation;
    @SerializedName("order_long")
    @Expose
    private String orderLong;
    @SerializedName("order_lat")
    @Expose
    private String orderLat;
    @SerializedName("order_house_number")
    @Expose
    private String orderHouseNumber;
    @SerializedName("order_street_name")
    @Expose
    private String orderStreetName;
    @SerializedName("order_address_type  ")
    @Expose
    private String orderAddressType;
    @SerializedName("order_landmark")
    @Expose
    private String orderLandmark;
    @SerializedName("order_city")
    @Expose
    private String orderCity;
    @SerializedName("order_state")
    @Expose
    private String orderState;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("product")
    @Expose
    private List<Product> product = new ArrayList<Product>();
    public final static Creator<Order> CREATOR = new Creator<Order>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        public Order[] newArray(int size) {
            return (new Order[size]);
        }

    }
    ;

    protected Order(Parcel in) {
        this.orderId = ((String) in.readValue((String.class.getClassLoader())));
        this.orderNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.orderAmount = ((String) in.readValue((String.class.getClassLoader())));
        this.orderPaymentType = ((String) in.readValue((String.class.getClassLoader())));
        this.orderTransactionId = ((String) in.readValue((String.class.getClassLoader())));
        this.orderLocation = ((String) in.readValue((String.class.getClassLoader())));
        this.orderLong = ((String) in.readValue((String.class.getClassLoader())));
        this.orderLat = ((String) in.readValue((String.class.getClassLoader())));
        this.orderHouseNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.orderStreetName = ((String) in.readValue((String.class.getClassLoader())));
        this.orderAddressType = ((String) in.readValue((String.class.getClassLoader())));
        this.orderLandmark = ((String) in.readValue((String.class.getClassLoader())));
        this.orderCity = ((String) in.readValue((String.class.getClassLoader())));
        this.orderState = ((String) in.readValue((String.class.getClassLoader())));
        this.orderStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.orderDate = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.product, (ibt.sabziwala.model.order_history_responce.Product.class.getClassLoader()));
    }

    public Order() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Order withOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Order withOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Order withOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
        return this;
    }

    public String getOrderPaymentType() {
        return orderPaymentType;
    }

    public void setOrderPaymentType(String orderPaymentType) {
        this.orderPaymentType = orderPaymentType;
    }

    public Order withOrderPaymentType(String orderPaymentType) {
        this.orderPaymentType = orderPaymentType;
        return this;
    }

    public String getOrderTransactionId() {
        return orderTransactionId;
    }

    public void setOrderTransactionId(String orderTransactionId) {
        this.orderTransactionId = orderTransactionId;
    }

    public Order withOrderTransactionId(String orderTransactionId) {
        this.orderTransactionId = orderTransactionId;
        return this;
    }

    public String getOrderLocation() {
        return orderLocation;
    }

    public void setOrderLocation(String orderLocation) {
        this.orderLocation = orderLocation;
    }

    public Order withOrderLocation(String orderLocation) {
        this.orderLocation = orderLocation;
        return this;
    }

    public String getOrderLong() {
        return orderLong;
    }

    public void setOrderLong(String orderLong) {
        this.orderLong = orderLong;
    }

    public Order withOrderLong(String orderLong) {
        this.orderLong = orderLong;
        return this;
    }

    public String getOrderLat() {
        return orderLat;
    }

    public void setOrderLat(String orderLat) {
        this.orderLat = orderLat;
    }

    public Order withOrderLat(String orderLat) {
        this.orderLat = orderLat;
        return this;
    }

    public String getOrderHouseNumber() {
        return orderHouseNumber;
    }

    public void setOrderHouseNumber(String orderHouseNumber) {
        this.orderHouseNumber = orderHouseNumber;
    }

    public Order withOrderHouseNumber(String orderHouseNumber) {
        this.orderHouseNumber = orderHouseNumber;
        return this;
    }

    public String getOrderStreetName() {
        return orderStreetName;
    }

    public void setOrderStreetName(String orderStreetName) {
        this.orderStreetName = orderStreetName;
    }

    public Order withOrderStreetName(String orderStreetName) {
        this.orderStreetName = orderStreetName;
        return this;
    }

    public String getOrderAddressType() {
        return orderAddressType;
    }

    public void setOrderAddressType(String orderAddressType) {
        this.orderAddressType = orderAddressType;
    }

    public Order withOrderAddressType(String orderAddressType) {
        this.orderAddressType = orderAddressType;
        return this;
    }

    public String getOrderLandmark() {
        return orderLandmark;
    }

    public void setOrderLandmark(String orderLandmark) {
        this.orderLandmark = orderLandmark;
    }

    public Order withOrderLandmark(String orderLandmark) {
        this.orderLandmark = orderLandmark;
        return this;
    }

    public String getOrderCity() {
        return orderCity;
    }

    public void setOrderCity(String orderCity) {
        this.orderCity = orderCity;
    }

    public Order withOrderCity(String orderCity) {
        this.orderCity = orderCity;
        return this;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public Order withOrderState(String orderState) {
        this.orderState = orderState;
        return this;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Order withOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Order withOrderDate(String orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public Order withProduct(List<Product> product) {
        this.product = product;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(orderId);
        dest.writeValue(orderNumber);
        dest.writeValue(orderAmount);
        dest.writeValue(orderPaymentType);
        dest.writeValue(orderTransactionId);
        dest.writeValue(orderLocation);
        dest.writeValue(orderLong);
        dest.writeValue(orderLat);
        dest.writeValue(orderHouseNumber);
        dest.writeValue(orderStreetName);
        dest.writeValue(orderAddressType);
        dest.writeValue(orderLandmark);
        dest.writeValue(orderCity);
        dest.writeValue(orderState);
        dest.writeValue(orderStatus);
        dest.writeValue(orderDate);
        dest.writeList(product);
    }

    public int describeContents() {
        return  0;
    }

}
