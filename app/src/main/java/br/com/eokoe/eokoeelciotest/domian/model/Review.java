package br.com.eokoe.eokoeelciotest.domian.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Review implements Serializable {
    @SerializedName("results")
    @Expose
    private List<ReviewResults> results = null;

    public List<ReviewResults> getResults() {
        return results;
    }

    public void setResults(List<ReviewResults> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "Review{" +
                "results=" + results +
                '}';
    }
}
