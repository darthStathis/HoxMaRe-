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

package gr.aueb.stathis.hoxmare.algorithm;

import gr.aueb.stathis.hoxmare.algorithm.prerequisites.JobBuilder;
import gr.aueb.stathis.hoxmare.algorithm.prerequisites.WholeFileInputFormat;
import gr.aueb.stathis.hoxmare.preprocessing.EmbeddingWithDOM;
import gr.aueb.stathis.hoxmare.preprocessing.EmbeddingsNode;
import gr.aueb.stathis.xPathQuery.XPathQuery;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class HoxMare extends Configured implements Tool{

	public static class Map extends Mapper<NullWritable, BytesWritable, Text, Text> {
		
		private Text filenameKey = null;
		private String xPathQuery = null;
		private int outputNode = -1;
		private String branchingNodes = null;
		private int dbnode = -1;
		
		@Override
		protected void setup(Context context) throws IOException,InterruptedException {
			InputSplit split = context.getInputSplit();
			Path path = ((FileSplit) split).getPath();
			filenameKey = new Text(path.toString());
			xPathQuery = context.getConfiguration().get("xPath.query");
			outputNode = Integer.parseInt(context.getConfiguration().get("output.node"));
			branchingNodes = context.getConfiguration().get("branching.nodes");
			dbnode = Integer.parseInt(context.getConfiguration().get("deepest.branching.node"));
		}
		
		@Override
		protected void map(NullWritable key, BytesWritable value, Context context)throws 
			IOException, InterruptedException {
			byte[] data = value.getBytes();
			XPathQuery xpQuery = new XPathQuery(xPathQuery,branchingNodes);
			List<String> dq = xpQuery.getDecomposition();
			List<String> nb = xpQuery.getBranchingNodes();
			int[][] maps = xpQuery.getbranchingNodesMapping();
			EmbeddingWithDOM ewd = new EmbeddingWithDOM(data);
			for (int i=0;i<dq.size();i++){
				List<List<EmbeddingsNode>> embeddings = ewd.getEmbeddings(dq.get(i));

				for (int j=0;j<embeddings.size();j++){
					String[] ab = new String[nb.size()];
					initializeArray(ab,"*");
					for (int l=0;l<nb.size();l++){
						if (maps[l][i]>0){
							ab[l] = embeddings.get(j).get(maps[l][i]-1).getNode().getAttributes().getNamedItem("iidd").getNodeValue();
						}
					}

					//Check if there is an embedding
					if(embeddings.size()>0){
						
						Text mapkey = new Text(embeddings.get(j).get(maps[dbnode][i]-1).getNode().getAttributes().getNamedItem("iidd").getNodeValue());
						String valueStr = concatenateAb(ab)+";"+i+";";//Mat--dq.get(i)+";";
						Text mapValue = null;
						if (i==0){
							valueStr+=embeddings.get(j).get(outputNode).getNode().getAttributes()
							.getNamedItem("iidd").getNodeValue();
							mapValue = new Text(valueStr);
						}else{
							valueStr+="true";
							mapValue = new Text(valueStr);
						}
						context.write(mapkey, mapValue);
					}
				}
			}
			
		}
		
		private String concatenateAb(String[] ab){
			StringBuffer sb = new StringBuffer();
			for (int j=0;j<ab.length;j++){
				sb.append(ab[j]+",");
			}
			sb.deleteCharAt(sb.length()-1);
			return sb.toString();
		}
		
		private void initializeArray(String[] ab, String s){
			for (int i=0;i<ab.length;i++){
				ab[i] = new String(s);
			}
		}
   }

	public static class Reduce extends Reducer<Text, Text, Text, Text> {
		private String xPathQuery = null;
		@Override
		protected void setup(Context context) throws IOException,InterruptedException {
			xPathQuery = context.getConfiguration().get("xPath.query");
		}
		
		public void reduce(Text key, Iterable<Text> values, Context context) throws 
		IOException, InterruptedException {
			XPathQuery xpQuery = new XPathQuery(xPathQuery);
			List<String> dq = xpQuery.getDecomposition();
			//Number of Paths in Decomposition
			int NoPiD=dq.size();
			
			Iterable<Text> kra = values;
			Hashtable<String,List<String>> buckets = getBuckets(kra);
			//--------------------------------------------------------
			//System.out.println("-------");
			//if(buckets.size()<3){
			//	return;
			//}
			//System.out.println(buckets.toString());
			//System.out.println("........");
			//--------------------------------------------------------			
			String selectionPath = "0";//Mat--dq.get(0);
			//System.out.println(selectionPath);
			List<String> results = buckets.get(selectionPath);
			
			//Mat--Enumeration<String> keys = buckets.keys();
			//Mat--List<String> keyList = new LinkedList<String>();
			//Mat--do{
				//Mat--String str = keys.nextElement();
//				System.out.println(str);
				//Mat--if (!str.equals(selectionPath)){
				//Mat--keyList.add(str);
				//Mat--}
			//Mat--}while (keys.hasMoreElements());
			//--------------------------------------
			//Mat--System.out.println(keyList.size());
			//--------------------------------------
			if (buckets.size()==NoPiD){ //Mat--if (keyList.size()>0){
				//---------------------------------------
				//System.out.println(results.size());
				//---------------------------------------
				for (int i=0;i<results.size();i++){
					String array[] = results.get(i).split(";");
//					String arrayTab[] = array[0].split(",");
					List<String> checklist = new LinkedList<String>();
					int indexArray[] = new int[dq.size()];//Mat--keyList.size()];
					for (int j=0;j<indexArray.length;j++){
						indexArray[j] = 0;
					}
					checklist.add(array[0]);
					//---------------------------------------
					//System.out.println(results.get(i));
					//---------------------------------------
					for (int j=1;j<NoPiD;j++){//Mat--keyList.size();j++){
						List<String> strs = buckets.get(Integer.toString(j));//dq.get(j));//Mat--keyList.get(j));
						for (int k=indexArray[j];k<strs.size();k++){
							String str[] = strs.get(k).split(";");
							if (isUnifiable(checklist,str[0])){
								//--------------------------------------
								//System.out.println("OK");
								//--------------------------------------
								indexArray[j] = k;
								checklist.add(str[0]);
								break;
							}else if (k==strs.size()-1){
								if (j>1){//Mat--(j-2)>-1){
									k = indexArray[j-1];
									j-=2;
									checklist.remove(checklist.size()-1);
								}
							}
						}
						
					}
					if (checklist.size()==NoPiD){//dq.size()+1){//Mat--keyList.size()+1){
						//---------------------------------------
						//String str_test=array[1];//+ "  EMBED: \t";
						/*for (String s : checklist)
						{
							str_test += s + "\t";
						}*/
						//---------------------------------------
						//System.out.println(str_test);
						//System.out.println("-------");
						//---------------------------------------
//						Text value = new Text(array[1]);//Mat--str_test);//array[1]);
//						context.write(key, value);
					}

				}
			}
		}
		
		private boolean isUnifiable(List<String> checklist, String s){
			boolean flags[] = new boolean[checklist.size()];
			boolean result = true;
			for (int i=0;i<checklist.size();i++){
				String arr1[] = checklist.get(i).split(",");
				String arr2[] = s.split(",");
				result = result && checkArrays(arr1,arr2);
			}
			return result;
		}
		
		private boolean checkArrays(String[] arr1, String[] arr2){
			for (int i=0;i<arr1.length;i++){
				if (!arr1[i].equals(arr2[i])){
					if (!arr1[i].equals("*") && !arr2[i].equals("*")){
						return false;
					}
				}
			}
			return true;
		}
		
		private Hashtable<String,List<String>> getBuckets(Iterable<Text> values){
			Hashtable<String,List<String>> buckets = new Hashtable<String,List<String>>();
			List<String> list = null;
			for (Text val : values) {
				String[] str = val.toString().split(";");
				if (buckets.containsKey(str[1])){
					list = buckets.get(str[1]);
					list.add(str[0]+";"+str[2]);
				}else {
					list = new LinkedList<String>();
					list.add(str[0]+";"+str[2]);
					buckets.put(str[1], list);
				}
			}
			return buckets;
		}
		
	}

   @Override
	public int run(String[] args) throws Exception {
		Job job = JobBuilder.parseInputAndOutput(this, getConf(), args);
		if (job == null) {
			return -1;
		}
		job.setInputFormatClass(WholeFileInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		return job.waitForCompletion(true) ? 0 : 1;
	}
   
   public static void main(String[] args) throws Exception {
	   int res = ToolRunner	.run(new Configuration(), new HoxMare(), args);

	   System.exit(res);
   }
	
   private static boolean deleteDir(File dir){
	   if (dir.isDirectory()) {
           String[] children = dir.list();
           for (int i=0; i<children.length; i++) {
               boolean success = deleteDir(new File(dir, children[i]));
               if (!success) {
                   return false;
               }
           }
       }
	   return dir.delete();
   }
   
   
   
   
   
   
   
   
   
   
   
   
   
   
}
