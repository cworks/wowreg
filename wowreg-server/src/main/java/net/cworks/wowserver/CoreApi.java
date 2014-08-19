/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * Baked with love by comartin
 * Package: net.cworks.wowserver
 * User: comartin
 * Created: 8/18/2014 3:40 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */


package net.cworks.wowserver;

import net.cworks.json.JsonObject;
import static net.cworks.json.Json.Json;

public class CoreApi {


    protected JsonObject errorResponse(Integer httpStatus, String message) {

        JsonObject error = Json().object().number("httpStatus", httpStatus)
            .string("error", message).build();
        return error;
    }


}
