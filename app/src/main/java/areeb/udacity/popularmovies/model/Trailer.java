package areeb.udacity.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Trailer {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("site")
    @Expose
    private String site;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

}