package com.example.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Result implements Serializable {
    @SerializedName("geometry")
    private Geometry geometry;

    @SerializedName("icon")
    private String icon;

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("opening_hours")
    private OpeningHours openingHours;

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("rating")
    private Double rating;

    @SerializedName("user_ratings_total")
    private Integer user_rating;


    @SerializedName("reference")
    private String reference;

    @SerializedName("scope")
    private String scope;

    @SerializedName("types")
    private List<String> types = new ArrayList<String>();

    @SerializedName("vicinity")
    private String vicinity;

    @SerializedName("price_level")
    private Integer priceLevel;

    /**
     * @return The geometry
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * @param geometry The geometry
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    /**
     * @return The icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon The icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The openingHours
     */
    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    /**
     * @param openingHours The opening_hours
     */
    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    /**
     * @return The photos
     */

    /**
     * @return The placeId
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     * @param placeId The place_id
     */
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     * @return The rating
     */
    public Double getRating() {
        return rating;
    }

    /**
     * @param rating The rating
     */
    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getUser_rating()
    {
        return user_rating;
    }
    public void setUser_rating(Integer user_rating)
    {
        this.user_rating=user_rating;
    }
    /**
     * @return The reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * @param reference The reference
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * @return The scope
     */
    public String getScope() {
        return scope;
    }

    /**
     * @param scope The scope
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * @return The types
     */
    public List<String> getTypes() {
        return types;
    }

    /**
     * @param types The types
     */
    public void setTypes(List<String> types) {
        this.types = types;
    }

    /**
     * @return The vicinity
     */
    public String getVicinity() {
        return vicinity;
    }

    /**
     * @param vicinity The vicinity
     */
    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    /**
     * @return The priceLevel
     */
    public Integer getPriceLevel() {
        return priceLevel;
    }

    /**
     * @param priceLevel The price_level
     */
    public void setPriceLevel(Integer priceLevel) {
        this.priceLevel = priceLevel;
    }
}

