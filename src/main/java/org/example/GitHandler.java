package org.example;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

import static org.example.Main.logger;

public class GitHandler {
    private final String localRepoPath;
    private final File localRepoDirFile;
    private final String remoteRepoURL;

    private GitHandler(String localRepoPath, String remoteRepoURL) {
        this.localRepoPath = localRepoPath;
        this.localRepoDirFile = new File(localRepoPath);
        this.remoteRepoURL = remoteRepoURL;
    }

    public GitHandler() {
        this("git-repo/continuous-integration", "git@github.com:DD2480-Group-25/continuous-integration.git");
    }

    public void cloneRepo(String remotePath, File localPath) {
        try {
            CloneCommand cloneCommand = new CloneCommand();
            cloneCommand.setURI(remotePath);
            cloneCommand.setDirectory(localPath);
            cloneCommand.setBranch("main");
            cloneCommand.call();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void cloneRepo() {
        cloneRepo(remoteRepoURL, localRepoDirFile);
    }
}
