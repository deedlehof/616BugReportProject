package io.jenkins.plugins.sample;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Action;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.Project;
import hudson.util.FormValidation;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import jenkins.model.TransientActionFactory;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.HttpRedirect;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
* HelloWorldAction is responsible for serving
* data to the plugin front-end.
*/
public class RankingAction implements Action {

  private transient FileComparator fc;
  private transient Map<String, Double> matchedFiles;

  private String codeLocation;
  private String report;

  private Project project;

  /**
  * Creates a new HelloWorldAction that runs the
  * file comparison for bug reports.
  *
  */
  public RankingAction(Project project) {
    this.project = project;

    this.codeLocation = "/home/tanner/School/Project/Lang/src/main"; //TODO DELETE
    /*
    this.report = "  NumberUtils.createLong() does not handle hex numbers, but createInteger() handles hex and octal. This seems odd.  NumberUtils.createNumber() assumes that hex numbers can only be Integer. Again, why not handle bigger Hex numbers?  ==  It is trivial to fix createLong() - just use Long.decode() instead of valueOf(). It's not clear why this was not done originally - the decode() method was added to both Integer and Long in Java 1.2.  Fixing createNumber() is also fairly easy - if the hex string has more than 8 digits, use Long.  Should we allow for leading zeros in an Integer? If not, the length check is trivial.";
    this.codeLocation = codeLocation;
    this.report = report;
    */
    fc = new FileComparator("Lang");
    fc.trackDirectory(codeLocation);

    /*
    matchedFiles = fc.compare(report, 5);
    System.err.println("=======TOP MATCHING FILES=========");
    for (Map.Entry<String, Double> entry: matchedFiles.entrySet()) {
      System.out.println(entry.getValue() + " --- " + entry.getKey());
    }
    */
    System.err.println("===========REINIT ACTION============");
  }

  public String getCodeLocation() {
    return codeLocation;
  }

  public Map<String, Double> getMatchedFiles() {
    return matchedFiles;
  }

  public String[] getFileLabels() {
    Set<String> keys = matchedFiles.keySet();
    return keys.toArray(new String[keys.size()]);
  }

  public HttpResponse doCalculateResults(StaplerRequest req) throws IOException, ServletException {
    JSONObject jsonData = req.getSubmittedForm();
    report = jsonData.optString("bugReport");
    matchedFiles = fc.compare(report, 5);
    System.err.println("=======TOP MATCHING FILES=========");
    for (Map.Entry<String, Double> entry: matchedFiles.entrySet()) {
      System.out.println(entry.getValue() + " --- " + entry.getKey());
    }
    return new HttpRedirect("index");
  } 

  @Override
  public String getIconFileName() {
    return "document.png";
  }

  @Override
  public String getDisplayName() {
    return "Bug Ranking";
  }

  @Override
  public String getUrlName() {
    return "RankingAction";
  }

  @Extension
  public static final class RankingFactory extends TransientActionFactory<Project> {

    public static RankingAction ra = null;
    public static Project project = null;
    
    @Override
    public Class<Project> type() {
      return Project.class;
    } 

    // Create action depending on the project type
    @Override
    public Collection<? extends Action> createFor(Project project) {
      if (this.project != project) {
        ra = new RankingAction(project);
	this.project = project;
      } 
      //return Collections.singleton(new RankingAction(project));
      return Collections.singleton(ra);
    } 
  } 

}