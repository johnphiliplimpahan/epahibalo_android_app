package com.app.epahibalo.e_pahibalo.ItemAdapters.ItemModels;

public class FeedItem {

    private String disaster_id,citizenPic,name,headline,date_posted,disaster_type,location,incidentImage,validated;

    public FeedItem(String disaster_id, String citizenPic, String name, String headline, String date_posted, String disaster_type, String location, String incidentImage, String validated) {
        this.disaster_id = disaster_id;
        this.citizenPic = citizenPic;
        this.name = name;
        this.headline = headline;
        this.date_posted = date_posted;
        this.disaster_type = disaster_type;
        this.location = location;
        this.incidentImage = incidentImage;
        this.validated = validated;
    }

    public String getDisaster_id() {
        return disaster_id;
    }

    public void setDisaster_id(String disaster_id) {
        this.disaster_id = disaster_id;
    }

    public String getCitizenPic() {
        return citizenPic;
    }

    public void setCitizenPic(String citizenPic) {
        this.citizenPic = citizenPic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDate_posted() {
        return date_posted;
    }

    public void setDate_posted(String date_posted) {
        this.date_posted = date_posted;
    }

    public String getDisaster_type() {
        return disaster_type;
    }

    public void setDisaster_type(String disaster_type) {
        this.disaster_type = disaster_type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIncidentImage() {
        return incidentImage;
    }

    public void setIncidentImage(String incidentImage) {
        this.incidentImage = incidentImage;
    }

    public String getValidated() {
        return validated;
    }

    public void setValidated(String validated) {
        this.validated = validated;
    }
}
