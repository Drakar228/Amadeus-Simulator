package com.amadeus.simulator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private JdbcTemplate jdbc;
    @Autowired private ObjectMapper mapper;

    @Override
    public void run(String... args) throws Exception {
        seedAirlines();
        seedCities();
        seedAircraft();
        seedSrCodes();
        seedFlights();
        seedFares();
        seedPnrs();
        System.out.println("  [Amadeus] Database ready.");
    }

    // ── Airlines ─────────────────────────────────────────────────────────────
    private void seedAirlines() {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM airlines", Integer.class);
        if (count != null && count > 0) return;
        String[][] data = {
            {"MS","EgyptAir"},{"EK","Emirates"},{"EY","Etihad Airways"},{"QR","Qatar Airways"},
            {"SV","Saudia"},{"WY","Oman Air"},{"GF","Gulf Air"},{"FZ","flydubai"},
            {"G9","Air Arabia"},{"XY","flynas"},{"BA","British Airways"},{"LH","Lufthansa"},
            {"AF","Air France"},{"KL","KLM"},{"TK","Turkish Airlines"},{"RJ","Royal Jordanian"},
            {"ME","Middle East Airlines"},{"ET","Ethiopian Airlines"},{"KE","Korean Air"},
            {"SQ","Singapore Airlines"},{"CX","Cathay Pacific"},{"AI","Air India"},
            {"UA","United Airlines"},{"AA","American Airlines"},{"DL","Delta Air Lines"},
            {"LX","SWISS"},{"OS","Austrian Airlines"},{"IB","Iberia"},{"AZ","ITA Airways"},
            {"SK","Scandinavian Airlines"},{"AY","Finnair"},{"U2","easyJet"},
            {"FR","Ryanair"},{"PC","Pegasus"},{"SA","South African Airways"},{"VY","Vueling"}
        };
        for (String[] row : data)
            jdbc.update("INSERT IGNORE INTO airlines (code,name) VALUES (?,?)", row[0], row[1]);
    }

    // ── Cities ────────────────────────────────────────────────────────────────
    private void seedCities() {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM cities", Integer.class);
        if (count != null && count > 0) return;
        String[][] data = {
            {"CAI","Cairo International","Egypt"},
            {"HRG","Hurghada International","Egypt"},
            {"SSH","Sharm El Sheikh","Egypt"},
            {"LXR","Luxor International","Egypt"},
            {"ASW","Aswan International","Egypt"},
            {"DXB","Dubai International","UAE"},
            {"AUH","Abu Dhabi International","UAE"},
            {"SHJ","Sharjah International","UAE"},
            {"DOH","Hamad International","Qatar"},
            {"RUH","King Khalid International","Saudi Arabia"},
            {"JED","King Abdulaziz International","Saudi Arabia"},
            {"MCT","Muscat International","Oman"},
            {"KWI","Kuwait International","Kuwait"},
            {"BAH","Bahrain International","Bahrain"},
            {"AMM","Queen Alia International","Jordan"},
            {"BEY","Beirut Rafic Hariri","Lebanon"},
            {"LHR","London Heathrow","United Kingdom"},
            {"LGW","London Gatwick","United Kingdom"},
            {"CDG","Paris Charles de Gaulle","France"},
            {"FRA","Frankfurt","Germany"},
            {"MUC","Munich","Germany"},
            {"AMS","Amsterdam Schiphol","Netherlands"},
            {"IST","Istanbul Airport","Turkey"},
            {"FCO","Rome Fiumicino","Italy"},
            {"MAD","Madrid Barajas","Spain"},
            {"ZRH","Zurich","Switzerland"},
            {"JFK","New York JFK","USA"},
            {"LAX","Los Angeles","USA"},
            {"ORD","Chicago O Hare","USA"},
            {"SIN","Singapore Changi","Singapore"},
            {"BKK","Bangkok Suvarnabhumi","Thailand"},
            {"HKG","Hong Kong","Hong Kong"},
            {"ICN","Seoul Incheon","South Korea"},
            {"NRT","Tokyo Narita","Japan"},
            {"DEL","New Delhi Indira Gandhi","India"},
            {"BOM","Mumbai Chhatrapati Shivaji","India"},
            {"JNB","Johannesburg O.R. Tambo","South Africa"},
            {"NBO","Nairobi Jomo Kenyatta","Kenya"},
            {"ADD","Addis Ababa Bole","Ethiopia"},
            {"CMN","Casablanca Mohammed V","Morocco"},
            {"TUN","Tunis Carthage","Tunisia"},
            {"ALG","Algiers Houari Boumediene","Algeria"}
        };
        for (String[] row : data)
            jdbc.update("INSERT IGNORE INTO cities (code,name,country) VALUES (?,?,?)", row[0], row[1], row[2]);
    }

    // ── Aircraft ──────────────────────────────────────────────────────────────
    private void seedAircraft() {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM aircraft_types", Integer.class);
        if (count != null && count > 0) return;
        String[][] data = {
            {"738","Boeing 737-800"},{"73H","Boeing 737-800 Winglets"},{"73W","Boeing 737-700"},
            {"788","Boeing 787-8 Dreamliner"},{"789","Boeing 787-9 Dreamliner"},
            {"77W","Boeing 777-300ER"},{"772","Boeing 777-200"},
            {"388","Airbus A380-800"},{"359","Airbus A350-900"},
            {"333","Airbus A330-300"},{"332","Airbus A330-200"},
            {"320","Airbus A320"},{"321","Airbus A321"},{"319","Airbus A319"},
            {"32N","Airbus A320neo"},{"32Q","Airbus A321neo"},
            {"773","Boeing 777-300"},{"AT7","ATR 72"},{"E90","Embraer E190"}
        };
        for (String[] row : data)
            jdbc.update("INSERT IGNORE INTO aircraft_types (code,name) VALUES (?,?)", row[0], row[1]);
    }

    // ── SR Codes ──────────────────────────────────────────────────────────────
    private void seedSrCodes() {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM sr_codes", Integer.class);
        if (count != null && count > 0) return;
        String[][] data = {
            {"WCHR","Wheelchair - Ramp"},{"WCHC","Wheelchair - Cabin"},{"WCHS","Wheelchair - Steps"},
            {"BLND","Blind Passenger"},{"DEAF","Deaf Passenger"},{"DPNA","Disabled - Needs Assistance"},
            {"MEDA","Medical Case"},{"OXYG","Oxygen Required"},{"STCR","Stretcher Required"},
            {"UMNR","Unaccompanied Minor"},{"CHML","Child Meal"},{"VGML","Vegetarian Meal"},
            {"KSML","Kosher Meal"},{"MOML","Moslem Meal"},{"HNML","Hindu Meal"},
            {"DBML","Diabetic Meal"},{"GFML","Gluten Free Meal"},{"AVML","Asian Vegetarian Meal"},
            {"SFML","Seafood Meal"},{"FPML","Fruit Platter"},{"SPML","Special Meal"},
            {"BULK","Bulky Baggage"},{"PETC","Pet in Cabin"},{"AVIH","Animal in Hold"},
            {"BIKE","Bicycle"},{"GOLF","Golf Equipment"},{"SPEQ","Sports Equipment"},
            {"FOID","Form of ID Required"},{"EXST","Extra Seat"}
        };
        for (String[] row : data)
            jdbc.update("INSERT IGNORE INTO sr_codes (code,description) VALUES (?,?)", row[0], row[1]);
    }

    // ── Flights ───────────────────────────────────────────────────────────────
    private void seedFlights() {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM flights", Integer.class);
        if (count != null && count > 0) return;

        // CAI → DXB
        insertFlight("CAI","DXB","MS","0601","0200","0700","3:00","773",
            new String[][]{{"F","4"},{"J","7"},{"C","9"},{"Y","9"},{"M","9"},{"K","6"},{"L","3"}});
        insertFlight("CAI","DXB","MS","0603","1400","1900","3:00","738",
            new String[][]{{"J","5"},{"Y","9"},{"M","9"},{"K","7"},{"L","4"}});
        insertFlight("CAI","DXB","EK","0926","0615","1115","3:00","77W",
            new String[][]{{"F","3"},{"J","6"},{"Y","9"},{"M","9"},{"K","5"}});
        insertFlight("CAI","DXB","EK","0928","2130","0230","3:00","388",
            new String[][]{{"F","6"},{"J","9"},{"Y","9"},{"M","9"},{"K","8"},{"L","4"}});
        insertFlight("CAI","DXB","FZ","1702","0830","1330","3:00","73H",
            new String[][]{{"Y","9"},{"M","8"},{"K","5"},{"L","2"}});

        // DXB → CAI
        insertFlight("DXB","CAI","MS","0602","0830","1130","3:00","773",
            new String[][]{{"J","4"},{"Y","9"},{"M","7"},{"K","4"}});
        insertFlight("DXB","CAI","MS","0604","2000","2300","3:00","738",
            new String[][]{{"Y","9"},{"M","9"},{"K","6"}});
        insertFlight("DXB","CAI","EK","0925","0800","1100","3:00","77W",
            new String[][]{{"J","5"},{"Y","9"},{"M","9"},{"K","4"}});
        insertFlight("DXB","CAI","FZ","1701","1400","1700","3:00","73H",
            new String[][]{{"Y","9"},{"M","6"},{"K","3"}});

        // CAI → LHR
        insertFlight("CAI","LHR","MS","0777","0800","1230","6:30","788",
            new String[][]{{"J","6"},{"C","9"},{"Y","9"},{"M","8"},{"K","5"}});
        insertFlight("CAI","LHR","BA","0154","1000","1435","6:35","789",
            new String[][]{{"J","4"},{"Y","9"},{"M","7"},{"K","3"}});
        insertFlight("CAI","LHR","MS","0779","2300","0335","6:35","333",
            new String[][]{{"J","3"},{"Y","9"},{"M","9"},{"K","6"}});

        // CAI → CDG
        insertFlight("CAI","CDG","MS","0801","0700","1100","6:00","333",
            new String[][]{{"J","4"},{"Y","9"},{"M","9"},{"K","6"}});
        insertFlight("CAI","CDG","AF","1578","1200","1600","6:00","320",
            new String[][]{{"J","3"},{"Y","9"},{"M","8"},{"K","5"}});

        // CAI → IST
        insertFlight("CAI","IST","TK","0695","0600","0900","3:00","321",
            new String[][]{{"J","5"},{"Y","9"},{"M","9"},{"K","4"}});
        insertFlight("CAI","IST","MS","0755","1000","1300","3:00","73H",
            new String[][]{{"Y","9"},{"M","8"},{"K","5"}});
        insertFlight("CAI","IST","TK","0697","1800","2100","3:00","32N",
            new String[][]{{"J","4"},{"Y","9"},{"M","7"},{"K","3"}});
    }

    private void insertFlight(String from, String to, String airline, String fltNum,
                               String dep, String arr, String dur, String aircraft,
                               String[][] classes) {
        jdbc.update(
            "INSERT INTO flights (route_from,route_to,airline,flight_num,dep_time,arr_time,duration,aircraft) VALUES (?,?,?,?,?,?,?,?)",
            from, to, airline, fltNum, dep, arr, dur, aircraft);
        Long id = jdbc.queryForObject(
            "SELECT id FROM flights WHERE airline=? AND flight_num=? AND route_from=? AND route_to=? ORDER BY id DESC LIMIT 1",
            Long.class, airline, fltNum, from, to);
        if (id != null) {
            for (String[] cls : classes)
                jdbc.update("INSERT INTO flight_classes (flight_id,class_code,seats_avail) VALUES (?,?,?)",
                    id, cls[0], Integer.parseInt(cls[1]));
        }
    }

    // ── Fares ─────────────────────────────────────────────────────────────────
    private void seedFares() {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM fares", Integer.class);
        if (count != null && count > 0) return;

        // CAI-DXB
        insertFare("CAI","DXB","MS","YOWMS","OW","Y","EGP",4500,800,"NONREF/NONEND");
        insertFare("CAI","DXB","MS","MOWMS","OW","Y","EGP",3200,800,"NONREF/NONEND");
        insertFare("CAI","DXB","MS","KOWMS","OW","Y","EGP",2800,800,"NONREF/NONEND");
        insertFare("CAI","DXB","MS","JOWMS","OW","J","EGP",12000,1200,"REF/END OK");
        insertFare("CAI","DXB","MS","FOWMS","OW","F","EGP",22000,1500,"REF/END OK");
        insertFare("CAI","DXB","EK","YOWEK","OW","Y","USD",320,65,"NONREF/NONEND");
        insertFare("CAI","DXB","EK","MOWEK","OW","Y","USD",245,65,"NONREF/NONEND");
        insertFare("CAI","DXB","EK","JOWEK","OW","J","USD",1100,120,"REF OK/NONEND");
        insertFare("CAI","DXB","EK","FOWEK","OW","F","USD",3500,200,"REF/END OK");
        insertFare("CAI","DXB","FZ","YOWFZ","OW","Y","EGP",2600,750,"NONREF/NONEND");

        // DXB-CAI
        insertFare("DXB","CAI","MS","YOWMS","OW","Y","EGP",4500,800,"NONREF/NONEND");
        insertFare("DXB","CAI","MS","MOWMS","OW","Y","EGP",3200,800,"NONREF/NONEND");
        insertFare("DXB","CAI","EK","YOWEK","OW","Y","USD",320,65,"NONREF/NONEND");
        insertFare("DXB","CAI","EK","JOWEK","OW","J","USD",1100,120,"REF OK");

        // CAI-LHR
        insertFare("CAI","LHR","MS","YOWMS","OW","Y","EGP",8500,1200,"NONREF/NONEND");
        insertFare("CAI","LHR","MS","MOWMS","OW","Y","EGP",6800,1200,"NONREF/NONEND");
        insertFare("CAI","LHR","MS","JOWMS","OW","J","EGP",28000,2000,"REF/END OK");
        insertFare("CAI","LHR","BA","YOWBA","OW","Y","GBP",450,120,"NONREF");
        insertFare("CAI","LHR","BA","JOWBA","OW","J","GBP",1850,250,"REF OK");

        // CAI-CDG
        insertFare("CAI","CDG","MS","YOWMS","OW","Y","EGP",8000,1100,"NONREF/NONEND");
        insertFare("CAI","CDG","MS","MOWMS","OW","Y","EGP",6200,1100,"NONREF/NONEND");
        insertFare("CAI","CDG","AF","YOWAF","OW","Y","EUR",380,95,"NONREF");
        insertFare("CAI","CDG","AF","JOWAF","OW","J","EUR",1600,180,"REF OK");

        // CAI-IST
        insertFare("CAI","IST","TK","YOWTK","OW","Y","USD",290,55,"NONREF/NONEND");
        insertFare("CAI","IST","TK","MOWTK","OW","Y","USD",220,55,"NONREF/NONEND");
        insertFare("CAI","IST","MS","YOWMS","OW","Y","EGP",6500,900,"NONREF/NONEND");
    }

    private void insertFare(String from, String to, String al, String basis,
                             String type, String cabin, String cur,
                             double base, double tax, String note) {
        jdbc.update(
            "INSERT INTO fares (route_from,route_to,airline,fare_basis,fare_type,cabin,currency,base_fare,tax,note) VALUES (?,?,?,?,?,?,?,?,?,?)",
            from, to, al, basis, type, cabin, cur, base, tax, note);
    }

    // ── Practice PNRs ─────────────────────────────────────────────────────────
    private void seedPnrs() throws Exception {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM pnrs", Integer.class);
        if (count != null && count > 0) return;

        String today = todayStr();
        String ttl3  = ttlStr(3);
        String ttl5  = ttlStr(5);
        String ttl7  = ttlStr(7);

        insertPnr("ABCD12","OPEN","CAIMS1234AA","AA","EMAD HASSAN", ttl3, today,
            pnr("ABCD12","OPEN","CAIMS1234AA","AA","EMAD HASSAN", ttl3, today,
                pax("MR","HASSAN","EMAD","ADT"),
                seg(1,"MS","0601","CAI","DXB","20MAY","0200","0700","Y","HK",1),
                ap("02-24567890"), ape("emad.hassan@email.com")));

        insertPnr("EFGH34","TICKETED","CAIMS1234AA","BB","AHMED ALI", null, today,
            pnr("EFGH34","TICKETED","CAIMS1234AA","BB","AHMED ALI", null, today,
                new Object[]{pax("MRS","ALI","NADIA","ADT"), pax("MSTR","ALI","KARIM","CHD")},
                new Object[]{seg(1,"EK","0926","CAI","DXB","15JUN","0615","1115","J","HK",2),
                             seg(2,"EK","0925","DXB","CAI","22JUN","0800","1100","J","HK",2)},
                ap("010-99887766"), ape("nadia.ali@email.com"),
                ssr("CHML",1,2,"CHILD MEAL REQUIRED")));

        insertPnr("IJKL56","OPEN","CAIMS1234AA","CC","SARA MOHSEN", ttl7, today,
            pnr("IJKL56","OPEN","CAIMS1234AA","CC","SARA MOHSEN", ttl7, today,
                pax("MS","MOHSEN","SARA","ADT"),
                seg(1,"MS","0777","CAI","LHR","10JUL","0800","1230","C","HK",1),
                ap("012-33445566"), null,
                ssr("VGML",1,1,"VEGETARIAN MEAL")));

        insertPnr("MNOP78","OPEN","CAIMS1234AA","AA","TRAVEL AGENT", ttl3, today,
            pnr("MNOP78","OPEN","CAIMS1234AA","AA","TRAVEL AGENT", ttl3, today,
                new Object[]{pax("MR","IBRAHIM","KHALID","ADT"), pax("MRS","IBRAHIM","RANIA","ADT")},
                new Object[]{seg(1,"QR","1234","CAI","DOH","05AUG","1000","1300","M","HK",2)},
                ap("011-22334455"), ape("khalid@company.com"),
                osi("VIP PASSENGER - GOLD MEMBER")));

        insertPnr("QRST90","REFUNDED","CAIMS1234AA","DD","JOHN SMITH", null, today,
            pnr("QRST90","REFUNDED","CAIMS1234AA","DD","JOHN SMITH", null, today,
                pax("MR","SMITH","JOHN","ADT"),
                seg(1,"BA","0154","CAI","LHR","01MAR","1000","1435","Y","XX",1),
                ap("+44-7700900123"), null,
                rm("REFUND PROCESSED 05MAR")));

        insertPnr("UVWX11","OPEN","CAIMS1234AA","EE","BOOKING ONLINE", ttl5, today,
            pnr("UVWX11","OPEN","CAIMS1234AA","EE","BOOKING ONLINE", ttl5, today,
                pax("MR","FAROUK","TAREK","ADT"),
                new Object[]{seg(1,"TK","0695","CAI","IST","25MAY","0600","0900","Y","HK",1),
                             seg(2,"TK","0697","IST","CAI","01JUN","1800","2100","Y","HK",1)},
                ap("015-66778899"), ape("tarek.farouk@mail.com")));
    }

    // ── PNR builder helpers ───────────────────────────────────────────────────
    private Map<String,Object> pax(String title, String last, String first, String type) {
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("title",title); m.put("last",last); m.put("first",first); m.put("pax_type",type);
        return m;
    }
    private Map<String,Object> seg(int n, String al, String flt, String from, String to,
                                    String date, String dep, String arr, String cls,
                                    String st, int seats) {
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("seg",n); m.put("airline",al); m.put("flight",flt);
        m.put("from",from); m.put("to",to); m.put("date",date);
        m.put("dep",dep); m.put("arr",arr); m.put("class",cls);
        m.put("status",st); m.put("seats",seats);
        return m;
    }
    private Map<String,Object> ap(String val) {
        Map<String,Object> m = new LinkedHashMap<>(); m.put("type","AP"); m.put("value",val); return m;
    }
    private Map<String,Object> ape(String val) {
        Map<String,Object> m = new LinkedHashMap<>(); m.put("type","APE"); m.put("value",val); return m;
    }
    private Map<String,Object> ssr(String code, int seg, int pax, String text) {
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("code",code); m.put("seg",seg); m.put("pax",pax); m.put("text",text); return m;
    }
    private Map<String,Object> osi(String text) {
        Map<String,Object> m = new LinkedHashMap<>(); m.put("text",text); return m;
    }
    private Map<String,Object> rm(String text) { return Collections.singletonMap("text", text); }

    @SuppressWarnings("unchecked")
    private Map<String,Object> pnr(String loc, String status, String office, String sine,
                                    String rf, String ttl, String created,
                                    Object... extras) {
        Map<String,Object> p = new LinkedHashMap<>();
        p.put("locator",loc); p.put("status",status); p.put("office",office);
        p.put("sine",sine); p.put("rf",rf); p.put("ttl",ttl); p.put("created",created);

        List<Map<String,Object>> passengers = new ArrayList<>();
        List<Map<String,Object>> itinerary  = new ArrayList<>();
        List<Map<String,Object>> contacts   = new ArrayList<>();
        List<Map<String,Object>> ssr        = new ArrayList<>();
        List<Map<String,Object>> osi        = new ArrayList<>();
        List<String>             remarks    = new ArrayList<>();

        for (Object e : extras) {
            if (e == null) continue;
            if (e instanceof Object[]) {
                for (Object item : (Object[]) e) processExtra(item, passengers, itinerary, contacts, ssr, osi, remarks);
            } else {
                processExtra(e, passengers, itinerary, contacts, ssr, osi, remarks);
            }
        }
        p.put("passengers",passengers); p.put("itinerary",itinerary);
        p.put("contacts",contacts); p.put("ssr",ssr); p.put("osi",osi); p.put("remarks",remarks);
        return p;
    }

    @SuppressWarnings("unchecked")
    private void processExtra(Object e, List<Map<String,Object>> passengers,
                               List<Map<String,Object>> itinerary, List<Map<String,Object>> contacts,
                               List<Map<String,Object>> ssr, List<Map<String,Object>> osi,
                               List<String> remarks) {
        if (!(e instanceof Map)) return;
        Map<String,Object> m = (Map<String,Object>) e;
        if (m.containsKey("pax_type"))  passengers.add(m);
        else if (m.containsKey("seg"))  itinerary.add(m);
        else if (m.containsKey("type")) contacts.add(m);
        else if (m.containsKey("code")) ssr.add(m);
        else if (m.containsKey("text") && m.size()==1) remarks.add((String)m.get("text"));
        else osi.add(m);
    }

    private void insertPnr(String loc, String status, String office, String sine,
                            String rf, String ttl, String created,
                            Map<String,Object> pnrData) throws Exception {
        String json = mapper.writeValueAsString(pnrData);
        jdbc.update(
            "INSERT IGNORE INTO pnrs (locator,status,created,office,sine,ttl,rf,raw_json) VALUES (?,?,?,?,?,?,?,?)",
            loc, status, created, office, sine, ttl, rf, json);
    }

    // ── Date helpers ──────────────────────────────────────────────────────────
    private static final String[] MONTHS = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
    private String todayStr() {
        Calendar c = Calendar.getInstance();
        return String.format("%02d%s%d", c.get(Calendar.DAY_OF_MONTH),
            MONTHS[c.get(Calendar.MONTH)], c.get(Calendar.YEAR));
    }
    private String ttlStr(int plusDays) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, plusDays);
        return String.format("%02d%s%d/2359Z", c.get(Calendar.DAY_OF_MONTH),
            MONTHS[c.get(Calendar.MONTH)], c.get(Calendar.YEAR));
    }
}
