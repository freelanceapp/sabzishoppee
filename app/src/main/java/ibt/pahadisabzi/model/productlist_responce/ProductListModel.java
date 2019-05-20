package ibt.pahadisabzi.model.productlist_responce;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductListModel implements Parcelable
{

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("product")
    @Expose
    private List<Product> product = null;
    @SerializedName("appslider")
    @Expose
    private List<Appslider> appslider = null;
    public final static Parcelable.Creator<ProductListModel> CREATOR = new Creator<ProductListModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ProductListModel createFromParcel(Parcel in) {
            return new ProductListModel(in);
        }

        public ProductListModel[] newArray(int size) {
            return (new ProductListModel[size]);
        }

    }
            ;

    protected ProductListModel(Parcel in) {
        this.error = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.product, (ibt.pahadisabzi.model.productlist_responce.Product.class.getClassLoader()));
        in.readList(this.appslider, (ibt.pahadisabzi.model.productlist_responce.Appslider.class.getClassLoader()));
    }

    public ProductListModel() {
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public List<Appslider> getAppslider() {
        return appslider;
    }

    public void setAppslider(List<Appslider> appslider) {
        this.appslider = appslider;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(error);
        dest.writeValue(message);
        dest.writeList(product);
        dest.writeList(appslider);
    }

    public int describeContents() {
        return 0;
    }

}