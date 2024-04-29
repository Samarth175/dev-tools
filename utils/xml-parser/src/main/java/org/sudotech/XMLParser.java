package org.sudotech;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.crypto.dsig.TransformException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {

    public static Document loadXMLFromFile(String filePath){
        Document document = null;
        try{
            File inputFile = new File(filePath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(inputFile);
            document.getDocumentElement().normalize();
            //parserObj.parseXml(doc);
//            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        }catch(Exception e){
            System.out.println("Exception while reading XML file : " + e);
        }
        return document;
    }

    public static void writeXMLToFile(Document document,String outputFilePath) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(document);

            // Specify your local file path
            StreamResult result = new StreamResult(outputFilePath);
            transformer.transform(source, result);
        }
        catch(Exception e){
            System.out.println("Exception while writing XML Document to File. " + e);
        }
    }

    public static List<DataObject> getXMLFileContentAsDataObjectList(String filePath){
        List<DataObject> dataObjectsFile = new ArrayList<DataObject>();

        Document doc = XMLParser.loadXMLFromFile(filePath);
        doc.getDocumentElement().normalize();

        // Get DataObjects from File
        Element rootElement = (Element) doc.getDocumentElement();
        NodeList nodeList = rootElement.getChildNodes();
        int childNodeCount = nodeList.getLength();
//        System.out.println(childNodeCount);
        for(int i=0; i<childNodeCount; i++){
            if(nodeList.item(i).getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) nodeList.item(i);
                dataObjectsFile.add(new DataObject(element));
            }
        }
        return dataObjectsFile;
    }

    public static Document convertDataObjectListToXML(List<DataObject> list) {
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Create a new Document
            document = builder.newDocument();

            // Create root element
            Element documentRootElement = document.createElement(list.get(0).getMetaInfoField("DataObjectName") + "List");
            document.appendChild(documentRootElement);

            int listSize = list.size();
            for (int i = 0; i < listSize; i++) {
                Element dataObjectRoot = document.createElement(list.get(i).getMetaInfoField("DataObjectName"));
                documentRootElement.appendChild(dataObjectRoot);

                for (String nodeName : list.get(i).getDataObjectMap().keySet()) {
                    Element element = document.createElement(nodeName);
                    element.appendChild(document.createTextNode(list.get(i).getField(nodeName)));
                    dataObjectRoot.appendChild(element);
                }
            }
        }
        catch (Exception e){
            System.out.println("Exception while converting DataObjectList to XML Document. " + e);
        }

        return document;
    }

    public static void writeDataObjectsListToXMLFile(List<DataObject> list,String outputFilePath){
        Document document = XMLParser.convertDataObjectListToXML(list);
        XMLParser.writeXMLToFile(document,outputFilePath);
    }
}
