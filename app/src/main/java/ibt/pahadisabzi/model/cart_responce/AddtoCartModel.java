
package ibt.pahadisabzi.model.cart_responce;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddtoCartModel implements Parcelable
{

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("cart_data")
    @Expose
    private List<CartDatum> cartData = new ArrayList<CartDatum>();
    public final static Creator<AddtoCartModel> CREATOR = new Creator<AddtoCartModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AddtoCartModel createFromParcel(Parcel in) {
            return new AddtoCartModel(in);
        }

        public AddtoCartModel[] newArray(int size) {
            return (new AddtoCartModel[size]);
        }

    }
    ;

    protected AddtoCartModel(Parcel in) {
        this.error = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.cartData, (ibt.pahadisabzi.model.cart_responce.CartDatum.class.getClassLoader()));
    }

    public AddtoCartModel() {
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public AddtoCartModel withError(Boolean error) {
        this.error = error;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AddtoCartModel withMessage(String message) {
        this.message = message;
        return this;
    }

    public List<CartDatum> getCartData() {
        return cartData;
    }

    public void setCartData(List<CartDatum> cartData) {
        this.cartData = cartData;
    }

    public AddtoCartModel withCartData(List<CartDatum> cartData) {
        this.cartData = cartData;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(error);
        dest.writeValue(message);
        dest.writeList(cartData);
    }

    public int describeContents() {
        return  0;
    }

}
