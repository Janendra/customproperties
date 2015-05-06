package com.github.Janendra;

import com.github.Janendra.utils.ApplicationPropertyUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Mojo(name = "hostInfo", requiresProject = true)
public class HostInfoMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true)
    private MavenProject project;

    public void execute() throws MojoExecutionException {
        try {
            this.project.getProperties().setProperty("hostIP", ApplicationPropertyUtils.getLocalHostLANAddress().getHostAddress());
            this.project.getProperties().setProperty("hostName", InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            getLog().error("Error in fetching host IP.");
        } catch (Exception e) {
            getLog().error("Error: ", e);
        }
    }

}
