/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowconf
 * Class: Registrar
 * Created: 8/20/14 11:06 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowconf;

import net.cworks.json.JsonArray;
import net.cworks.json.JsonObject;
import net.cworks.wowreg.db.WowRegDb;

final public class Registrar {

    private static final Registrar INSTANCE = new Registrar();

    private Registrar() { }

    public static Registrar instance() {
        return INSTANCE;
    }

    public int register(JsonObject attendee) {
        WowRegDb db = db();

        close(db);
        return 0;
    }

    public int register(JsonArray attendees) {
        WowRegDb db = db();

        close(db);
        return 0;
    }

    int recordCosts(JsonObject attendee) {
        WowRegDb db = db();

        close(db);
        return 0;
    }

    int recordCosts(JsonArray attendees) {
        WowRegDb db = db();

        close(db);
        return 0;
    }

    int recordAgeClass(JsonObject attendee) {
        WowRegDb db = db();

        close(db);
        return 0;
    }

    int recordAgeClass(JsonArray attendees) {
        WowRegDb db = db();

        close(db);
        return 0;
    }

    private WowRegDb db() {
        return WowRegDb.db("root", "", "jdbc:mysql://localhost:3306/wowreg");
    }

    private void close(WowRegDb db) {
        db.close();
    }
}
