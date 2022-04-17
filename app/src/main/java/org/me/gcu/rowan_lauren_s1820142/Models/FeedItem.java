package org.me.gcu.rowan_lauren_s1820142.Models;

import java.util.Date;

//Lauren Rowan S1820142

public class FeedItem { //used for both incidents and roadworks as they have the same properties
    private String title;
    private String description;
    private float latitude;
    private float longitude;
    private Date publishDate;

    private Date startDate;
    private Date endDate;

    private String type; //"INCIDENT", "CURRENT_ROADWORKS", "PLANNED_ROADWORKS"

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public FeedItem(String title, String description, float latitude, float longitude, Date publishDate, Date startDate, Date endDate, String type) {
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.publishDate = publishDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    @Override
    public String toString() {
        return "-- " + title + " --" + '\n' +
                "> title: " + title + '\n' +
                "> description: " + description + '\n' +
                "> latitude: " + latitude + '\n' +
                "> longitude: " + longitude + '\n' +
                "> publishDate: " + publishDate + '\n' +
                "> startDate: " + startDate + '\n' +
                "> endDate: " + endDate + '\n' +
                "> type: " + type + '\n' + '\n';
    }
}
