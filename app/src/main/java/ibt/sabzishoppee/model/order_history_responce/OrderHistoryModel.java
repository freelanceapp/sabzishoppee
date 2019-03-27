
package ibt.sabzishoppee.model.order_history_responce;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderHistoryModel implements Parcelable
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
    public final static Creator<OrderHistoryModel> CREATOR = new Creator<OrderHistoryModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public OrderHistoryModel createFromParcel(Parcel in) {
            return new OrderHistoryModel(in);
        }

        public OrderHistoryModel[] newArray(int size) {
            return (new OrderHistoryModel[size]);
        }

    }
    ;

    protected OrderHistoryModel(Parcel in) {
        this.error = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.order, (Order.class.getClassLoader()));
    }

    public OrderHistoryModel() {
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public OrderHistoryModel withError(Boolean error) {
        this.error = error;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OrderHistoryModel withMessage(String message) {
        this.message = message;
        return this;
    }

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }

    public OrderHistoryModel withOrder(List<Order> order) {
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
