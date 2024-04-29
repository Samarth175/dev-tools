package org.sudotech;

import java.util.*;

public class DuplicateChecker {

    public static void duplicateCheckInSIngleFile(String filePath, List<String> duplFieldIdentifiers, String duplicatesOutputFilePath, String uniqueOutputFilePath){
        // TODO
    }

    public static void duplicateCheckInTwoFiles(String filePath1, String filePath2, List<String> duplFieldIdentifiers, String duplicatesOutputFilePath, String uniqueOutputFilePath){
        List<DataObject> dataObjectList1 = XMLParser.getXMLFileContentAsDataObjectList(filePath1);
        List<DataObject> dataObjectList2 = XMLParser.getXMLFileContentAsDataObjectList(filePath2);

        List<List<DataObject>> comparisonResult = getDuplicatesAndUniqueListInTwoDataObjectList(dataObjectList1,dataObjectList2,duplFieldIdentifiers);
        List<DataObject> duplicatesList = comparisonResult.get(0);
        List<DataObject> uniqueList = comparisonResult.get(1);

        System.out.println("Duplicates Count in File 2 with respect to File 1 : " + duplicatesList.size());
        // Enable the Below commented Code to print Duplicates List
//        System.out.print("Duplicates List : [");
//        for(int i=0; i<duplicatesList.size(); i++)
//            System.out.print(duplicatesList.get(i).getField("IncdnceId") + ", ");
//        System.out.println("]");

        if(duplicatesOutputFilePath != null)
            XMLParser.writeDataObjectsListToXMLFile(duplicatesList,duplicatesOutputFilePath);

        System.out.println("Unique Count in File 2 with respect to File 1 : " + uniqueList.size());
        // Enable the Below commented Code to print Unique List
//        System.out.print("Unique List : [");
//        for(int i=0; i<uniqueList.size(); i++)
//            System.out.print(uniqueList.get(i).getField("IncdnceId") + ", ");
//        System.out.print("]");

        if(uniqueOutputFilePath != null)
            XMLParser.writeDataObjectsListToXMLFile(uniqueList,uniqueOutputFilePath);
    }

    private static List<List<DataObject>> getDuplicatesAndUniqueListInTwoDataObjectList(List<DataObject> list1,List<DataObject> list2,List<String> duplFieldIdentifiers){
        List<DataObject> duplicatesList = new ArrayList<>();
        List<DataObject> uniqueList = new ArrayList<>();
        Set<String> uniqueSet = new HashSet<>();

        // Add unique keys for all records in list 1 to unique set
        for(int i=0; i<list1.size(); i++){
            String uniqueKey = "";
            for(int j=0; j<duplFieldIdentifiers.size(); j++) {
                if(uniqueKey.isEmpty())
                    uniqueKey += list1.get(i).getField(duplFieldIdentifiers.get(j));
                else
                    uniqueKey += "_" + list1.get(i).getField(duplFieldIdentifiers.get(j));
            }
            //System.out.println(uniqueKey);
            uniqueSet.add(uniqueKey);
        }
        System.out.println("List 1 objects added to Unique Set");

        // Search for each record in list 2 in unique set
        System.out.println("Comparing List 2 objects with the Unique Set");
        for(int i=0; i<list2.size(); i++){
            String uniqueKey = "";
            for(int j=0; j<duplFieldIdentifiers.size(); j++)
                if(uniqueKey.isEmpty())
                    uniqueKey += list2.get(i).getField(duplFieldIdentifiers.get(j));
                else
                    uniqueKey += "_" + list2.get(i).getField(duplFieldIdentifiers.get(j));
            //System.out.println(uniqueKey);
            if(uniqueSet.contains(uniqueKey))
                duplicatesList.add(list2.get(i));
            else
                uniqueList.add(list2.get(i));
        }

        return new ArrayList<>(){{
            add(duplicatesList);
            add(uniqueList);
        }};
    }

    public static void main(String args[]){
        // TODO - Get Inputs from args instead of HardCoding

        String filePath1 = "D:/test-workspace/dev-tools/tools/duplicate-checker/src/main/resources/IncidenceConfig-insertifnotpresent-common.xml";
        String filePath2 = "D:/test-workspace/dev-tools/tools/duplicate-checker/src/main/resources/IncidenceConfig-insertifnotpresent-common-EMEA-end.xml";
        //String filePath1 = "D:/test-workspace/dev-tools/utils/xml-parser/src/main/resources/IncidenceConfig-sample1.xml";
        //String filePath2 = "D:/test-workspace/dev-tools/utils/xml-parser/src/main/resources/IncidenceConfig-sample2.xml";

        String duplicatesOutputFilePath = "D:/test-workspace/dev-tools/tools/duplicate-checker/src/main/resources/IncidenceConfig-Duplicates.xml";
        String uniqueOutputFilePath = "D:/test-workspace/dev-tools/tools/duplicate-checker/src/main/resources/IncidenceConfig-Unique.xml";

        List<String> duplFieldIdentifiers = Arrays.asList("IncdnceCd","PrcCd","WorkflwCd","MsgFctnCd","BizFctnCd");
        duplicateCheckInTwoFiles(filePath1,filePath2,duplFieldIdentifiers,duplicatesOutputFilePath,uniqueOutputFilePath);
    }
}
