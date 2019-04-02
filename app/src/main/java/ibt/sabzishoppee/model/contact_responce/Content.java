
package ibt.sabzishoppee.model.contact_responce;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Content implements Parcelable
{

    @SerializedName("content_id")
    @Expose
    private String contentId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("content_type")
    @Expose
    private String contentType;
    public final static Creator<Content> CREATOR = new Creator<Content>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Content createFromParcel(Parcel in) {
            return new Content(in);
        }

        public Content[] newArray(int size) {
            return (new Content[size]);
        }

    }
    ;

    protected Content(Parcel in) {
        this.contentId = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.content = ((String) in.readValue((String.class.getClassLoader())));
        this.contentType = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Content() {
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public Content withContentId(String contentId) {
        this.contentId = contentId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Content withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Content withContent(String content) {
        this.content = content;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Content withContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(contentId);
        dest.writeValue(title);
        dest.writeValue(content);
        dest.writeValue(contentType);
    }

    public int describeContents() {
        return  0;
    }

}
