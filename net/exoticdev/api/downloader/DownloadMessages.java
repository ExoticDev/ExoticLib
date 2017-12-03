package net.exoticdev.api.downloader;

public interface DownloadMessages {

    void onDownloadStart();

    void onFileAlreadyExists();

    void onPercentPrint(String percentage);

    void onErrorReceive(Exception e);

    void onSuccessfullDownload(double processTime);

}