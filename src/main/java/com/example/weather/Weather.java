package com.example.weather;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Weather {
    static ArrayList<String> row = new ArrayList<>();
    static ArrayList<Float> mm = new ArrayList<>();
    static ArrayList<Integer> temp = new ArrayList<>();

    static HashMap<String, String> city = new HashMap<>();

    private static String url = "https://meteoinfo.ru/forecasts/russia/sverdlovsk-area/ekaterinburg";

    @FXML
    private ComboBox<String> comboCity;
    @FXML
    private AnchorPane panel = new AnchorPane();
    @FXML
    private TextArea textArea = new TextArea();
    @FXML
    private Label label = new Label();
    @FXML
    private ImageView imgView;



    public Weather() {
        city.put("Екатеринбург", "https://meteoinfo.ru/forecasts/russia/sverdlovsk-area/ekaterinburg");
        city.put("Каменск-Уральский", "https://meteoinfo.ru/forecasts/russia/sverdlovsk-area/kamensk-uralsk");
        city.put("Североуральск", "https://meteoinfo.ru/forecasts/russia/sverdlovsk-area/severouralsk");
        city.put("Ростов-на-Дону", "https://meteoinfo.ru/forecasts/russia/rostov-area/rostov-na-donu");
        city.put("Казань", "https://meteoinfo.ru/forecasts/russia/republic-tatarstan/kasan");
        city.put("Ханты-Мансийск", "https://meteoinfo.ru/forecasts/russia/hanty-mansijskij-ar/hanty-mansijsk");
        city.put("Сочи", "https://meteoinfo.ru/forecasts/russia/krasnodar-territory/adler");
        city.put("Санкт-Петербург", "https://meteoinfo.ru/forecasts/russia/leningrad-region/sankt-peterburg");
    }

    private static Document getPage() throws IOException {
        String mainURL = getURL();
        Document page = (Document) Jsoup.parse(new URL(mainURL), 3000);
        return page;
    }

    public void initialize() throws IOException {
        box();
        changeCity();
    }

    public void changeCity() throws IOException {
        Document page = getPage();
        Element tab = page.select("table").get(3);
        Elements val = tab.select("tr");

        for (int i = 2; i < 28; i += 2) {
            String tabStr = val.get(i).text();
            int degree = tabStr.indexOf("°");
            int start = tabStr.indexOf("ь");
            row.add(tabStr.substring(0, degree + 1));
            mm.add(Float.parseFloat(tabStr.substring(degree + 2, tabStr.length() - 6)));
            temp.add(Integer.parseInt(tabStr.substring(start + 2, degree)));
        }
        gui();
    }

    public void gui() {
        textArea.setEditable(false);
        for (int i = 1; i < 13; i++) {
            textArea.appendText(row.get(i) + "\n");
        }

        if (mm.get(0) == 0.0) {
            setFile("src/cloud.jpg");
        }
        if (temp.get(0) > 3 && mm.get(0) > 0) {
            setFile("src/rain.png");
        }
        if (temp.get(0) < -3 && mm.get(0) > 0) {
            setFile("src/snow.png");
        }
        if (temp.get(0) >= -3 && temp.get(0) <= 3 && mm.get(0) > 0) {
            setFile("src/rainSnow.png");
        }

        label.setText(String.valueOf(row.get(0)));
    }

    public void setFile(String f){
        File file = new File(f);
        Image image = new Image(file.toURI().toString());
        imgView.setImage(image);
    }

    public static String getURL() {
        return url;
    }

    public void setURL(String urlVal) {
        url = urlVal;
    }

    public void box() {
        comboCity.getItems().addAll("Екатеринбург", "Санкт-Петербург", "Каменск-Уральский", "Североуральск", "Казань", "Ростов-на-Дону", "Сочи", "Ханты-Мансийск");
        comboCity.setValue("Екатеринбург");
        comboCity.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mm.clear();
                temp.clear();
                row.clear();
                textArea.clear();
                setURL(city.get(comboCity.getValue()));
                try {
                    changeCity();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}



