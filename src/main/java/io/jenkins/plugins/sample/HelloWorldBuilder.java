package io.jenkins.plugins.sample;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import java.io.IOException;
import javax.servlet.ServletException;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

public class HelloWorldBuilder extends Builder implements SimpleBuildStep {

  private String codeLocation;
  private String bugReport;

  @DataBoundConstructor
  public HelloWorldBuilder(String codeLocation, String bugReport) {
    this.codeLocation = codeLocation;
    this.bugReport = bugReport;
  }

  public String getCodeLocation() {
    return codeLocation;
  }

  public String getBugReport() {
    return bugReport;
  }

  @DataBoundSetter
  public void setCodeLocation(String codeLocation) {
    this.codeLocation = codeLocation;
  }

  @DataBoundSetter
  public void setBugReport(String bugReport) {
    this.bugReport = bugReport;
  }

  @Override
  public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener)
      throws InterruptedException, IOException {
    run.addAction(new HelloWorldAction(codeLocation, bugReport));
  }

  @Symbol("greet")
  @Extension
  public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

    public FormValidation doCheckName(
        @QueryParameter String codeLocation, @QueryParameter String bugReport)
        throws IOException, ServletException {
      return FormValidation.ok();
    }

    @Override
    public boolean isApplicable(Class<? extends AbstractProject> testClass) {
      return true;
    }

    @Override
    public String getDisplayName() {
      return "Bug Report";
    }
  }
}
