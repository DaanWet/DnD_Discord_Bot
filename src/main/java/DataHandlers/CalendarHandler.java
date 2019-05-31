package DataHandlers;

import net.dv8tion.jda.core.entities.Guild;
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

public class CalendarHandler extends DataHandler{

    private SimpleDateFormat storesdf;
    private JSONArray dates;

    public CalendarHandler(Guild g){
        super(g);
        storesdf = new SimpleDateFormat("dd/MM/yyyy");
        dates = (JSONArray)((JSONObject) jsonObject.get(guild)).get("Dates");
    }


    @SuppressWarnings("unchecked")
    public void addSession(Date date) {
        dates.add(storesdf.format(date));
        save();
    }

    public void removeSession(Date date) {
        dates.remove(storesdf.format(date));
        save();
    }

    public ArrayList<Date> getSessions(boolean allsessions) {
        ArrayList<Date> dates = new ArrayList<>();
        for (Object date : dates.toArray()) {
            try {
                String d = (String) date;
                Date da = storesdf.parse(d);
                Date today = new Date();
                if (allsessions || da.after(new Date(today.getTime() - (1000 * 60 * 60 * 24)))) {
                    dates.add(da);
                }
            } catch (java.text.ParseException exc) {
                exc.printStackTrace();
            }
        }
        Collections.sort(dates);
        return dates;
    }
}
