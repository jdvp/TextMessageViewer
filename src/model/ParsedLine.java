package model;

import java.util.HashMap;

/**
 * A ParsedLine is a searchable mapping of all of the key-value
 * pairs found within the line of a SMS record
 *
 * @author JD Porterfield
 */
public class ParsedLine {

    /**
     * A HashMap that contains all of the key-value pairs found in an SMS record line
     */
    HashMap<String,String> fields = new HashMap<String, String>();

    /**
     * The ParsedLine constructor.
     * Takes in a String (taken from an XML file)
     * And parses all of the keys and values held within
     * the line into a HashMap
     * @param lineToParse The line that is to be parsed
     */
    public ParsedLine(String lineToParse){
        lineToParse = lineToParse.replaceAll("<sms ", "");
        while(lineToParse.length()>0){
            if(lineToParse.equals("/>")){
                break;
            }
            String fieldName = "";
            String fieldValue = "";
            int separator = lineToParse.indexOf("=");
            char valueContainer = lineToParse.charAt(separator+1);
            fieldName = lineToParse.substring(0,separator);
            int endBody = lineToParse.indexOf(valueContainer,separator+2);
            fieldValue = lineToParse.substring(separator+2,endBody);
            lineToParse = lineToParse.substring(endBody+2);
            fields.put(fieldName,fieldValue);
        }
    }

    /**
     * Finds a value mapped to the input fieldToFind
     * @param fieldToFind The field that is being searched for
     * @return The value that belongs to the input fieldToFind
     */
    public String findExactField(String fieldToFind) {
        return fields.get(fieldToFind);
    }

    /**
     * Finds an approximate match to the input fieldToFind
     * @param fieldToFind The field that is being searched for
     * @return The value that belongs to a key that contains fieldToFind
     */
    public String findApproximateMatch(String fieldToFind) {
        for(String key: fields.keySet()){
            if(key.contains(fieldToFind))
                return fields.get(key);
        }
        return null;
    }

    /**
     * This method tries to find an exact match based on
     * the input fieldToFind. If it cannot find an exact
     * match, it looks for an approximate match
     *
     * @param fieldToFind The field that is being searched for
     * @return The value to the found key, or null if no key is found
     */
    public String findMatch(String fieldToFind) {
        if(findExactField(fieldToFind)==null)
            return findApproximateMatch(fieldToFind);
        return findExactField(fieldToFind);
    }
}
