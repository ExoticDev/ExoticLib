package net.exoticdev.api.downloader;

import net.exoticdev.api.gson.GsonFactory;
import net.exoticdev.api.spigot.Spigot;
import net.exoticdev.api.util.WebConnection;
import org.bukkit.plugin.Plugin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class PluginDownloader {

    private DownloadMessages messages;
    private Plugin plugin;

    public PluginDownloader(Plugin plugin) {
        this.plugin = plugin;
    }

    public void setMessages(DownloadMessages messages) {
        this.messages = messages;
    }

    public void unsetMessages() {
        this.messages = null;
    }

    private String getResourceIDFromURL(String url) {
        return url.substring(url.lastIndexOf(".") + 1, url.length() - 1);
    }

    public void download(URL pluginURL, boolean loadAfter) {
        long startMS = System.currentTimeMillis();

        BufferedInputStream inputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            if(this.messages != null) {
                this.messages.onDownloadStart();
            }

            String resourceID = this.getResourceIDFromURL(pluginURL.toString());

            URL url = new URL("https://api.spiget.org/v2/resources/" + resourceID + "/");
            URL downloadURL = new URL("https://api.spiget.org/v2/resources/" + resourceID + "/download");

            String pluginName = "Corrupted";

            GsonFactory factory = new GsonFactory(WebConnection.getInputFromURL(url));

            if(factory.contains("name")) {
                pluginName = factory.getString("name");
            }

            HttpURLConnection downloadConnection = (HttpURLConnection) downloadURL.openConnection();

            downloadConnection.addRequestProperty("User-Agent", "ExoticDev");

            final int fileLength = downloadConnection.getContentLength();

            String libLocation = this.plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
            String folderLocation = libLocation.substring(6, libLocation.lastIndexOf("/") + 1);

            folderLocation = folderLocation.replace("%20", " ");

            String fileLocation = pluginName + ".jar";

            File pluginFile = new File(folderLocation.replace("/", ""), fileLocation);

            if(pluginFile.exists()) {
                if(this.messages != null) {
                    this.messages.onFileAlreadyExists();
                }
            }

            inputStream = new BufferedInputStream(downloadURL.openStream());
            fileOutputStream = new FileOutputStream(folderLocation + fileLocation);

            byte[] data = new byte[1024];

            int count;

            long downloaded = 0;

            while ((count = inputStream.read(data, 0, 1024)) != -1) {
                downloaded += count;

                fileOutputStream.write(data, 0, count);

                int percent = (int) ((downloaded * 100) / fileLength);

                if((percent % 10) == 0) {
                    if(this.messages != null) {
                        this.messages.onPercentPrint(Integer.toString(percent));
                    }
                }
            }

            long endMS = System.currentTimeMillis();

            if(this.messages != null) {
                this.messages.onSuccessfullDownload((endMS - startMS) / 1000.0);
            }

            fileOutputStream.flush();
            fileOutputStream.close();

            if(loadAfter) {
                Spigot.getPluginManager().enablePlugin(Spigot.getPluginManager().loadPlugin(pluginFile));
            }
        } catch (Exception e) {
            if(this.messages != null) {
                this.messages.onErrorReceive(e);
            }
        } finally {
            try {
                if(fileOutputStream != null) {
                    fileOutputStream.close();
                }

                if(inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                if(this.messages != null) {
                    this.messages.onErrorReceive(e);
                }
            }
        }
    }
}