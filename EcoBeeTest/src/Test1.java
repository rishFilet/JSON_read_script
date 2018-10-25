import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test1 {

    private String fileLocation = "src/resources/test_results.json";
    private JSONArray testList = new JSONArray();
    private String newLine = System.getProperty("line.separator");

    public void readJson(){


        JSONParser jsonParser = new JSONParser();
        try {
            Object obj1 = jsonParser.parse(new FileReader(fileLocation));
            JSONObject jsonObject = (JSONObject) obj1;
            testList = (JSONArray) jsonObject.get("test_suites");
        }
        catch (FileNotFoundException e) {
            System.out.println("Could not find file at location: "+fileLocation);
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Could not parse json file due to: "+e);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Could not read json file due to : "+e);
            e.printStackTrace();
        }

        for (Object obj:testList){
            JSONObject testSuite = (JSONObject) obj;

            String suiteName = (String) testSuite.get("suite_name");
            JSONArray results = (JSONArray) testSuite.get("results");

            System.out.println(newLine);


            int passCounter =0;
            int failCounter=0;
            int blockCounter=0;
            int tenCounter=0;
            List<String> passedTests= new ArrayList<>();
            List<String> failedTests = new ArrayList<>();
            List<String> blockedTests = new ArrayList<>();
            List<String> tenSecondTests = new ArrayList<>();
            for (Object r: results){
                JSONObject result = (JSONObject) r;
                String test_name = (String) result.get("test_name");
                String time = (String) result.get("time");
                String status = (String) result.get("status");
                if(status.matches("pass")){
                    passedTests.add("Test Name: "+test_name);
                    passedTests.add("Time: "+time);
                    passedTests.add(newLine);
                    passCounter+=1;
                }
                else if (status.matches("fail")){
                    failedTests.add("Test Name: "+test_name);
                    failedTests.add("Time: "+time);
                    failedTests.add(newLine);
                    failCounter+=1;
                }
                else if (status.matches("blocked")){
                    blockedTests.add("Test Name: "+test_name);
                    blockedTests.add("Time: "+time);
                    blockedTests.add(newLine);
                    blockCounter+=1;
                }
                else{
                    System.out.println("ERROR: Status is incorrect or not in string format at: "+test_name+", "+time+", "+"status");
                }
                if (!time.isEmpty()) {
                    try {
                        if (Float.valueOf(time) > 10.000) {
                            tenSecondTests.add("Test Name: " + test_name);
                            tenSecondTests.add("Time: " + time);
                            tenSecondTests.add(newLine);
                            tenCounter += 1;
                        }
                    }
                    catch (NumberFormatException e){
                        System.out.println("Time is an empty field");
                    }
                }
            }
            System.out.println(suiteName+ ": Passed Tests");
            System.out.println("===========================");
            System.out.println("TOTAL TESTS PASSED: "+passCounter);
            System.out.println(newLine);
            for (int a = 0;a<passedTests.size();a++){
                System.out.println(passedTests.get(a));
            }
            System.out.println(suiteName+ ": Failed Tests");
            System.out.println("===========================");
            System.out.println("TOTAL TESTS FAILED: "+failCounter);
            System.out.println(newLine);
            for (int a = 0;a<failedTests.size();a++){
                System.out.println(failedTests.get(a));
            }
            System.out.println(suiteName+ ": Blocked Tests");
            System.out.println("===========================");
            System.out.println("TOTAL TESTS BLOCKED: "+blockCounter);
            System.out.println(newLine);
            for (int a = 0;a<blockedTests.size();a++){
                System.out.println(blockedTests.get(a));
            }
            System.out.println(suiteName+ ": More than 10 Second Tests");
            System.out.println("===========================");
            System.out.println("TOTAL TESTS TAKING MORE THAN 10 SECONDS: "+tenCounter);
            System.out.println(newLine);
            for (int a = 0;a<tenSecondTests.size();a++){
                System.out.println(tenSecondTests.get(a));
            }
        }

    }
    public static void main(String[] args){
        Test1 obj = new Test1();
        obj.readJson();
    }

}
