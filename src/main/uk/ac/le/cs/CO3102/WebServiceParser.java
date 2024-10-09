package main.uk.ac.le.cs.CO3102;

import org.apache.xerces.parsers.DOMParser;
//import org.apache.xerces.parsers.SAXParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class WebServiceParser {
	
	public static void main(String[] args)
    {
	  try{
	  	   
		  //DOM
		    DOMParser parser = new DOMParser();
			parser.parse(args[0]);
		  	Document doc = parser.getDocument();
			NodeList nodeList = doc.getElementsByTagName("*");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					// do something with the current element
					System.out.println(node.getNodeName());
				}
			}
		  	System.out.println(doc.getDocumentElement().getNodeName());
			String jsonResult = "";
		  	jsonResult = traverse_tree(doc, jsonResult);
	  		
	  	   //or SAX
			//WebServiceSAX SAXHandler = new WebServiceSAX();
	 		//SAXParser parser = new SAXParser();
	 		//parser.setContentHandler(SAXHandler);
	 		//parser.setErrorHandler(SAXHandler);
	 		//parser.parse(args);	
	  		
	       }
	       catch(Exception e){
	          e.printStackTrace(System.err);
	       }
    }

	public static String traverse_tree(Node node, String current) {
		//Task 3
		String result = "";

		if(node.getFirstChild() != null) {
			Node currentNode = node.getFirstChild();
			HashMap<String, List<String>> returnStringMap = new HashMap<>();
			while(currentNode.getNextSibling() != null) {
                returnStringMap.computeIfAbsent(currentNode.getNodeName(), k -> new ArrayList<>());
				returnStringMap.get(currentNode.getNodeName())
						.add(traverse_tree(currentNode.getNextSibling(), result));
			}

			for(String key : returnStringMap.keySet()) {
				if(returnStringMap.get(key).size() > 1) {
					result += String.format("\"%s\": [ \n", key );
					for(int i = 0; i < returnStringMap.get(key).size(); i++) {
						result += i != returnStringMap.get(key).size()-1
								? String.format("\t%s,\n", returnStringMap.get(key).get(i))
								: String.format("\t%s\n", returnStringMap.get(key).get(i));

					}
					result += "]\n";
				} else {
					result += String.format("\"%s\": { \n", key );
					for(int i = 0; i < returnStringMap.get(key).size(); i++) {
						result += i != returnStringMap.get(key).size()-1
								? String.format("\t%s,\n", returnStringMap.get(key).get(i))
								: String.format("\t%s\n", returnStringMap.get(key).get(i));
					}
					result += "}\n";
				}
			}
		} else {

		}

		return result;
	}

}
