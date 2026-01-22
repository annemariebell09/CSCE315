import java.util.*;
import java.io.IOException;
import java.nio.file.*; 
import java.util.concurrent.TimeUnit;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.HashMap;
import java.util.*;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.*;
import java.io.*;

public class DataCleaner {

    static void cleanPrincipals(String fileParse) {
        try {

            Path filePath = Paths.get(fileParse+".csv");//filepath that we are reading
            List<String> lines = Files.readAllLines(filePath); //lines is a list of string(one for each line) that we parse
            FileWriter csvWriter = new FileWriter(fileParse+"_clean.csv"); //creates output object

            //iterate through each line in the csv and clean it
            for(String line:lines) {
                //all spaces are replaced with comma and then only columns 1,2,3 are selected. To clean we only these columns
                line = line.replaceAll("\t", ",");
                line = line.replaceAll("\"", "");

                String [] split = line.split(",");
                String [] processedLine = new String[3];

                processedLine[0] = split[1];
                processedLine[1] = split[2];
                processedLine[2] = split[3];
                
                //wrties each line to the csv and adds new line after one row is done writing
                for (int i=0; i<3; i++) {
                    csvWriter.append(processedLine[i]);
                    if (i != 2)
                        csvWriter.append(",");
                }

                csvWriter.append("\n");
            }

            //makes sure file is closed
            csvWriter.flush();
            csvWriter.close(); 

        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**  
     *  Converts CSV File to a List of String arrays.
     *  Each String array corresponds to each line in the file.
     *  The first array is reserved for column headers.
     */
    private static ArrayList<ArrayList<LinkedList<String>>> ingestCrewData(String fileName) {
        Scanner scanner;
        try {
            scanner = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            return new ArrayList<ArrayList<LinkedList<String>>>();
        }
        scanner.useDelimiter("\\t|,|\"");

        ArrayList<ArrayList<LinkedList<String>>> formattedData = new ArrayList<ArrayList<LinkedList<String>>>();

        // 1. Add Headers
        if (scanner.hasNextLine()) {
            ArrayList<LinkedList<String>> headers = new ArrayList<LinkedList<String>>(); 
            
            LinkedList<String> header = new LinkedList<String>();
                
            String[] line = scanner.nextLine().split("\\t|,");
            for (int i = 1; i < line.length; i++) {
                String value = line[i].replaceAll("\"", "");
                header.add(value);
            }
            headers.add(header); 
            formattedData.add(headers);
        } else {
            return new ArrayList<ArrayList<LinkedList<String>>>();
        }
        formattedData.get(0).get(0).removeFirst();
        

        // 2. Add Data Rows to List
        while (scanner.hasNextLine()) {
            
            // Initalize Data Row with 2 entries
            ArrayList<LinkedList<String>> dataRow = new ArrayList<LinkedList<String>>(); 
            for (int i = 0; i < 2; i++) {
                dataRow.add(new LinkedList());
            }

            // Clean up line (has extra ")
            String[] line = scanner.nextLine().replaceAll("\"", "").split("\\t");
            for (int i = 0; i < line.length; i++) {
                // Splits columns with multiple entries
                // e.x. tt3454    nm1233,nm1234 <----- 
                String[] data = line[i].split(",");
                for (int j = 0; j < data.length; j++) {
                    // Row organized as [Directors (tt), Writers (nm)]
                    if (data[j].startsWith("tt")) {
                        dataRow.get(0).add(data[j]);
                    }
                    else if (data[j].startsWith("nm")) {
                        dataRow.get(1).add(data[j]);
                    }
                }
            }

            // Only add if not missing data
            if (dataRow.get(0).size() != 0 && dataRow.get(1).size() != 0) {
                formattedData.add(dataRow);
            }

        }
        return formattedData;
    }
    /**  
     *  Removes duplicate rows from inputted data
     */
    private static ArrayList<ArrayList<LinkedList<String>>> removeDuplicates(ArrayList<ArrayList<LinkedList<String>>> data) {
        HashSet set = new HashSet();
        for (int i = 0; i < data.size(); i++) {
            if (set.add(data.get(i).get(0)) == false) { // Sets do not allow duplicates
                data.remove(i);
            }
        }
        return data;
    }

    private static void writeToCSV(ArrayList<ArrayList<LinkedList<String>>> data, String fileName) {
        
        try {
            PrintWriter writer = new PrintWriter(new File(fileName+"_clean.csv"));
            for (ArrayList<LinkedList<String>> line : data) {
                StringBuilder sb = new StringBuilder();
                for (LinkedList<String> entry : line) {
                    String dataRow = Arrays.toString(entry.toArray());
                    if (line == data.get(0)) {
                        sb.append(dataRow.substring(1, dataRow.length() - 1).replaceAll(" ", "").replaceAll(",", ";"));
                    }
                    else if (entry != line.get(line.size()-1)) {
                        sb.append(dataRow.substring(1, dataRow.length() - 1).replaceAll(" ", "").replaceAll(",", ";") + ";");
                    } else {
                        sb.append("{" + dataRow.substring(1, dataRow.length() - 1).replaceAll(" ", "") + "}");
                    }
                }
                sb.append("\n");
                writer.write(sb.toString());
            } 
            writer.close();
          } catch (FileNotFoundException e) { }
    }
    static void cleanCrew(String fileParse) {
        String fileName = fileParse+".csv";
        ArrayList<ArrayList<LinkedList<String>>> data = ingestCrewData(fileName);
        data = removeDuplicates(data);
        writeToCSV(data, fileParse);
    }

    static void cleanCustomerRatings(String fileParse) {
        try {
		
			//this declares a tab so that it is easier to remove
			String remove = "\t";
			
			//create the arrays to hold the dates and ratings
			Vector <String> dropData = new Vector<String>();
			Vector <String>  custID = new Vector<String>();
			Vector <String>  rating = new Vector<String>();
			Vector <String>  date = new Vector<String>();
			Vector <String>  titleID = new Vector<String>();
			
			//get all the lines of the csv into a file
			List<String> lines = Files.readAllLines(Paths.get(fileParse+".csv"));
			
			for(String line : lines) {
				//remove all the tabs and replace them with commas
				line = line.replaceAll(remove, ",");
				//remove all double quotes at the end 
				line = line.replaceAll("\"", "" );
				//System.out.println(line);
				//spilt the line into an array and put them in right one
				String[] lineArray = line.split(",");

				dropData.add(lineArray[0]);
				custID.add(lineArray[1]);
				rating.add(lineArray[2]);
				date.add(lineArray[3]);
				titleID.add(lineArray[4]);
			}
			
			/*
			 * Write the data to a new csv file without the first line of data
			 */
			PrintWriter writer = new PrintWriter(new File(fileParse+"_clean.csv"));
			StringBuilder sb = new StringBuilder();
			
			sb.append("Customer ID");
			sb.append(",");
			sb.append("Movie ID");
			sb.append(",");
			sb.append("Movie rating");
			sb.append('\n');
			writer.write(sb.toString());
			
			for (int i = 0; i < custID.size(); i++) {
				//append the strings then write to the new csv
				StringBuilder str  = new StringBuilder();
				
				str.append(custID.elementAt(i));
				str.append(",");
				str.append(titleID.elementAt(i));
				str.append(",");
				str.append(rating.elementAt(i));
				str.append('\n');
				writer.write(str.toString());
			}
		
		}
		
		catch(Exception e) {
			//if the file does not exist simply call the error
			System.out.println(e.getMessage());
		}
    }

    static void cleanNames(String fileParse) {
        try{
            String remove = "\t";
            Path filePath = Paths.get(fileParse+".csv");
            List<String> lines = Files.readAllLines(filePath);

            FileWriter new_file = new FileWriter(fileParse+"_clean.csv");

            for(String line : lines){ 

                /*
                * Deleting random tabs with commas
                * Deleting random spaces with nothing
                */
                line = line.replaceAll(remove, ",");
                line = line.replaceAll("\"","");
               
                /*
                * create new variable comma_count in order to seperate and delete parts of the files
                * created string in order to append to them and then delete them after looping
                */

                int comma_count = 0;
                String dates = "";
                String professions = "";

                /*
                * loop through each character in a line 
                */

                for(int i = 0; i < line.length(); i++){
                    if(line.charAt(i) == ','){ //increment if a comma
                        comma_count++;
                    }

                    if(comma_count >= 3 && comma_count < 5){//this is appending the unecessary dates to the string
                        dates += line.charAt(i);   
                    }

                    if(comma_count >= 6){//this is appending the unecessary professions to the string
                        professions += line.charAt(i);
                    }

                }
                /*
                * updating line and deleting the unecessary parts of the line
                */

                line = line.replace(dates, ""); 
                line = line.replace(professions, "");

                /*
                * repeating same process as above
                * this time we are getting rid of the entire line for names that do not have a profession 
                * associated with them
                */                

                int comma_count_2 = 0;
                String no_profession = "";

                for(int i = 0; i < line.length(); i++){
                    if(line.charAt(i) == ','){ //keeping track of the commas
                        comma_count_2++;
                    }

                    if(comma_count_2 == 3){ //in primaryProfession part of file
                        if(line.charAt(line.length()-1) >= 'a' && line.charAt(line.length()-1) <= 'z'){//if it has an letter in the slot 
                            break;
                        }else{
                            no_profession += line.substring(0, line.length()); //if no profession grab the whole line
                        }
                    }
                }
                /*
                * Delete whole line
                */
                line = line.replace(no_profession,"");
                
                /*
                * if there is an empty line do not write it into the file 
                */
                if(!line.isEmpty()){
                    new_file.append(line);
                    new_file.append("\n");
                }
                
            }
            new_file.close();

        }catch(Exception e){
            System.out.println(e);
        }
    }

    static void cleanTitles(String fileParse) {
        try {
            // Creates a hashmap table to store titles with their most recent years (no old duplicates)
            Scanner sc = new Scanner(new File(fileParse + ".csv"));
            HashMap<String, String> titleAndYear = new HashMap<String, String>();
            String removeFirst = sc.nextLine();
            while (sc.hasNext()) {
                String row = sc.nextLine();
                String[] temp = row.split("\t");
                // if doesn't contain title, put into table
                // else, check if the duplicate title has a greater year than one in hashmap table, then replace
                if (!titleAndYear.containsKey(temp[3])) { 
                    titleAndYear.put(temp[3], temp[8]);
                } else {
                    if (!titleAndYear.get(temp[3]).equals(null) && (Integer.parseInt(temp[8]) > Integer.parseInt(titleAndYear.get(temp[3])))) {
                        titleAndYear.remove(temp[3]);
                        titleAndYear.put(temp[3], temp[8]);
                    }
                }
            }
            
            // 
            Scanner sc2 = new Scanner(new File(fileParse + ".csv"));
            FileWriter newFile = new FileWriter(fileParse + "_clean.csv");
            removeFirst = sc2.nextLine();
            String[] split = removeFirst.split("\t");
            // header line in csv
            newFile.write(split[1].concat(",").concat(split[2]).concat(",").concat(split[3]).concat(",").concat(split[7]).concat(",").concat(split[8]).concat(",").concat(split[9]).concat(",").concat(split[10]).concat("\n"));
            while (sc2.hasNext()) {
                String row = sc2.nextLine();
                String[] temp = row.split("\t");
                // only input into table if hashmap contains title and year
                if (titleAndYear.containsKey(temp[3])) {
                    if (!titleAndYear.get(temp[3]).equals(null) && titleAndYear.get(temp[3]).equals(temp[8])) {
                        int numCommas = row.length() - row.replace(",", "").length();
                        // if commas exist, that means more than one genre. Replace commas with dashes
                        if (numCommas > 0) {
                            String[] temp2 = row.split("\t");
                            row = row.replaceFirst(temp2[7], temp2[7].replaceAll(",","-"));
                        }
                        // replace all tabs with commas, also replace movie title commas with dashes
                        String[] splitFinal = row.split("\t");
                        splitFinal[3] = splitFinal[3].replaceAll(",","-");
                        newFile.write(splitFinal[1].concat(",").concat(splitFinal[2]).concat(",").concat(splitFinal[3]).concat(",").concat(splitFinal[7]).concat(",").concat(splitFinal[8]).concat(",").concat(splitFinal[9]).concat(",").concat(splitFinal[10]).concat("\n"));
                        // remove title from hashmap table once done to avoid duplicates
                        titleAndYear.remove(temp[3]);
                    }
                }
            }
            newFile.close();
        } catch (FileNotFoundException ie) {
            System.out.println(ie);
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public static void main(String[] args){
        //decide which file to clean
        String fileParse = "titles";
        if (fileParse.equals("principals")) {
            cleanPrincipals(fileParse);
        } else if (fileParse.equals("crew")) {
            cleanCrew(fileParse);
        } else if (fileParse.equals("customer_ratings")) {
            cleanCustomerRatings(fileParse);
        } else if (fileParse.equals("names")) {
            cleanNames(fileParse);
        } else if (fileParse.equals("titles")) {
            cleanTitles(fileParse);
        }
    }
}