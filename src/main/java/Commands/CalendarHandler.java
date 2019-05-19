package Commands;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class CalendarHandler {

    private SimpleDateFormat storesdf;

    public CalendarHandler() {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("src/main/resources/Dates.json")) {
            jsonObject = (JSONObject) parser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        storesdf = new SimpleDateFormat("dd/MM/yyyy");

    }

    private JSONObject jsonObject;

    public void addSession(Date date) {
        JSONArray jsonArray = ((JSONArray) jsonObject.get("Dates"));
        jsonArray.add(storesdf.format(date));
        save();
    }

    public void removeSession(Date date) {
        JSONArray jsonArray = ((JSONArray) jsonObject.get("Dates"));
        jsonArray.remove(storesdf.format(date));
        save();
    }

    public String removeSession(int i) {
        JSONArray jsonArray = ((JSONArray) jsonObject.get("Dates"));
        String s = (String) jsonArray.get(i);
        jsonArray.remove(i);
        save();
        return s;
    }

    public ArrayList<Date> getSessions() {
        JSONArray jsonArray = ((JSONArray) jsonObject.get("Dates"));
        ArrayList<Date> dates = new ArrayList<>();
        for (Object date : jsonArray.toArray()) {
            try {
                String d = (String) date;
                System.out.println(d);
                dates.add(storesdf.parse(d));
            } catch (java.text.ParseException exc) {
                exc.printStackTrace();
            }
        }
        System.out.println(dates);
        Collections.sort(dates);
        return dates;
    }

    public void save() {
        try (FileWriter file = new FileWriter("src/main/resources/Dates.json")) {
            file.write(jsonObject.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
