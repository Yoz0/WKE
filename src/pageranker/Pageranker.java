package pageranker;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import java.io.IOException;

import java.util.List;

import org.bson.Document;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class Pageranker {

  public static class PagerankMapper
    extends Mapper<Object, Text, Text, DoubleWritable>{

    private Text linkText = new Text();
    private DoubleWritable pageRankToAdd = new DoubleWritable();

    public void map(Object o, Text pageText, Context context)
        throws IOException, InterruptedException {
      String pageName = pageText.toString();
      Document page = pageCollection.find(eq("name",pageName)).first();
      if (page == null){
        System.err.println("Page Not Found :" + pageName);
        return;
      }
      double pageRank = (double) page.get("pagerank");
      List<String> links = (List<String>) page.get("list_links");
      pageRankToAdd.set(pageRank / links.size());
      for (String link : links){
        linkText.set(link);
        context.write(linkText,pageRankToAdd);
      }
    }
  }

  public static class PagerankReducer
    extends Reducer<Text, DoubleWritable, Text, DoubleWritable>{

    static private DoubleWritable pageRank = new DoubleWritable();
    public void reduce(Text pageText, 
      Iterable<DoubleWritable> listPageRankReceived,
      Context context)
       throws IOException, InterruptedException{
      double newPageRank = 0;
      for (DoubleWritable pageRankReceived : listPageRankReceived){
        newPageRank += pageRankReceived.get();
      }
      String pageName = pageText.toString();
      Document page = pageCollection.find(eq("name",pageName)).first();
      if (page == null){
        System.err.println("Page Not Found :" + pageName);
        return;
      }
      double updatedPageRank = dampingValue * newPageRank + (1-dampingValue); 
      pageCollection.updateOne(eq("name",pageName),
          set("pagerank",updatedPageRank));
      pageRank.set(updatedPageRank);
      context.write(pageText,pageRank);
    }
  }

  static protected MongoCollection<Document> pageCollection;
  static protected String listArticles;
  static protected final double dampingValue = (double)0.85;

  static public void setCollection(MongoCollection collection){
    Pageranker.pageCollection = collection;
  }

  static public void setListArticles (String listArticles){
    Pageranker.listArticles = listArticles;
  }

  static public void doPagerank()
      throws Exception {
    if (Pageranker.pageCollection == null || Pageranker.listArticles == null){
      System.err.println("Pageranker was not properly initialised");
      return;
    }
    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.newInstance(conf);

    Job job = Job.getInstance(conf, "pagerank");
    job.setJarByClass(Pageranker.class);
    job.setMapperClass(PagerankMapper.class);
    job.setReducerClass(PagerankReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(DoubleWritable.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(DoubleWritable.class);
    FileInputFormat.addInputPath(job, new Path(listArticles));
    FileOutputFormat.setOutputPath(job, new Path("out"));

    job.waitForCompletion(true);
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    String[] otherArgs = new GenericOptionsParser(conf, args). getRemainingArgs();

    if (otherArgs.length == 0) {
      System.out.println("No arguments found, going with the default");
      MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
      MongoClient mongoClient = new MongoClient(connectionString);
      MongoDatabase database = mongoClient.getDatabase("mydb");
      setCollection(database.getCollection("articles"));
      setListArticles("../../wikipedia-page-id-title.raw");
    }
    else if (otherArgs.length == 4) {
      MongoClientURI connectionString = new MongoClientURI(otherArgs[0]);
      MongoClient mongoClient = new MongoClient(connectionString);
      MongoDatabase database = mongoClient.getDatabase(otherArgs[1]);
      setCollection(database.getCollection(otherArgs[2]));
      setListArticles(otherArgs[3]);
    }
    else {
      System.err.println("Usage: pageranker <mongoURI> <database> <collection>");
      System.exit(2);
    }

    doPagerank();
    System.exit(0);
  }
}
