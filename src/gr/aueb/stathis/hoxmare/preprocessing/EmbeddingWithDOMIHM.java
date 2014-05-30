/*
 * Copyright 2013 Stathis Plitsos, Matthew Damigos, Manolis Gergatsoulis

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 *  http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package gr.aueb.stathis.hoxmare.preprocessing;

import gr.ntua.stasino.hoxmare.preprocessing.ParseXPath;

import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;
import org.xml.sax.InputSource;

public class EmbeddingWithDOMIHM {	//Embedding with DOM for Improved Hox-MaRe

	static ArrayList<String> relations = new ArrayList<String>();
	static ArrayList<String> labels = new ArrayList<String>();
	static Document doc;
	static DocumentTraversal traversal;
	static TreeWalker walker;
	
	private String xmlFilePath;
	private String xPathstr;
	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	private Node root;
	private NodeList nodesL;
	
	public EmbeddingWithDOMIHM(byte[] data, String xPathExpr){
		relations = new ArrayList<String>();
		labels = new ArrayList<String>();
//		this.xPathstr = xPathstr;
		dbFactory = DocumentBuilderFactory.newInstance();
//		parseXPath xpath_in= new parseXPath(this.xPathstr);
//		labels.addAll(xpath_in.returnLabels());
//		relations.addAll(xpath_in.returnArcs());
		try {
			//System.out.print(new String(data));
			dBuilder = dbFactory.newDocumentBuilder();
			String xmlString = new String(data);
			CharArrayReader characterStream = new CharArrayReader((xmlString.trim()).toCharArray());
			InputSource is = new InputSource(characterStream);
//			InputStream s = new ByteArrayInputStream(data);
			doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			root = doc.getDocumentElement();
			
			traversal = (DocumentTraversal)doc;
			
			XPath xPath = XPathFactory.newInstance().newXPath();
            nodesL = (NodeList)xPath.evaluate(xPathExpr,doc.getDocumentElement(), XPathConstants.NODESET);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public EmbeddingWithDOMIHM(byte[] data){
		relations = new ArrayList<String>();
		labels = new ArrayList<String>();
//		this.xPathstr = xPathstr;
		dbFactory = DocumentBuilderFactory.newInstance();
//		parseXPath xpath_in= new parseXPath(this.xPathstr);
//		labels.addAll(xpath_in.returnLabels());
//		relations.addAll(xpath_in.returnArcs());
		try {
			//System.out.print(new String(data));
			dBuilder = dbFactory.newDocumentBuilder();
			String xmlString = new String(data);
			CharArrayReader characterStream = new CharArrayReader((xmlString.trim()).toCharArray());
			InputSource is = new InputSource(characterStream);
//			InputStream s = new ByteArrayInputStream(data);
			doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			root = doc.getDocumentElement();
			
			traversal = (DocumentTraversal)doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public EmbeddingWithDOMIHM(String xmlFilePath, String xPathstr){
		this.xmlFilePath = xmlFilePath;
		this.xPathstr = xPathstr;
		dbFactory = DocumentBuilderFactory.newInstance();
		ParseXPath xpath_in= new ParseXPath(this.xPathstr);
		labels.addAll(xpath_in.returnLabels());
		relations.addAll(xpath_in.returnArcs());
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			File fXmlFile = new File(this.xmlFilePath);
			doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			root = doc.getDocumentElement();
			
			traversal = (DocumentTraversal)doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
//			labels.add("root");
//			relations.add("child");
//			labels.add("*");
//			relations.add("descendant");
//			labels.add("name");
			ParseXPath xpath_in= new ParseXPath();
			labels.addAll(xpath_in.returnLabels());
			relations.addAll(xpath_in.returnArcs());
			
			String xml_file_path= JOptionPane.showInputDialog("Enter the path of the XML file");
			File fXmlFile = new File(xml_file_path);
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			Node root = doc.getDocumentElement();
			
			traversal = (DocumentTraversal)doc;
			
			String output_str="";
			
			System.out.println("Root element :" + root.getNodeName());
			output_str="\n"+"Root element :" + root.getNodeName();
			System.out.println("-----------------------");
			output_str=output_str+"\n"+"-----------------------";
			
			List<List<EmbeddingsNode>> embeddings = new LinkedList<List<EmbeddingsNode>>();
			for (int i=0;i<labels.size();i++){
				LinkedList<EmbeddingsNode> list = new LinkedList<EmbeddingsNode>();
				embeddings.add(list);
			}
			EmbeddingsNode embRoot = new EmbeddingsNode(root,null);
			embeddings.get(0).add(embRoot);
			getEmbeddings3(root,1,embeddings);
			for (int i=0;i<embeddings.size();i++){
				System.out.print(i+" ");
				output_str=output_str+"\n"+i+" ";
				for (int j=0;j<embeddings.get(i).size();j++){
					if (i==0){
						System.out.print(embeddings.get(i).get(j).getNode().getNodeName());
						output_str=output_str+embeddings.get(i).get(j).getNode().getNodeName()+" ";
					}else{
						System.out.print(embeddings.get(i).get(j).getParent().getNodeName()+":"+
							embeddings.get(i).get(j).getNode().getNodeName()+" ");
						output_str=output_str+embeddings.get(i).get(j).getParent().getNodeName()+":"+
							embeddings.get(i).get(j).getNode().getNodeName()+" ";
					}
				}
				System.out.println();
				output_str=output_str+"\n";
			}
			
			
			JOptionPane.showMessageDialog(null, output_str, "Output", JOptionPane.PLAIN_MESSAGE);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	public void getAncestors(EmbeddingsNode n, List<EmbeddingsNode> l, List<List<EmbeddingsNode>> e, int level){
		Node parent = n.getParent();
		if (parent==null){
			return;
		}
		int prevLevel = level-1;
		List<EmbeddingsNode> possibleParents = e.get(prevLevel);
		EmbeddingsNode p = null;
		for (int i=0;i<possibleParents.size();i++){
			if (parent == possibleParents.get(i).getNode()){
				p = possibleParents.get(i);
				break;
			}
		}
		l.add(p);
		getAncestors(p,l,e,prevLevel);
	}
	
	public List<List<EmbeddingsNode>> getEmbeddings(String xPath){
		List<List<EmbeddingsNode>> embeddings = new LinkedList<List<EmbeddingsNode>>();
		ParseXPath xpath_in= new ParseXPath(xPath);
		labels = new ArrayList<String>(xpath_in.returnLabels());
		relations = new ArrayList<String>(xpath_in.returnArcs());
		for (int i=0;i<labels.size();i++){
			LinkedList<EmbeddingsNode> list = new LinkedList<EmbeddingsNode>();
			embeddings.add(list);
		}
		EmbeddingsNode embRoot = new EmbeddingsNode(root,null);
		embeddings.get(0).add(embRoot);
		getEmbeddings3(root,1,embeddings);
		//printEmbeddings(embeddings);
		////////////////////////////formulate embeddings///////////////////////////////
		List<List<EmbeddingsNode>> emb = new LinkedList<List<EmbeddingsNode>>();
		for (int i=0;i<embeddings.get(embeddings.size()-1).size();i++){
			LinkedList<EmbeddingsNode> list = new LinkedList<EmbeddingsNode>();
			getAncestors(embeddings.get(embeddings.size()-1).get(i),list,embeddings,embeddings.size()-1);
			list.addFirst(embeddings.get(embeddings.size()-1).get(i));
			Collections.reverse(list);
			emb.add(list);
		}
		return emb;
	}
	
	public List<List<EmbeddingsNode>> getEmbeddings(){
		List<List<EmbeddingsNode>> embeddings = new LinkedList<List<EmbeddingsNode>>();
		for (int i=0;i<labels.size();i++){
			LinkedList<EmbeddingsNode> list = new LinkedList<EmbeddingsNode>();
			embeddings.add(list);
		}
		EmbeddingsNode embRoot = new EmbeddingsNode(root,null);
		embeddings.get(0).add(embRoot);
		getEmbeddings3(root,1,embeddings);
		
		return embeddings;
	}
	
	public static boolean getEmbeddings3(Node node, int labelIndex, List<List<EmbeddingsNode>> embeddings){
		
		List<Node> descs = new LinkedList<Node>();
		if (labelIndex>=labels.size()){
			//results.add(testNode);
			return true;
		}
		if (relations.get(labelIndex-1).equals("descendant")){
			if (labels.get(labelIndex).equals("*")){
				getDescendants2(node,descs);
			}else{
				getDescendantsWithTag3(node,labels.get(labelIndex),descs);
			}
			
		}else{
			if (labels.get(labelIndex).equals("*")){
				descs = getChildren(node);
			}else {
				descs = getChildrenWithTag(node,labels.get(labelIndex));
			}
		}
		if (descs!=null){
			if (descs.size()!=0){
				int falseCounter = 0;
				for (int j=0;j<descs.size();j++){
					if (getEmbeddings3(descs.get(j),labelIndex+1,embeddings)){
						embeddings.get(labelIndex).add(new EmbeddingsNode(descs.get(j),node));
					}else{
						falseCounter++;
					}
				}
				if (falseCounter==descs.size()){
					return false;
				}else{
					return  true;
				}
			}else{
				return false;
			}
		}
		return false;
	}
	
	
	public static boolean getEmbeddings(Node node, int labelIndex, List<List<Node>> embeddings){
		
		if (labelIndex>=labels.size()){
			return true;
		}

		Node testNode = node.cloneNode(false);
		if (relations.get(labelIndex-1).equals("descendant")){
			List<Node> descs = new LinkedList<Node>();
			if (labels.get(labelIndex).equals("*")){
				getDescendants2(node,descs);
			}else{
				getDescendantsWithTag3(node,labels.get(labelIndex),descs);
			}
			if (descs.size()!=0){
				int falseCounter = 0;
				for (int j=0;j<descs.size();j++){
					if (getEmbeddings(descs.get(j),labelIndex+1,embeddings)){
						embeddings.get(labelIndex).add(descs.get(j));
						testNode.appendChild(descs.get(j).cloneNode(false));					
					}else{
						falseCounter++;
					}
				}
				if (falseCounter==descs.size()){
					return false;
				}else{
					return  true;
				}
			}else{
				return false;
			}
		}else{
			List<Node> children = null;
			if (labels.get(labelIndex).equals("*")){
				children = getChildren(node);
			}else {
				children = getChildrenWithTag(node,labels.get(labelIndex));
			}
			if (children!=null){
				int falseCounter = 0;
				for (int j=0;j<children.size();j++){
					if (getEmbeddings(children.get(j),labelIndex+1,embeddings)){
						embeddings.get(labelIndex).add(children.get(j));
						testNode.appendChild(children.get(j).cloneNode(false));						
					}else{
						falseCounter++;
					}
				}
				if (falseCounter==children.size()){
					return false;
				}else{
					return  true;
				}
			}
			else{
//				int k = embeddings.get(labelIndex-1).size()-1;
//				embeddings.get(labelIndex-1).remove(k);
				return false;
			}
		}
		//return false;
	}
	
	public void printEmbeddings(List<List<EmbeddingsNode>> embeddings){
		for (int i=0;i<embeddings.size();i++){
			System.out.print(i+" ");
			System.out.print("nodes:"+embeddings.get(i).size()+" ");
			for (int j=0;j<embeddings.get(i).size();j++){
				if (i==0){
					System.out.print(embeddings.get(i).get(j).getNode().getAttributes().getNamedItem("iidd").getNodeValue());
				}else{
					System.out.print(embeddings.get(i).get(j).getParent().getAttributes().getNamedItem("iidd").getNodeValue()+":"+
						embeddings.get(i).get(j).getNode().getAttributes().getNamedItem("iidd").getNodeValue()+" ");
				}
			}
			System.out.println();
		}
	}
	
	public void printEmbeddingsFormalized(List<List<EmbeddingsNode>> emb){
		for (int i=0;i<emb.size();i++){
			for (int j=0;j<emb.get(i).size();j++){
				System.out.print(emb.get(i).get(j).getNode().getAttributes().getNamedItem("iidd").getNodeValue()+" ");
			}
			System.out.println();
		}
	}
	
	private static boolean getDescendants2(Node node, List<Node> results){
		
		int whattoshow = NodeFilter.SHOW_ALL;
		NodeFilter nodefilter = new ElementNodeFilter();
		boolean expandreferences = false;
		walker = traversal.createTreeWalker(node, whattoshow, nodefilter, expandreferences);
		Node thisNode = null;
	    thisNode = walker.nextNode();
	    while (thisNode != null) {
	       results.add(thisNode);
	       thisNode = walker.nextNode();
	    }
		return true;
	}
	
	private static boolean getDescendants(Node node, List<Node> results){
		List<Node> children = getChildren(node);
		if (results == null || children==null){
			return false;
		}
		for (int i=0;i<children.size();i++){
			results.add(children.get(i));
			getDescendants(children.get(i), results);
		}
		if (results.size()==0){
			return false;
		}
		
		return true;
	}
	
	private static boolean getDescendantsWithTag3(Node node, String tag, List<Node> results){
		int whattoshow = NodeFilter.SHOW_ALL;
		NodeFilter nodefilter = new ElementNodeFilter();
		boolean expandreferences = false;
		walker = traversal.createTreeWalker(node, whattoshow, nodefilter, expandreferences);
		Node thisNode = walker.nextNode();
	    while (thisNode != null) {
	    	if (thisNode.getNodeName().equals(tag))
	    		results.add(thisNode);
	    	thisNode = walker.nextNode();
	    }
		return true;
	}
	
	private static boolean getDescendantsWithTag2(Node node, String tag, List<Node> descs){
		List<Node> children = getChildren(node);
		if (children==null){
			return false;
		}
		for (int i=0;i<children.size();i++){
			if (children.get(i).getNodeName().equals(tag)){
				descs.add(children.get(i));
			}
			getDescendantsWithTag2(children.get(i),tag, descs);
		}
		return true;
	}
	
	private static List<Node> getDescendantsWithTag(Node node, String tag){
		List<Node> descs = new LinkedList<Node>();
		List<Node> results = new LinkedList<Node>();
		getDescendants(node,descs);
		for (int i=0;i<descs.size();i++){
			if (descs.get(i).getNodeName().equals(tag)){
				results.add(descs.get(i));
			}
		}
		if (results.size()==0){
			return null;
		}
		return results;
	}
	
	private static List<Node> getChildren(Node node){
		NodeList children = node.getChildNodes();
		LinkedList<Node> elementChildren = new LinkedList<Node>();
		for (int i=0;i<children.getLength();i++){
			if (children.item(i).getNodeType()==Node.ELEMENT_NODE)
				elementChildren.add(children.item(i));
		}
		if (elementChildren.size()==0){
			return null;
		}
		return elementChildren;
	}
	
	private static List<Node> getChildrenWithTag(Node node, String tag){
		List<Node> children = getChildren(node);
		List<Node> results = new LinkedList<Node>();
		if (children==null){
			return null;
		}
		for (int i =0;i<children.size();i++){
			if (children.get(i).getNodeName().equals(tag)){
				results.add(children.get(i));
			}
		}
		if (results.size()==0){
			return null;
		}
		return results;
	}
	
	public static Document getDoc() {
		return doc;
	}

	public static void setDoc(Document doc) {
		EmbeddingWithDOMIHM.doc = doc;
	}

	public NodeList getNodesL() {
		return nodesL;
	}

	public void setNodesL(NodeList nodesL) {
		this.nodesL = nodesL;
	}
}