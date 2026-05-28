-- ============================================================
--  AMADEUS TRAINING SIMULATOR — MySQL Workbench Reference
--  Use this file directly in MySQL Workbench 8.0
--  The app auto-creates the schema on first run.
-- ============================================================

USE amadeus_db;

-- ── View all PNRs ─────────────────────────────────────────────
SELECT locator, status, rf, created, ttl, office
FROM pnrs
ORDER BY created DESC;

-- ── View Open PNRs ────────────────────────────────────────────
SELECT locator, rf, ttl
FROM pnrs
WHERE status = 'OPEN';

-- ── View Ticketed PNRs ────────────────────────────────────────
SELECT locator, rf, created
FROM pnrs
WHERE status = 'TICKETED';

-- ── All flights from Cairo ────────────────────────────────────
SELECT airline, flight_num, route_from, route_to,
       dep_time, arr_time, aircraft
FROM flights
WHERE route_from = 'CAI'
ORDER BY dep_time;

-- ── Seat availability CAI → DXB ──────────────────────────────
SELECT f.airline, f.flight_num, fc.class_code, fc.seats_avail
FROM flights f
JOIN flight_classes fc ON fc.flight_id = f.id
WHERE f.route_from = 'CAI' AND f.route_to = 'DXB'
ORDER BY f.dep_time, fc.class_code;

-- ── Fares CAI → DXB ──────────────────────────────────────────
SELECT airline, fare_basis, fare_type, cabin,
       currency, base_fare, tax,
       (base_fare + tax) AS total, note
FROM fares
WHERE route_from = 'CAI' AND route_to = 'DXB'
ORDER BY base_fare;

-- ── Fares CAI → LHR ──────────────────────────────────────────
SELECT airline, fare_basis, cabin, currency,
       base_fare, tax, (base_fare + tax) AS total, note
FROM fares
WHERE route_from = 'CAI' AND route_to = 'LHR'
ORDER BY base_fare;

-- ── All airlines ──────────────────────────────────────────────
SELECT code, name FROM airlines ORDER BY code;

-- ── All cities ────────────────────────────────────────────────
SELECT code, name, country FROM cities ORDER BY code;

-- ── All SR codes ──────────────────────────────────────────────
SELECT code, description FROM sr_codes ORDER BY code;

-- ── Training log (recent 50) ──────────────────────────────────
SELECT session_id, command, ts
FROM training_log
ORDER BY ts DESC
LIMIT 50;

-- ── Commands per session ──────────────────────────────────────
SELECT session_id, COUNT(*) AS total_commands, MAX(ts) AS last_used
FROM training_log
GROUP BY session_id
ORDER BY last_used DESC;

-- ── Add a new flight ──────────────────────────────────────────
-- INSERT INTO flights (route_from, route_to, airline, flight_num,
--                      dep_time, arr_time, duration, aircraft)
-- VALUES ('CAI', 'FRA', 'LH', '0581', '0800', '1300', '5:00', '333');

-- ── Add a new fare ────────────────────────────────────────────
-- INSERT INTO fares (route_from, route_to, airline, fare_basis,
--                    fare_type, cabin, currency, base_fare, tax, note)
-- VALUES ('CAI', 'FRA', 'LH', 'YOWLH', 'OW', 'Y', 'EUR', 420, 85, 'NONREF');

-- ── Reset / delete all PNRs (practice reset) ──────────────────
-- DELETE FROM pnrs;
