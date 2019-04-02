
package ibt.sabzishoppee.model.order_history_responce;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product implements Parcelable
{

    @SerializedName("product_order_details_id")
    @Expose
    private String productOrderDetailsId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_type")
    @Expose
    private String productType;
    @SerializedName("product_quantity")
    @Expose
    private String productQuantity;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_discount")
    @Expose
    private String productDiscount;
    @SerializedName("product_status")
    @Expose
    private String productStatus;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    public final static Creator<Product> CREATOR = new Creator<Product>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return (new Product[size]);
        }

    }
    ;

    protected Product(Parcel in) {
        this.productOrderDetailsId = ((String) in.readValue((String.class.getClassLoader())));
        this.productId = ((String) in.readValue((String.class.getClassLoader())));
        this.productName = ((String) in.readValue((String.class.getClassLoader())));
        this.productType = ((String) in.readValue((String.class.getClassLoader())));
        this.productQuantity = ((String) in.readValue((String.class.getClassLoader())));
        this.productPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.productDiscount = ((String) in.readValue((String.class.getClassLoader())));
        this.productStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.orderDate = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Product() {
    }

    public String getProductOrderDetailsId() {
        return productOrderDetailsId;
    }

    public void setProductOrderDetailsId(String productOrderDetailsId) {
        this.productOrderDetailsId = productOrderDetailsId;
    }

    public Product withProductOrderDetailsId(String productOrderDetailsId) {
        this.productOrderDetailsId = productOrderDetailsId;
        return this;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Product withProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Product withProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Product withProductType(String productType) {
        this.productType = productType;
        return this;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Product withProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
        return this;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public Product withProductPrice(String productPrice) {
        this.productPrice = productPrice;
        return this;
    }

    public String getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(String productDiscount) {
        this.productDiscount = productDiscount;
    }

    public Product withProductDiscount(String productDiscount) {
        this.productDiscount = productDiscount;
        return this;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public Product withProductStatus(String productStatus) {
        this.productStatus = productStatus;
        return this;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Product withOrderDate(String orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(productOrderDetailsId);
        dest.writeValue(productId);
        dest.writeValue(productName);
        dest.writeValue(productType);
        dest.writeValue(productQuantity);
        dest.writeValue(productPrice);
        dest.writeValue(productDiscount);
        dest.writeValue(productStatus);
        dest.writeValue(orderDate);
    }

    public int describeContents() {
        return  0;
    }

}
