package org.scify.memorimusicgame.helper;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Responsible for dealing with JSON files.
 */
public class JSONFileHandler {


    /**
     * Given a JSONObject and a name of an array inside this object, get the array
     * @param arrayName the array name
     * @return the shuffled set
     */
    public JSONArray getJSONArrayFromObject(JSONObject object, String arrayName) {
        JSONArray jsonArray = object.getJSONArray(arrayName); // Get all JSONArray rows
        // shuffle the rows (we want the cards to be in a random order)
        return jsonArray;
    }


    /**
     * Parses a {@link JSONArray} elements to a String array
     * @param jsonArray the JSON formatted array ( eg ["1", "2"] )
     * @return a String array containing the elements of the JSON array
     */
    public String[] jsonArrayToStringArray(JSONArray jsonArray){
        String[] stringArray = null;
        int length = jsonArray.length();
        if(jsonArray!=null){
            stringArray = new String[length];
            for(int i=0;i<length;i++){
                stringArray[i]= jsonArray.optString(i);
            }
        }
        return stringArray;
    }


}
