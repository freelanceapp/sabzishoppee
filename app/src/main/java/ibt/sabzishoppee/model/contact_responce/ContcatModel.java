
package ibt.sabzishoppee.model.contact_responce;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContcatModel implements Parcelable
{

    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("content")
    @Expose
    private List<Content> content = new ArrayList<Content>();
    public final static Creator<ContcatModel> CREATOR = new Creator<ContcatModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ContcatModel createFromParcel(Parcel in) {
            return new ContcatModel(in);
        }

        public ContcatModel[] newArray(int size) {
            return (new ContcatModel[size]);
        }

    }
    ;

    protected ContcatModel(Parcel in) {
        this.result = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.content, (ibt.sabzishoppee.model.contact_responce.Content.class.getClassLoader()));
    }

    public ContcatModel() {
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public ContcatModel withResult(Boolean result) {
        this.result = result;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ContcatModel withMessage(String message) {
        this.message = message;
        return this;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public ContcatModel withContent(List<Content> content) {
        this.content = content;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(result);
        dest.writeValue(message);
        dest.writeList(content);
    }

    public int describeContents() {
        return  0;
    }

}
