package org.example;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.example.Main.logger;

public class GitHandler {
    private final File localRepoDirFile;
    private final String remoteRepoURL;

    /**
     * Constructor for the GitHandler Class
     * @param localRepoPath the local path where the repo will be cloned
     * @param remoteRepoURL the url of the repo
     */
    public GitHandler(String localRepoPath, String remoteRepoURL) {
        this.localRepoDirFile = new File(localRepoPath);
        this.remoteRepoURL = remoteRepoURL;
    }

    /**
     * Default constructor for the GitHandler Class used for testing
     */
    public GitHandler() {
        this("git-repo/continuous-integration", "git@github.com:DD2480-Group-25/continuous-integration.git");
    }

    /**
     * Getter for the repo File
     */
    public File getLocalRepoDirFile() {
        return localRepoDirFile;
    }

    /**
     * Returns true if the repo is present locally
     */
    public boolean isRepoCloned() {
        boolean result;
        try (Git git = Git.open(localRepoDirFile)) {
            result = true; // We assume that a clone operation won't be interrupted
        } catch (Exception e) {
            result = false;
            logger.error(e.getMessage());
        }

        return result;
    }

    /**
     * Clones the remote repo locally
     * @param remotePath the url of the repo
     * @param localPath the local path to the folder where the repo will be cloned
     */
    private void cloneRepo(String remotePath, File localPath) {
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

    /**
     * Clones the remote repo locally
     */
    public void cloneRepo() {
        cloneRepo(remoteRepoURL, localRepoDirFile);
    }

    /**
     * Deletes the local files of the repository if they are present
     */
    public void deleteLocalRepo() {
        try {
            if(localRepoDirFile.exists()){
                delete(localRepoDirFile.toPath());
            }
        } catch (IOException e) {
            logger.info("Error while trying to delete the local repo files");
        }
    }

    /**
     * Deletes a directory and its content recursively
     * @param path the path of the directory to be deleted
     */
    public static void delete(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Pulls changes on the specified branch
     * @param localRepoPath the location of the local repo
     * @param branch the branch to fetch changes from
     * @return true if the pull was successful
     */
    private boolean pull(File localRepoPath, String branch) {
        boolean pullSuccessful = false;

        logger.info(String.format("Trying to pull changes from branch %s", branch));

        try (Git git = Git.open(localRepoPath)) {
            PullResult result = git.pull()
                    .setRemoteBranchName(branch)
                    .setRebase(false)
                    .call();

            pullSuccessful = result.isSuccessful();
        } catch (Exception e) {
            logger.error(String.format("Error while running git pull on branch %s", branch));
            logger.error(e.getMessage());
        }

        if (pullSuccessful) {
            logger.info("Pull was successful");
        }

        return pullSuccessful;
    }

    /**
     * Pulls changes on the specified branch
     * @return true if the pull was successful
     */
    public boolean pull(String branch) {
        return pull(localRepoDirFile, branch);
    }

    /**
     * Checks out the specified branch
     * @param repositoryLocalPath the path to the local repo
     * @param branch the branch to checkout
     * @return true if the checkout was successful
     */
    private boolean checkout(File repositoryLocalPath, String branch) {
        boolean actionCompleted = false;

        logger.info(String.format("Trying to checkout on branch %s", branch));

        try (Git git = Git.open(repositoryLocalPath)) {
            if (getBranchNames().contains("refs/heads/" + branch)) {
                git
                .checkout()
                .setCreateBranch(false)
                .setName(branch)
                .call();
            } else {
                git
                .checkout()
                .setCreateBranch(true)
                .setName(branch)
                .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.SET_UPSTREAM)
                .setStartPoint("origin/" + branch).call();
            }

            logger.info(String.format("Checked out successfully on branch %s", getCurrentBranch()));
            actionCompleted = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return actionCompleted;
    }

    /**
     * Checks out the specified branch
     * @param branch the branch to checkout
     * @return true if successful
     */
    public boolean checkout(String branch) {
        return checkout(localRepoDirFile, branch);
    }

    /**
     * Allows to list the branch names
     * @return the list of the branches available in the local repo
     */
    public List<String> getBranchNames() throws GitAPIException, IOException {
        try (Git git = Git.open(localRepoDirFile)) {
            List<String> names = new ArrayList<>();
            List <Ref> branches = git.branchList().call();

            for (Ref b : branches) {
                names.add(b.getName());
            }

            return names;
        } catch (Exception e) {
            logger.error(e.toString());
            return new ArrayList<>(Collections.singleton(""));
        }

    }

    /**
     * Fetches the changes on the specified branch
     * @param branch the branch we are interested in
     * @return true if successful
     */
    public boolean fetch(String branch) {
        boolean actionCompleted = false;

        try (Git git = Git.open(localRepoDirFile)) {
            git.fetch()
                    .setRemote("origin")
                    .setRefSpecs("+refs/heads/" + branch + ":refs/remotes/origin/" + branch)
                    .call();

            actionCompleted = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return actionCompleted;
    }

    /**
     * Getter for the current branch as a string
     * @return the current branch as a string
     */
    public String getCurrentBranch() {
        try (Git git = Git.open(localRepoDirFile)) {
            Repository rep = git.getRepository();
            return rep.getBranch();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "error";
        }
    }
}
