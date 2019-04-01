package org.josmer.application.interfaces;

public interface ICrawler {
    void download(String date) throws Exception;

    void unzip() throws Exception;

    void insert() throws Exception;

    void delete() throws Exception;
}
