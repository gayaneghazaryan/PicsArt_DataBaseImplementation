package db;

public class utils {

    public static boolean isNumber(String input) {
        try{
            Integer.parseInt(input);
        }catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isCharacter(String input) {
        return input.length() == 1;
    }

    public static boolean isValidInput(String[] inputData, String[] dataTypes) {
        for(int i = 0; i < inputData.length; i++) {
            if(dataTypes[i].equalsIgnoreCase("integer")) {
                return isNumber(inputData[i]);
            }
            if(dataTypes[i].equalsIgnoreCase("character")) {
                if(inputData[i].length() != 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkValidQuery(String dataType, String query) {
        return (!dataType.equalsIgnoreCase("integer") || utils.isNumber(query)) &&
                (!dataType.equalsIgnoreCase("character") || utils.isCharacter(query));
    }

}
