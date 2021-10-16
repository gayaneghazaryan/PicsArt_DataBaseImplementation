package db;

import services.FileService;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static db.utils.checkValidQuery;
import static db.utils.isValidInput;

public class DataTable {
    public String tableName;
    private final static Scanner keyboard = new Scanner(System.in);
    public String[] columns;
    public String[] columnDataTypes;
    public String[] validDataTypes = {"string", "integer", "character"};
    public List<String> rows;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public String[] getColumnDataTypes() {
        return columnDataTypes;
    }

    public void setColumnDataTypes(String[] columnDataTypes) {
        this.columnDataTypes = columnDataTypes;
    }

    public String[] getValidDataTypes() {
        return validDataTypes;
    }

    public void setValidDataTypes(String[] validDataTypes) {
        this.validDataTypes = validDataTypes;
    }

    public List<String> getRows() {
        return rows;
    }

    public void setRows(List<String> rows) {
        this.rows = rows;
    }


    public void createTable(String tableName) {
        this.tableName = tableName;
        FileService.createFile(tableName);
        System.out.println(tableName + " table is created");

        System.out.println("Give the column names separated by space");
        String columnsNames = "Id " + keyboard.nextLine() +"\n";

        columns = columnsNames.split("\\s+");

        System.out.println("Give the column types separated by space. They should be either String, Integer, or Character types");

        boolean areValidDataTypes = false;
        String dataTypesNames = "";

        while(!areValidDataTypes) {
            dataTypesNames = "String " + keyboard.nextLine()+"\n";
            columnDataTypes = dataTypesNames.split("\\s+");

            if(columns.length == columnDataTypes.length) {
                for (String dataType : columnDataTypes) {
                    if (!Arrays.asList(validDataTypes).contains(dataType.toLowerCase())) {
                        break;
                    } else {
                        areValidDataTypes = true;
                    }
                }
            }

            if(!areValidDataTypes) {
                System.out.println("Wrong input of data types, please try again");
            }

        }

        FileService.writeInFile(tableName,columnsNames);
        FileService.writeInFile(tableName, dataTypesNames);
    }

    public static DataTable selectTable() {

        System.out.println("Enter the table name (the input should not contain anything other than the name)");



        String tableName = keyboard.nextLine();

        File table = new File(tableName);
        DataTable dataTable = new DataTable();
        if(!table.exists()) {
            System.out.println("Such table does not exist! Please create the table");
            System.exit(0);
        } else {
            dataTable.setTableName(tableName);
            try{
                dataTable.setRows(FileService.readFromFile(tableName));
            }catch(IOException e) {
                e.printStackTrace();
            }

            dataTable.setColumns(dataTable.getRows().get(0).split("\\s+"));
            dataTable.setColumnDataTypes(dataTable.getRows().get(1).split("\\s+"));
            return dataTable;
        }
        return dataTable;
    }

    //insertOne
    public void insertOne() {
        boolean isValid = false;
        while(!isValid) {
            System.out.println("Please make a data entry: ");
            String id = UUID.randomUUID().toString();
            String input = id + " " + keyboard.nextLine()+"\n";
            String[] data = input.split("\\s+");
            if(isValidInput(data,columnDataTypes)){
                FileService.writeInFile(tableName,input);
                isValid = true;
            } else {
                System.out.println("Please make a valid entry");
            }
        }
    }
    //insertMany
    public void insertMany() {
        System.out.println("Please make data entries. To terminate use Ctrl+D.");
        while(keyboard.hasNextLine()) {
            insertOne();
        }

    }
    //find
    public void find(String columnName, String query) {
        int columnNum = Arrays.asList(columns).indexOf(columnName);
        String dataType = Arrays.asList(columnDataTypes).get(columnNum);

        if(!checkValidQuery(dataType,query)) {
            System.out.println("Invalid query");
            System.exit(0);
        }

        try {
            rows = FileService.readFromFile(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 2; i < rows.size(); i++) {
            String[] row = rows.get(i).split("\\s+");
            if(row[columnNum].equalsIgnoreCase(query)) {
                System.out.println(rows.get(i));
            }
        }
    }

    //updateOne
    public void updateOne(String columnName, String oldValue, String newValue) {
        int columnNum = Arrays.asList(columns).indexOf(columnName);
        String dataType = Arrays.asList(columnDataTypes).get(columnNum);

        if(!checkValidQuery(dataType,oldValue) || !checkValidQuery(dataType,newValue)) {
            System.out.println("Invalid update");
            System.exit(0);
        }

        try{
            rows = FileService.readFromFile(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 2; i < rows.size(); i++) {
            String[] row = rows.get(i).split("\\s+");
            if(row[columnNum].equalsIgnoreCase(oldValue)) {
                row[columnNum] = newValue;
                StringBuilder sb = new StringBuilder();
                for (String s : row) {
                    sb.append(s).append(" ");
                }
                rows.set(i,sb.toString());
                break;
            }

        }
        updateTable();
    }
    //updateMany
    public void updateMany(String columnName, String oldValue, String newValue) {
        int columnNum = Arrays.asList(columns).indexOf(columnName);
        String dataType = Arrays.asList(columnDataTypes).get(columnNum);

        if(!checkValidQuery(dataType,oldValue) || !checkValidQuery(dataType,newValue)) {
            System.out.println("Invalid update");
            System.exit(0);
        }

        try{
            rows = FileService.readFromFile(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 2; i < rows.size(); i++) {
            String[] row = rows.get(i).split("\\s+");
            if(row[columnNum].equalsIgnoreCase(oldValue)) {
                row[columnNum] = newValue;
                StringBuilder sb = new StringBuilder();
                for (String s : row) {
                    sb.append(s).append(" ");
                }
                rows.set(i,sb.toString());
            }

        }
        updateTable();
    }


    //deleteOne
    public void deleteOne(String columnName, String query) {
        int columnNum = Arrays.asList(columns).indexOf(columnName);
        String dataType = Arrays.asList(columnDataTypes).get(columnNum);

        if(!checkValidQuery(dataType,query)) {
            System.out.println("Invalid deletion");
            System.exit(0);
        }

        try {
            rows = FileService.readFromFile(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<String> iterator = rows.iterator();
        while(iterator.hasNext()) {
            String[] row = iterator.next().split("\\s+");
            if(row[columnNum].equalsIgnoreCase(query)) {
                iterator.remove();
                break;
            }
        }
        updateTable();
    }
    //deleteMany
    public void deleteMany(String columnName, String query) {
        int columnNum = Arrays.asList(columns).indexOf(columnName);
        String dataType = Arrays.asList(columnDataTypes).get(columnNum);

        if(!checkValidQuery(dataType,query)) {
            System.out.println("Invalid deletion");
            System.exit(0);
        }

        try {
            rows = FileService.readFromFile(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<String> iterator = rows.iterator();
        while(iterator.hasNext()) {
            String[] row = iterator.next().split("\\s+");
            if(row[columnNum].equalsIgnoreCase(query)) {
                iterator.remove();
            }

        }
        updateTable();

    }

    private void updateTable() {
        try {
            FileService.clearFile(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String row : rows) {
            FileService.writeInFile(tableName, row+"\n");
        }

    }


}
