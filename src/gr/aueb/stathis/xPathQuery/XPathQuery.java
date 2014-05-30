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

package gr.aueb.stathis.xPathQuery;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class XPathQuery {
	
	private String xpathQuery;
	private String branchingNodes;
	
	public XPathQuery(String x){
		this.xpathQuery = new String(x);
	}
	
	public XPathQuery(String x, String y){
		this.xpathQuery = new String(x);
		this.branchingNodes = new String(y);
	}
	
	public int[][] getbranchingNodesMapping(){
		String[] strs = xpathQuery.split(";");
		int paths = strs.length;
		strs = branchingNodes.split(";");
		int bNodes = strs.length;
		int[][] table = new int[bNodes][paths];
		for (int i=0;i<bNodes;i++){
			String[] brStrs = strs[i].split(":");
			String[] maps = brStrs[1].split(",");
			for (int j=0;j<paths;j++){
				table[i][j] = Integer.parseInt(maps[j]);
			}
		}
//		for (int i=0;i<bNodes;i++){
//			for (int j=0;j<paths;j++){
//				System.out.print(table[i][j]+" ");
//			}
//			System.out.println();
//		}
		return table;
	}
	
	public List<String> getDecomposition(){
		List<String> list = new LinkedList<String>();
		StringTokenizer st = new StringTokenizer(xpathQuery,";");
		while (st.hasMoreTokens()){
			list.add(st.nextToken());
		}
		return list;
	}
	
	public List<String> getBranchingNodes(){
		List<String> list = new LinkedList<String>();
		String[] array = branchingNodes.split(";");
		for (int i=0;i<array.length;i++){
			String[] strs = array[i].split(":");
			list.add(strs[0]);
		}
		return list;
	}
}
