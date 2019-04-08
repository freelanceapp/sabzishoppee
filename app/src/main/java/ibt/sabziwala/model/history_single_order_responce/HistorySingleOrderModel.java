
package ibt.sabziwala.model.history_single_order_responce;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistorySingleOrderModel implements Parcelable
{

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Order")
    @Expose
    private List<Order> order = new ArrayList<Order>();
    public final static Creator<HistorySingleOrderModel> CREATOR = new Creator<HistorySingleOrderModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public HistorySingleOrderModel createFromParcel(Parcel in) {
            return new HistorySingleOrderModel(in);
        }

        public HistorySingleOrderModel[] newArray(int size) {
            return (new HistorySingleOrderModel[size]);
        }

    }
    ;

    protected HistorySingleOrderModel(Parcel in) {
        this.error = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.order, (ibt.sabziwala.model.history_single_order_responce.Order.class.getClassLoader()));
    }

    public HistorySingleOrderModel() {
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public HistorySingleOrderModel withError(Boolean error) {
        this.error = error;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HistorySingleOrderModel withMessage(String message) {
        this.message = message;
        return this;
    }

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }

    public HistorySingleOrderModel withOrder(List<Order> order) {
        this.order = order;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(error);
        dest.writeValue(message);
        dest.writeList(order);
    }

    public int describeContents() {
        return  0;
    }

}
