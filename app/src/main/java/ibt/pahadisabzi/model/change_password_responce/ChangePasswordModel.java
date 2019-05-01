package ibt.pahadisabzi.model.change_password_responce;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePasswordModel implements Parcelable
{

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user_id")
    @Expose
    private String userId;
    public final static Parcelable.Creator<ChangePasswordModel> CREATOR = new Creator<ChangePasswordModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ChangePasswordModel createFromParcel(Parcel in) {
            return new ChangePasswordModel(in);
        }

        public ChangePasswordModel[] newArray(int size) {
            return (new ChangePasswordModel[size]);
        }

    }
            ;

    protected ChangePasswordModel(Parcel in) {
        this.error = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ChangePasswordModel() {
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public ChangePasswordModel withError(Boolean error) {
        this.error = error;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChangePasswordModel withMessage(String message) {
        this.message = message;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ChangePasswordModel withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(error);
        dest.writeValue(message);
        dest.writeValue(userId);
    }

    public int describeContents() {
        return 0;
    }

}
