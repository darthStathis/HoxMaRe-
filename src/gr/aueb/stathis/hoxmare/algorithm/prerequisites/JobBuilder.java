package gr.aueb.stathis.hoxmare.algorithm.prerequisites;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;

public class JobBuilder {
	  
	  private final Class<?> driverClass;
	  private final JobConf conf;
	  private final int extraArgCount;
	  private final String extrArgsUsage;
	  
	  private String[] extraArgs;
	  
	  public JobBuilder(Class<?> driverClass) {
	    this(driverClass, 0, "");
	  }
	  
	  public JobBuilder(Class<?> driverClass, int extraArgCount, String extrArgsUsage) {
	    this.driverClass = driverClass;
	    this.extraArgCount = extraArgCount;
	    this.conf = new JobConf(driverClass);
	    this.extrArgsUsage = extrArgsUsage;
	  }

	  public static Job parseInputAndOutput(Tool tool, Configuration conf,
		  String[] args) throws IOException {
//		  if (args.length < 5 || args.length>7) {
//			  printUsage(tool, "<input> <XPathQuery> <outputNodeMap> <branchingNodesMap> " +
//			  		"<deepestBranchingNode> [OPTIONAL <borderElementsFile>]");
//			  return null;
//		  }
		  Job job = new Job(conf);
		  job.setJarByClass(tool.getClass());
//		  FileInputFormat.addInputPath(job, new Path(job.getConfiguration().get("input")));
		  FileInputFormat.addInputPath(job, new Path(args[0]));
		  job.getConfiguration().set("xPath.query", args[1]);
		  job.getConfiguration().set("output.node", args[2]);		  
		  job.getConfiguration().set("branching.nodes", args[3]);
		  job.getConfiguration().set("deepest.branching.node", args[4]);
		  if (args.length==7){
			  job.getConfiguration().set("border.elements", args[5]);
			  job.getConfiguration().set("full.xPath.query", args[6]);
		  }
		  FileOutputFormat.setOutputPath(job, new Path("output"));
		  return job;
	}

	  public static void printUsage(Tool tool, String extraArgsUsage) {
	    System.err.printf("Usage: %s [genericOptions] %s\n\n",
	        tool.getClass().getSimpleName(), extraArgsUsage);
	    GenericOptionsParser.printGenericCommandUsage(System.err);
	  }
	  // ^^ JobBuilder
	  
	  
	  public JobConf build() {
	    return conf;
	  }
	  
	  public String[] getExtraArgs() {
	    return extraArgs;
	  }
	}
