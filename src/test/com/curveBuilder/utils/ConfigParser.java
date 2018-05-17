package com.curveBuilder.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.Properties;


public class ConfigParser {

    private Properties prop;

    public ConfigParser() {
        InputStream input = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            input = new FileInputStream(classLoader.getResource("config.properties").getFile());
            this.prop = new Properties();
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Properties getProperties() {
        return prop;
    }

    void populateConfigs() {
        try {
            // input the file content to the StringBuffer "input"
            BufferedReader file = new BufferedReader(new FileReader(prop.getProperty("curvebuilder.atom_config")));
            String line;
            StringBuilder inputBuffer = new StringBuilder();

            while ((line = file.readLine()) != null) {
                inputBuffer.append(line
                        .replaceAll("(configSettings.+&&.+WsDnsPrefix)", prop.getProperty("websocket_prefix"))
                        .replaceAll("(configSettings.+&&.+PrivateKey)", prop.getProperty("api_key"))
                        .replaceAll("(configSettings.+&&.+RsDnsPrefix)", prop.getProperty("rest_prefix"))
                        .replaceFirst("TD\\.atom\\.on", "//" + line))
                        .append("\n");
            }
            String inputStr = inputBuffer.toString();

            FileOutputStream fileOut = new FileOutputStream(prop.getProperty("curvebuilder.atom_config"));
            fileOut.write(inputStr.getBytes());
            fileOut.close();

        } catch (Exception e) {
            System.out.println("Problem reading file.");
        }
    }

    void configureSession() {

        String script =
                "<script>\n" +
                        "    const config = {\n" +
                        "        WsDnsPrefix: \"ws://127.0.0.1:55555\", \n" +
                        "        RsDnsPrefix: \"http://localhost:33333\", \n" +
                        "        //PrivateKey: \"RZgEHRAtdUyNryuM2IFqIw2\", \n" +
                        "        AtomConfiguration: {AtomUsername: ''}\n" +
                        "    };\n" +
                        "    \n" +
                        "    var atom = {\n" +
                        "        sendMessage: (name, message, callback) => {\n" +
                        "            callback(null, {Topic: JSON.stringify(config)})\n" +
                        "        },\n" +
                        "        messageFactory: {register: () => {}},\n" +
                        "    }\n" +
                        "\n" +
                        "    var TD = {atom: {on: console.log, replayProviderStates : console.log}};\n" +
                        "\n" +
                        "\t</script>\n";

        try {
            BufferedReader file = new BufferedReader(new FileReader(prop.getProperty("plugin.html")));
            Boolean endOfFileNotReached = true;
            String line;

            while (endOfFileNotReached) {
                line = file.readLine();
                if (line != null && line.contains("<script>")) {
                    break;
                } else if (line == null) {
                    Document pluginHtml = Jsoup.parse(new File(prop.getProperty("plugin.html")), "UTF-8", "");
                    pluginHtml.select("head").append(script);

                    FileOutputStream fileOut = new FileOutputStream(prop.getProperty("plugin.html"));
                    fileOut.write(pluginHtml.toString().getBytes());
                    fileOut.close();
                    endOfFileNotReached = false;
                }
            }

        } catch (Exception e) {
            System.out.println("Problem reading file.");
        }
    }
}
