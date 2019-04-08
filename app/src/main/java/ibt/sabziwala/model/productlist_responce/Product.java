
package ibt.sabziwala.model.productlist_responce;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Comparator;

public class Product implements Parcelable, Comparable<Product> {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("inCart")
    @Expose
    private boolean inCart;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("quantity_type")
    @Expose
    private String quantityType;
    @SerializedName("selling_price")
    @Expose
    private String sellingPrice;
    @SerializedName("availability")
    @Expose
    private String availability;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("min_quantity")
    @Expose
    private String minQuantity;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("product_quantity")
    @Expose
    private String productQuantity = "0";

    ArrayList<Product> jobCandidate = new ArrayList<>();

    public final static Parcelable.Creator<Product> CREATOR = new Creator<Product>() {


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
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.inCart = ((boolean) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.quantity = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
        this.quantityType = ((String) in.readValue((String.class.getClassLoader())));
        this.sellingPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.availability = ((String) in.readValue((String.class.getClassLoader())));
        this.discount = ((String) in.readValue((String.class.getClassLoader())));
        this.rating = ((String) in.readValue((String.class.getClassLoader())));
        this.minQuantity = ((String) in.readValue((String.class.getClassLoader())));
        this.createdDate = ((String) in.readValue((String.class.getClassLoader())));
        this.productQuantity = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Product() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product withId(String id) {
        this.id = id;
        return this;
    }

    public boolean isInCart() {
        return inCart;
    }

    public void setInCart(boolean inCart) {
        this.inCart = inCart;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Product withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Product withDescription(String description) {
        this.description = description;
        return this;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Product withQuantity(String quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Product withType(String type) {
        this.type = type;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Product withImage(String image) {
        this.image = image;
        return this;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }

    public Product withQuantityType(String quantityType) {
        this.quantityType = quantityType;
        return this;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Product withSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
        return this;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public Product withAvailability(String availability) {
        this.availability = availability;
        return this;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public Product withDiscount(String discount) {
        this.discount = discount;
        return this;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Product withRating(String rating) {
        this.rating = rating;
        return this;
    }

    public String getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(String minQuantity) {
        this.minQuantity = minQuantity;
    }

    public Product withMinQuantity(String minQuantity) {
        this.minQuantity = minQuantity;
        return this;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Product withCreatedDate(String createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(title);
        dest.writeValue(inCart);
        dest.writeValue(description);
        dest.writeValue(quantity);
        dest.writeValue(type);
        dest.writeValue(image);
        dest.writeValue(quantityType);
        dest.writeValue(sellingPrice);
        dest.writeValue(availability);
        dest.writeValue(discount);
        dest.writeValue(rating);
        dest.writeValue(minQuantity);
        dest.writeValue(createdDate);
        dest.writeValue(productQuantity);
    }

    public int describeContents() {
        return 0;
    }


    public static final Comparator<Product> BY_NAME_ALPHABETICAL = new Comparator<Product>() {
        @Override
        public int compare(Product movie, Product t1) {

            return movie.getSellingPrice().compareTo(t1.getSellingPrice());
        }
    };


    @Override
    public int compareTo(@NonNull Product product) {
        return (Integer.parseInt(this.getSellingPrice()) < Integer.parseInt(product.getSellingPrice()) ? -1 :
                (this.getSellingPrice() == product.getSellingPrice() ? 0 : 1));
    }

    public static Comparator<Product> hightolowComparator = new Comparator<Product>() {
        @Override
        public int compare(Product jc1, Product jc2) {
            return (Integer.parseInt(jc1.getSellingPrice()) > Integer.parseInt(jc2.getSellingPrice()) ? -1 :
                    (jc1.getSellingPrice() == jc2.getSellingPrice() ? 0 : 1));
        }
    };

    public static Comparator<Product> nameComparator = new Comparator<Product>() {
        @Override
        public int compare(Product jc1, Product jc2) {
            return (int) (jc1.getTitle().compareToIgnoreCase(jc2.getTitle()));
        }
    };

    @Override
    public String toString() {
        return " Name: " + this.getTitle() + ", Price: " + this.getSellingPrice() + ", Type:" + this.getType();
    }
}