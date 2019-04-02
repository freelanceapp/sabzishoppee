
package ibt.sabzishoppee.model.address_show_responce;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressShowModel implements Parcelable
{

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("address")
    @Expose
    private List<Address> address = new ArrayList<Address>();
    public final static Creator<AddressShowModel> CREATOR = new Creator<AddressShowModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AddressShowModel createFromParcel(Parcel in) {
            return new AddressShowModel(in);
        }

        public AddressShowModel[] newArray(int size) {
            return (new AddressShowModel[size]);
        }

    }
    ;

    protected AddressShowModel(Parcel in) {
        this.error = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.address, (Address.class.getClassLoader()));
    }

    public AddressShowModel() {
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public AddressShowModel withError(Boolean error) {
        this.error = error;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AddressShowModel withMessage(String message) {
        this.message = message;
        return this;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public AddressShowModel withAddress(List<Address> address) {
        this.address = address;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(error);
        dest.writeValue(message);
        dest.writeList(address);
    }

    public int describeContents() {
        return  0;
    }

}
