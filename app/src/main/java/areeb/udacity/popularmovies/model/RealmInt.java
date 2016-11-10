package areeb.udacity.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import io.realm.RealmObject;

import java.io.Serializable;

public class RealmInt extends RealmObject implements Parcelable {
    public int val;

    public RealmInt() {
    }

    public RealmInt(int val) {
        this.val = val;
    }

    protected RealmInt(Parcel in) {
        val = in.readInt();
    }

    public static final Creator<RealmInt> CREATOR = new Creator<RealmInt>() {
        @Override
        public RealmInt createFromParcel(Parcel in) {
            return new RealmInt(in);
        }

        @Override
        public RealmInt[] newArray(int size) {
            return new RealmInt[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(val);
    }

    // Getters and setters
}