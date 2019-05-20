package ibt.pahadisabzi.model.productlist_responce;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Appslider implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("slider")
    @Expose
    private String slider;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("updated_date")
    @Expose
    private String updatedDate;
    public final static Parcelable.Creator<Appslider> CREATOR = new Creator<Appslider>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Appslider createFromParcel(Parcel in) {
            return new Appslider(in);
        }

        public Appslider[] newArray(int size) {
            return (new Appslider[size]);
        }

    };

    protected Appslider(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.slider = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
        this.updatedDate = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Appslider() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlider() {
        return slider;
    }

    public void setSlider(String slider) {
        this.slider = slider;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(slider);
        dest.writeValue(image);
        dest.writeValue(updatedDate);
    }

    public int describeContents() {
        return 0;
    }

}