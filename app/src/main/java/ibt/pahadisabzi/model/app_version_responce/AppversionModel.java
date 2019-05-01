package ibt.pahadisabzi.model.app_version_responce;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppversionModel implements Parcelable
{

    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("version_url")
    @Expose
    private String versionUrl;
    public final static Parcelable.Creator<AppversionModel> CREATOR = new Creator<AppversionModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public AppversionModel createFromParcel(Parcel in) {
            return new AppversionModel(in);
        }

        public AppversionModel[] newArray(int size) {
            return (new AppversionModel[size]);
        }

    }
            ;

    protected AppversionModel(Parcel in) {
        this.version = ((String) in.readValue((String.class.getClassLoader())));
        this.versionUrl = ((String) in.readValue((String.class.getClassLoader())));
    }

    public AppversionModel() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public AppversionModel withVersion(String version) {
        this.version = version;
        return this;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public AppversionModel withVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(version);
        dest.writeValue(versionUrl);
    }

    public int describeContents() {
        return 0;
    }

}
