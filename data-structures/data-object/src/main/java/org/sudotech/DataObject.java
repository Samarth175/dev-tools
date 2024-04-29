package org.sudotech;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataObject {
    private Map<String,String> metaInfoMap;
    private Map<String,String> dataObjectMap;

    DataObject(){
        metaInfoMap = new HashMap<>();
        dataObjectMap = new LinkedHashMap<>();
    }

    DataObject(Element element){
        metaInfoMap = new HashMap<>();
        dataObjectMap = new LinkedHashMap<>();

        metaInfoMap.put("DataObjectName",element.getNodeName());

        NodeList nodeList = element.getChildNodes();
        int nodeCount = nodeList.getLength();
//        System.out.println("Number of childs in Node " + element.getNodeName() + " is " + nodeCount);
        for(int i=0; i<nodeCount; i++){
            Node node = nodeList.item(i);
//            System.out.println(node.getNodeName());
            if(node.getNodeType() == Node.ELEMENT_NODE)
                dataObjectMap.put(node.getNodeName(),node.getTextContent());
        }
    }

    public Map<String,String> getMetaInfoMap(){
        return this.metaInfoMap;
    }

    public Map<String,String> getDataObjectMap(){
        return this.dataObjectMap;
    }

    public String getField(String key){
        return this.dataObjectMap.get(key);
    }

    public String getMetaInfoField(String key){
        return this.metaInfoMap.get(key);
    }
}
