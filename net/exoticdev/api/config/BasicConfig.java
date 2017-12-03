package net.exoticdev.api.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

public class BasicConfig {

    private JavaPlugin plugin;
    private File configFile;
    private FileConfiguration config;

    public BasicConfig(File configFile, JavaPlugin plugin) {
        this.configFile = configFile;
        this.plugin = plugin;

        this.setup();
    }

    public void saveConfig() {
        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    private void setup() {
        if(!this.configFile.exists()) {
            this.saveResource();
        }

        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    private void saveResource() {
        String name = this.configFile.getName();
        File folder = this.configFile.getParentFile();

        if((name == null) || (name.equals(""))) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        name = name.replace('\\', '/');
        InputStream in = this.getResource(name);

        if(in == null) {
            throw new IllegalArgumentException("The embedded resource '" + name + "' cannot be found in " + folder);
        }

        File outFile = new File(folder, name);

        int lastIndex = name.lastIndexOf('/');

        File outDir = new File(folder, name.substring(0, lastIndex >= 0 ? lastIndex : 0));

        if(!(outDir.exists())) {
            outDir.mkdirs();
        }

        try {
            if((!outFile.exists())) {
                OutputStream out = new FileOutputStream(outFile);

                byte[] buf = new byte['?'];

                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                out.close();
                in.close();
            } else {
                this.plugin.getLogger().log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, ex);
        }
    }

    private InputStream getResource(String filename) {
        if(filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }

        try {
            URL url = this.plugin.getClass().getClassLoader().getResource(filename);

            if(url == null) {
                return null;
            }

            URLConnection connection = url.openConnection();

            connection.setUseCaches(false);

            return connection.getInputStream();
        } catch (IOException localIOException) {
        }

        return null;
    }
}