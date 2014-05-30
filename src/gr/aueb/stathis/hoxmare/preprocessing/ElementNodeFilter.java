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

import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class ElementNodeFilter implements NodeFilter {
	public short acceptNode(Node thisNode) { 
        if (thisNode.getNodeType() == Node.ELEMENT_NODE) { 
            return NodeFilter.FILTER_ACCEPT; 
        } 
             return NodeFilter.FILTER_SKIP;  
   } 
}
