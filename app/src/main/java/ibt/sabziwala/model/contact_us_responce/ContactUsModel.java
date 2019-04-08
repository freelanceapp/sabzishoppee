package ibt.sabziwala.model.contact_us_responce;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactUsModel implements Parcelable
{

    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("message")
    @Expose
    private String message;
    public final static Parcelable.Creator<ContactUsModel> CREATOR = new Creator<ContactUsModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ContactUsModel createFromParcel(Parcel in) {
            return new ContactUsModel(in);
        }

        public ContactUsModel[] newArray(int size) {
            return (new ContactUsModel[size]);
        }

    }
            ;

    protected ContactUsModel(Parcel in) {
        this.result = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ContactUsModel() {
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public ContactUsModel withResult(Boolean result) {
        this.result = result;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ContactUsModel withMessage(String message) {
        this.message = message;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(result);
        dest.writeValue(message);
    }

    public int describeContents() {
        return 0;
    }

}