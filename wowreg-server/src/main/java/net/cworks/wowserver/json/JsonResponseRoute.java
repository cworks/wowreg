/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowserver.json
 * Class: JsonResponseRoute
 * Created: 8/15/14 11:44 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowserver.json;

import net.cworks.json.JsonElement;
import spark.ResponseTransformerRoute;

import static net.cworks.json.Json.Json;

public abstract class JsonResponseRoute extends ResponseTransformerRoute {

    protected JsonResponseRoute(String path) {
        super(path, "application/json");
    }

    protected JsonResponseRoute(String path, String acceptType) {
        super(path, acceptType);
    }

    @Override
    public String render(Object model) {
        if(model instanceof JsonElement) {
            return Json().toJson((JsonElement)model);
        }
        return Json().toJson(model);
    }

    public String render(JsonElement model) {
        return Json().toJson(model);
    }
}
