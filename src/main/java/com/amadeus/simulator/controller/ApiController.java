package com.amadeus.simulator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired private JdbcTemplate jdbc;
    @Autowired private ObjectMapper mapper;

    // ── Health ────────────────────────────────────────────────────────────────
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> r = new LinkedHashMap<>();
        try {
            jdbc.queryForObject("SELECT 1", Integer.class);
            r.put("ok", true); r.put("db", "MySQL OK");
        } catch (Exception e) {
            r.put("ok", false); r.put("db", "MySQL ERR: " + e.getMessage());
        }
        return r;
    }

    // ── Reference data ────────────────────────────────────────────────────────
    @GetMapping("/airlines")
    public List<Map<String, Object>> airlines() {
        return jdbc.queryForList("SELECT * FROM airlines ORDER BY code");
    }

    @GetMapping("/cities")
    public List<Map<String, Object>> cities() {
        return jdbc.queryForList("SELECT * FROM cities ORDER BY code");
    }

    @GetMapping("/aircraft")
    public List<Map<String, Object>> aircraft() {
        return jdbc.queryForList("SELECT * FROM aircraft_types ORDER BY code");
    }

    @GetMapping("/sr-codes")
    public List<Map<String, Object>> srCodes() {
        return jdbc.queryForList("SELECT * FROM sr_codes ORDER BY code");
    }

    // ── Availability ──────────────────────────────────────────────────────────
    @GetMapping("/availability/{from}/{to}")
    public ResponseEntity<?> availability(@PathVariable String from,
                                           @PathVariable String to,
                                           @RequestParam(required = false) String airline) {
        try {
            String f = from.toUpperCase();
            String t = to.toUpperCase();

            StringBuilder sql = new StringBuilder(
                "SELECT f.*, a.name AS airline_name, ac.name AS aircraft_name " +
                "FROM flights f " +
                "JOIN airlines a ON a.code = f.airline " +
                "JOIN aircraft_types ac ON ac.code = f.aircraft " +
                "WHERE f.route_from = ? AND f.route_to = ?");
            List<Object> params = new ArrayList<>(Arrays.asList(f, t));
            if (airline != null && !airline.isEmpty()) {
                sql.append(" AND f.airline = ?");
                params.add(airline.toUpperCase());
            }
            sql.append(" ORDER BY f.dep_time");

            List<Map<String, Object>> flights = jdbc.queryForList(sql.toString(), params.toArray());
            for (Map<String, Object> flight : flights) {
                Long id = ((Number) flight.get("id")).longValue();
                List<Map<String, Object>> classes = jdbc.queryForList(
                    "SELECT class_code, seats_avail FROM flight_classes WHERE flight_id = ? ORDER BY class_code", id);
                flight.put("classes", classes);
            }
            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // ── Fares ─────────────────────────────────────────────────────────────────
    @GetMapping("/fares/{from}/{to}")
    public ResponseEntity<?> fares(@PathVariable String from,
                                    @PathVariable String to,
                                    @RequestParam(required = false) String airline,
                                    @RequestParam(required = false) String cabin) {
        try {
            StringBuilder sql = new StringBuilder(
                "SELECT fa.*, a.name AS airline_name FROM fares fa " +
                "JOIN airlines a ON a.code = fa.airline " +
                "WHERE fa.route_from = ? AND fa.route_to = ?");
            List<Object> params = new ArrayList<>(Arrays.asList(from.toUpperCase(), to.toUpperCase()));
            if (airline != null && !airline.isEmpty()) { sql.append(" AND fa.airline = ?"); params.add(airline.toUpperCase()); }
            if (cabin   != null && !cabin.isEmpty())   { sql.append(" AND fa.cabin = ?");   params.add(cabin.toUpperCase()); }
            sql.append(" ORDER BY fa.base_fare");
            return ResponseEntity.ok(jdbc.queryForList(sql.toString(), params.toArray()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // ── PNR list ──────────────────────────────────────────────────────────────
    @GetMapping("/pnrs")
    public ResponseEntity<?> pnrList() {
        try {
            return ResponseEntity.ok(jdbc.queryForList(
                "SELECT locator, status, rf, created, ttl, office FROM pnrs ORDER BY created DESC"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // ── PNR get ───────────────────────────────────────────────────────────────
    @GetMapping("/pnr/{locator}")
    public ResponseEntity<?> pnrGet(@PathVariable String locator) {
        try {
            List<Map<String, Object>> rows = jdbc.queryForList(
                "SELECT raw_json FROM pnrs WHERE locator = ?", locator.toUpperCase());
            if (rows.isEmpty()) return ResponseEntity.status(404).body(Map.of("error", "PNR NOT FOUND"));
            Object json = rows.get(0).get("raw_json");
            return ResponseEntity.ok(mapper.readValue(json.toString(), Map.class));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // ── PNR search by name ────────────────────────────────────────────────────
    @GetMapping("/pnr-search/{name}")
    public ResponseEntity<?> pnrSearch(@PathVariable String name) {
        try {
            List<Map<String, Object>> rows = jdbc.queryForList(
                "SELECT raw_json FROM pnrs WHERE raw_json LIKE ?", "%" + name.toUpperCase() + "%");
            List<Object> result = new ArrayList<>();
            for (Map<String, Object> row : rows)
                result.add(mapper.readValue(row.get("raw_json").toString(), Map.class));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // ── PNR create ────────────────────────────────────────────────────────────
    @PostMapping("/pnr")
    public ResponseEntity<?> pnrCreate(@RequestBody Map<String, Object> pnr) {
        try {
            if (!pnr.containsKey("locator") || pnr.get("locator") == null) {
                String loc;
                do {
                    loc = randomLocator();
                    List<Map<String, Object>> ex = jdbc.queryForList("SELECT 1 FROM pnrs WHERE locator = ?", loc);
                    if (ex.isEmpty()) break;
                } while (true);
                pnr.put("locator", loc);
            }
            String loc    = pnr.get("locator").toString().toUpperCase();
            String status = pnr.getOrDefault("status", "OPEN").toString();
            String office = pnr.getOrDefault("office", "CAIMS1234AA").toString();
            String sine   = pnr.getOrDefault("sine",   "AA").toString();
            Object ttl    = pnr.get("ttl");
            Object rf     = pnr.get("rf");
            Object created = pnr.get("created");
            String json   = mapper.writeValueAsString(pnr);

            jdbc.update("INSERT INTO pnrs (locator,status,created,office,sine,ttl,rf,raw_json) VALUES (?,?,?,?,?,?,?,?)",
                loc, status,
                created != null ? created.toString() : new java.text.SimpleDateFormat("ddMMMyyyy").format(new Date()).toUpperCase(),
                office, sine,
                ttl != null ? ttl.toString() : null,
                rf  != null ? rf.toString()  : null,
                json);
            return ResponseEntity.ok(Map.of("locator", loc));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // ── PNR update ────────────────────────────────────────────────────────────
    @PutMapping("/pnr/{locator}")
    public ResponseEntity<?> pnrUpdate(@PathVariable String locator,
                                        @RequestBody Map<String, Object> pnr) {
        try {
            String json = mapper.writeValueAsString(pnr);
            Object ttl = pnr.get("ttl");
            Object rf  = pnr.get("rf");
            jdbc.update("UPDATE pnrs SET status=?, ttl=?, rf=?, raw_json=? WHERE locator=?",
                pnr.getOrDefault("status","OPEN").toString(),
                ttl != null ? ttl.toString() : null,
                rf  != null ? rf.toString()  : null,
                json, locator.toUpperCase());
            return ResponseEntity.ok(Map.of("ok", true));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // ── Training Log ──────────────────────────────────────────────────────────
    @PostMapping("/log")
    public ResponseEntity<?> log(@RequestBody Map<String, Object> body) {
        try {
            jdbc.update("INSERT INTO training_log (session_id, command) VALUES (?,?)",
                body.get("session_id"), body.get("command"));
            return ResponseEntity.ok(Map.of("ok", true));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // ── DB Stats ──────────────────────────────────────────────────────────────
    @GetMapping("/stats")
    public ResponseEntity<?> stats() {
        try {
            Map<String, Object> s = new LinkedHashMap<>();
            s.put("flights",  jdbc.queryForObject("SELECT COUNT(*) FROM flights",       Long.class));
            s.put("pnrs",     jdbc.queryForObject("SELECT COUNT(*) FROM pnrs",          Long.class));
            s.put("airlines", jdbc.queryForObject("SELECT COUNT(*) FROM airlines",      Long.class));
            s.put("cities",   jdbc.queryForObject("SELECT COUNT(*) FROM cities",        Long.class));
            s.put("fares",    jdbc.queryForObject("SELECT COUNT(*) FROM fares",         Long.class));
            s.put("log",      jdbc.queryForObject("SELECT COUNT(*) FROM training_log",  Long.class));
            return ResponseEntity.ok(s);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // ── Reset (re-seed PNRs) ──────────────────────────────────────────────────
    @PostMapping("/reset")
    public ResponseEntity<?> reset() {
        try {
            jdbc.update("DELETE FROM pnrs");
            return ResponseEntity.ok(Map.of("ok", true, "msg", "Run app restart to re-seed all data"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // ── Utility ───────────────────────────────────────────────────────────────
    private String randomLocator() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }
}
