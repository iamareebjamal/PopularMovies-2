package areeb.udacity.popularmovies.api;

import java.io.Serializable;

public enum Sort implements Serializable {
    // Type safe Sorting Modes

    POPULAR("popular"),
    TOP_RATED("top_rated");

    private String value;

    Sort(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public boolean equals(Sort sort) {
        return this.value.equals(sort.value);
    }
}
