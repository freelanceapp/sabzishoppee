
package ibt.pahadisabzi.model.cart_responce;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartDatum implements Parcelable
{

    @SerializedName("availability")
    @Expose
    private String availability;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("inCart")
    @Expose
    private Boolean inCart;
    @SerializedName("keyId")
    @Expose
    private Integer keyId;
    @SerializedName("min_quantity")
    @Expose
    private String minQuantity;
    @SerializedName("order_quantity")
    @Expose
    private String orderQuantity;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("quantity_type")
    @Expose
    private String quantityType;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("type")
    @Expose
    private String type;
    public final static Creator<CartDatum> CREATOR = new Creator<CartDatum>() {


        @SuppressWarnings({
            "unchecked"
        })
        public CartDatum createFromParcel(Parcel in) {
            return new CartDatum(in);
        }

        public CartDatum[] newArray(int size) {
            return (new CartDatum[size]);
        }

    }
    ;

    protected CartDatum(Parcel in) {
        this.availability = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.discount = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
        this.inCart = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.keyId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.minQuantity = ((String) in.readValue((String.class.getClassLoader())));
        this.orderQuantity = ((String) in.readValue((String.class.getClassLoader())));
        this.price = ((String) in.readValue((String.class.getClassLoader())));
        this.quantity = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.quantityType = ((String) in.readValue((String.class.getClassLoader())));
        this.rating = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CartDatum() {
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public CartDatum withAvailability(String availability) {
        this.availability = availability;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CartDatum withDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public CartDatum withDiscount(String discount) {
        this.discount = discount;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CartDatum withId(String id) {
        this.id = id;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CartDatum withImage(String image) {
        this.image = image;
        return this;
    }

    public Boolean getInCart() {
        return inCart;
    }

    public void setInCart(Boolean inCart) {
        this.inCart = inCart;
    }

    public CartDatum withInCart(Boolean inCart) {
        this.inCart = inCart;
        return this;
    }

    public Integer getKeyId() {
        return keyId;
    }

    public void setKeyId(Integer keyId) {
        this.keyId = keyId;
    }

    public CartDatum withKeyId(Integer keyId) {
        this.keyId = keyId;
        return this;
    }

    public String getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(String minQuantity) {
        this.minQuantity = minQuantity;
    }

    public CartDatum withMinQuantity(String minQuantity) {
        this.minQuantity = minQuantity;
        return this;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public CartDatum withOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public CartDatum withPrice(String price) {
        this.price = price;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public CartDatum withQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }

    public CartDatum withQuantityType(String quantityType) {
        this.quantityType = quantityType;
        return this;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public CartDatum withRating(String rating) {
        this.rating = rating;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CartDatum withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CartDatum withType(String type) {
        this.type = type;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(availability);
        dest.writeValue(description);
        dest.writeValue(discount);
        dest.writeValue(id);
        dest.writeValue(image);
        dest.writeValue(inCart);
        dest.writeValue(keyId);
        dest.writeValue(minQuantity);
        dest.writeValue(orderQuantity);
        dest.writeValue(price);
        dest.writeValue(quantity);
        dest.writeValue(quantityType);
        dest.writeValue(rating);
        dest.writeValue(title);
        dest.writeValue(type);
    }

    public int describeContents() {
        return  0;
    }

}
