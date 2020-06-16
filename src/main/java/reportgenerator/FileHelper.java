package reportgenerator;



import lombok.Getter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import reportgenerator.settings.Column;
import reportgenerator.settings.Page;
import reportgenerator.settings.Settings;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {

    @Getter
    private File fileSettings;
    @Getter
    private File fileSource;
    @Getter
    private File fileReport;
    @Getter
    private List<String> inputData;

    private Settings settings;

    public Settings getSettings() {
        return settings;
    }

    public FileHelper(String settings, String source, String report) throws FileNotFoundException, NullPointerException {
        if (settings == null && source == null && report == null)
            throw new NullPointerException("Отсутствует путь до одного из файлов");
        this.fileSettings = new File(settings);
        this.fileSource = new File(source);
        this.fileReport = new File(report);

        if (!this.fileSettings.exists() && !this.fileSource.exists() && !this.fileReport.exists())
            throw new FileNotFoundException("Файл не существует");
        this.settings = new Settings();
        inputData = new ArrayList<>();
        installSettings();
        recordData();
    }

    private void recordData() {

        try (BufferedReader reader = new BufferedReader(new FileReader(fileSource, StandardCharsets.UTF_16))){
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                inputData.add(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void installSettings(){
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            XMLHandler handler = new XMLHandler();
            parser.parse(fileSettings, handler);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recordDataInFile(List<String> result) {

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileReport))){
            for (String s : result)
                writer.write(s + "\n");

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class XMLHandler extends DefaultHandler {

        String elementValue;
        boolean pageOrColumn = false;
        int countColumnInSettingsList = 0;

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            elementValue = new String(ch, start, length);
        }

        @Override
        public void startElement(String uri, String lName, String qName, Attributes attr) throws SAXException {
            switch (qName) {
                case "page":
                    pageOrColumn = true;
                    break;
                case "column":
                    pageOrColumn = false;
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (pageOrColumn){

                switch (qName) {
                    case "width":
                        settings.getPage().setWidth(Integer.parseInt(elementValue));
                        break;
                    case "height":
                        settings.getPage().setHeight(Integer.parseInt(elementValue));
                        break;
                }
            }else {
                switch (qName) {
                    case "title":
                        Column column = new Column();
                        column.setTitle(elementValue);
                        settings.getColumns().add(column);
                        break;
                    case "width":
                        settings.getColumns().get(countColumnInSettingsList++).setWidth(Integer.parseInt(elementValue));
                        break;
                }

            }

        }
    }


}
