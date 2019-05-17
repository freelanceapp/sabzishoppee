
package ibt.pahadisabzi.model.delivary_time_responce;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Deliverytiming implements Parcelable
{

    @SerializedName("delivery_timing_id")
    @Expose
    private String deliveryTimingId;
    @SerializedName("delivery_timing_title")
    @Expose
    private String deliveryTimingTitle;
    @SerializedName("delivery_timing_start_time")
    @Expose
    private String deliveryTimingStartTime;
    @SerializedName("delivery_timing_end_time")
    @Expose
    private String deliveryTimingEndTime;
    @SerializedName("delivery_timing_created_date")
    @Expose
    private String deliveryTimingCreatedDate;
    public final static Creator<Deliverytiming> CREATOR = new Creator<Deliverytiming>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Deliverytiming createFromParcel(Parcel in) {
            return new Deliverytiming(in);
        }

        public Deliverytiming[] newArray(int size) {
            return (new Deliverytiming[size]);
        }

    }
    ;

    protected Deliverytiming(Parcel in) {
        this.deliveryTimingId = ((String) in.readValue((String.class.getClassLoader())));
        this.deliveryTimingTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.deliveryTimingStartTime = ((String) in.readValue((String.class.getClassLoader())));
        this.deliveryTimingEndTime = ((String) in.readValue((String.class.getClassLoader())));
        this.deliveryTimingCreatedDate = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Deliverytiming() {
    }

    public String getDeliveryTimingId() {
        return deliveryTimingId;
    }

    public void setDeliveryTimingId(String deliveryTimingId) {
        this.deliveryTimingId = deliveryTimingId;
    }

    public Deliverytiming withDeliveryTimingId(String deliveryTimingId) {
        this.deliveryTimingId = deliveryTimingId;
        return this;
    }

    public String getDeliveryTimingTitle() {
        return deliveryTimingTitle;
    }

    public void setDeliveryTimingTitle(String deliveryTimingTitle) {
        this.deliveryTimingTitle = deliveryTimingTitle;
    }

    public Deliverytiming withDeliveryTimingTitle(String deliveryTimingTitle) {
        this.deliveryTimingTitle = deliveryTimingTitle;
        return this;
    }

    public String getDeliveryTimingStartTime() {
        return deliveryTimingStartTime;
    }

    public void setDeliveryTimingStartTime(String deliveryTimingStartTime) {
        this.deliveryTimingStartTime = deliveryTimingStartTime;
    }

    public Deliverytiming withDeliveryTimingStartTime(String deliveryTimingStartTime) {
        this.deliveryTimingStartTime = deliveryTimingStartTime;
        return this;
    }

    public String getDeliveryTimingEndTime() {
        return deliveryTimingEndTime;
    }

    public void setDeliveryTimingEndTime(String deliveryTimingEndTime) {
        this.deliveryTimingEndTime = deliveryTimingEndTime;
    }

    public Deliverytiming withDeliveryTimingEndTime(String deliveryTimingEndTime) {
        this.deliveryTimingEndTime = deliveryTimingEndTime;
        return this;
    }

    public String getDeliveryTimingCreatedDate() {
        return deliveryTimingCreatedDate;
    }

    public void setDeliveryTimingCreatedDate(String deliveryTimingCreatedDate) {
        this.deliveryTimingCreatedDate = deliveryTimingCreatedDate;
    }

    public Deliverytiming withDeliveryTimingCreatedDate(String deliveryTimingCreatedDate) {
        this.deliveryTimingCreatedDate = deliveryTimingCreatedDate;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(deliveryTimingId);
        dest.writeValue(deliveryTimingTitle);
        dest.writeValue(deliveryTimingStartTime);
        dest.writeValue(deliveryTimingEndTime);
        dest.writeValue(deliveryTimingCreatedDate);
    }

    public int describeContents() {
        return  0;
    }

}
