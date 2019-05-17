
package ibt.pahadisabzi.model.delivary_time_responce;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DelivaryTimeModel implements Parcelable
{

    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("deliverytiming")
    @Expose
    private List<Deliverytiming> deliverytiming = new ArrayList<Deliverytiming>();
    public final static Creator<DelivaryTimeModel> CREATOR = new Creator<DelivaryTimeModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DelivaryTimeModel createFromParcel(Parcel in) {
            return new DelivaryTimeModel(in);
        }

        public DelivaryTimeModel[] newArray(int size) {
            return (new DelivaryTimeModel[size]);
        }

    }
    ;

    protected DelivaryTimeModel(Parcel in) {
        this.result = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.deliverytiming, (ibt.pahadisabzi.model.delivary_time_responce.Deliverytiming.class.getClassLoader()));
    }

    public DelivaryTimeModel() {
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public DelivaryTimeModel withResult(Boolean result) {
        this.result = result;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DelivaryTimeModel withMessage(String message) {
        this.message = message;
        return this;
    }

    public List<Deliverytiming> getDeliverytiming() {
        return deliverytiming;
    }

    public void setDeliverytiming(List<Deliverytiming> deliverytiming) {
        this.deliverytiming = deliverytiming;
    }

    public DelivaryTimeModel withDeliverytiming(List<Deliverytiming> deliverytiming) {
        this.deliverytiming = deliverytiming;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(result);
        dest.writeValue(message);
        dest.writeList(deliverytiming);
    }

    public int describeContents() {
        return  0;
    }

}
