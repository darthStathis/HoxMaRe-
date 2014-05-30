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

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Node;

public class EmbeddingsNode{
	private Node node;
	private Node parentNode;
	private List<EmbeddingsNode> children;
	
	public EmbeddingsNode(Node node,Node parentNode){
		this.node = node;
		this.parentNode = parentNode;
	}
	
	public EmbeddingsNode(Node node){
		this.node = node;
		children = new LinkedList<EmbeddingsNode>();
	}
	
	public List<EmbeddingsNode> getChildren(){
		return children;
	}
	
	public void setChildren(List<EmbeddingsNode> children){
		this.children = new LinkedList<EmbeddingsNode>(children);
	}
	
	public Node getNode(){
		return node;
	}
	
	public void setNode(Node node){
		this.node = node;
	}
	
	public Node getParent(){
		return parentNode;
	}
	
	public void setParent(Node node){
		this.parentNode = node;
	}
}
