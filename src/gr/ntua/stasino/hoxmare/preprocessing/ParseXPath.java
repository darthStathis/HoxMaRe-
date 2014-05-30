package gr.ntua.stasino.hoxmare.preprocessing;
import java.util.ArrayList;
import java.util.Collections; 
import javax.swing.JOptionPane; 

public class ParseXPath {
	String path="";
	ArrayList<String> labels = new ArrayList<String>();
	ArrayList<String> relations = new ArrayList<String>();
	
	public ParseXPath(String path){
		//path=  JOptionPane.showInputDialog ( "Enter the XPath expression" );
		//path="*//*/keyword";
		
		String[] tempstr_desc=path.split("//");
		
		for(int i=0;i<tempstr_desc.length-1;i++){
			relations.add("descendant");
		}
		
		int j=0;
		
		for(int i=0;i<tempstr_desc.length;i++){
			String[] tempstr_child=tempstr_desc[i].split("/");
			for(int k=0;k<tempstr_child.length-1;k++){
				relations.add(j,"child");
			}
			j=j+tempstr_child.length; //-1 +1 =0
			
			Collections.addAll(labels, tempstr_child); 
		}
	} 
	
public ParseXPath(){
	path=  JOptionPane.showInputDialog ( "Enter the XPath expression" );
	//path="*//*/keyword";
	
	String[] tempstr_desc=path.split("//");
	
	for(int i=0;i<tempstr_desc.length-1;i++){
		relations.add("descendant");
	}
	
	int j=0;
	
	for(int i=0;i<tempstr_desc.length;i++){
		String[] tempstr_child=tempstr_desc[i].split("/");
		for(int k=0;k<tempstr_child.length-1;k++){
			relations.add(j,"child");
		}
		j=j+tempstr_child.length; //-1 +1 =0
		
		Collections.addAll(labels, tempstr_child); 
	}
}

public ArrayList<String> returnLabels(){
	return labels;
}

public ArrayList<String> returnArcs(){
	return relations;
}

public void printPath(){

	for(int i=0;i<labels.size()-1;i++){
		System.out.println(labels.get(i)+" ");
		System.out.println(relations.get(i)+" ");
	}
	System.out.println(labels.get(labels.size()-1));
}
	
	
}
