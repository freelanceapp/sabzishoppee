
package ibt.sabzishoppee.model.banner_responce;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BannerModel implements Parcelable
{

    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("appslider")
    @Expose
    private List<Appslider> appslider = new ArrayList<Appslider>();
    public final static Creator<BannerModel> CREATOR = new Creator<BannerModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public BannerModel createFromParcel(Parcel in) {
            return new BannerModel(in);
        }

        public BannerModel[] newArray(int size) {
            return (new BannerModel[size]);
        }

    }
    ;

    protected BannerModel(Parcel in) {
        this.result = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.appslider, (Appslider.class.getClassLoader()));
    }

    public BannerModel() {
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public BannerModel withResult(Boolean result) {
        this.result = result;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BannerModel withMessage(String message) {
        this.message = message;
        return this;
    }

    public List<Appslider> getAppslider() {
        return appslider;
    }

    public void setAppslider(List<Appslider> appslider) {
        this.appslider = appslider;
    }

    public BannerModel withAppslider(List<Appslider> appslider) {
        this.appslider = appslider;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(result);
        dest.writeValue(message);
        dest.writeList(appslider);
    }

    public int describeContents() {
        return  0;
    }

}
