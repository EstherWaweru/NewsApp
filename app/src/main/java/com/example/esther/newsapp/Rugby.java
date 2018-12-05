package com.example.esther.newsapp;

public class Rugby {
    private String newsTitle;
    private String newsSection;

    private String publicationDate;
    private String newsUrl;



    public Rugby(String newsTitle, String newsSection, String publicationDate,String newsUrl) {
        this.newsTitle = newsTitle;
        this.newsSection = newsSection;

        this.publicationDate = publicationDate;
        this.newsUrl = newsUrl;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNewsSection() {
        return newsSection;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public String getPublicationDate() {
        return publicationDate;
    }
}
