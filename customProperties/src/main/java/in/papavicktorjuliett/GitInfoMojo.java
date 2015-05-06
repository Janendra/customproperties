package in.papavicktorjuliett;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.log.ScmLogDispatcher;
import org.apache.maven.scm.provider.git.gitexe.command.branch.GitBranchCommand;
import org.apache.maven.scm.provider.git.gitexe.command.changelog.GitChangeLogCommand;
import org.apache.maven.scm.provider.git.repository.GitScmProviderRepository;

import java.io.File;

@Mojo(name = "gitInfo", requiresProject = true)
public class GitInfoMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true)
    private MavenProject project;
    @Parameter(defaultValue = "${project.scm.connection}")
    private String       urlScm;
    @Parameter(defaultValue = "${basedir}")
    private File         scmDirectory;

    public void execute() throws MojoExecutionException {
        try {
            File[] files = scmDirectory.listFiles();

            boolean gotIt = false;
            if (files != null) {
                for (File file : files) {
                    if (file.getName().equals(".git")) {
                        gotIt = true;
                    }
                }
            }
            if (!gotIt)
                this.scmDirectory = this.scmDirectory.getParentFile();
            try {
                GitScmProviderRepository repository = new GitScmProviderRepository(this.urlScm);
                ScmFileSet fileSet = new ScmFileSet(this.scmDirectory);
                GitChangeLogCommand command = new GitChangeLogCommand();
                ScmLogDispatcher logger = new ScmLogDispatcher();
                command.setLogger(logger);
                ChangeLogScmResult result = (ChangeLogScmResult) command.executeCommand(repository, fileSet, new CommandParameters());
                ChangeSet info = result.getChangeLog().getChangeSets().get(0);
                this.project.getProperties().setProperty("lastCommitAuthor", info.getAuthor());
                this.project.getProperties().setProperty("lastCommitTimeStamp", info.getDate().toString());
                this.project.getProperties().setProperty("lastCommitId", info.getRevision());
                this.project.getProperties().setProperty("gitBranch", GitBranchCommand.getCurrentBranch(logger, repository, fileSet));
            } catch (ScmException e) {
                getLog().error("Cannot get the information from the git repository", e);
            }
        } catch (Exception e) {
            getLog().error("Error", e);
        }
    }

}
