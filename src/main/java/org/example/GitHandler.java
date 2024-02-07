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
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static org.example.Main.logger;

public class GitHandler {
    private final File localRepoDirFile;
    private final String remoteRepoURL;

    private GitHandler(String localRepoPath, String remoteRepoURL) {
        this.localRepoDirFile = new File(localRepoPath);
        this.remoteRepoURL = remoteRepoURL;
    }

    public GitHandler() {
        this("git-repo/continuous-integration", "git@github.com:DD2480-Group-25/continuous-integration.git");
    }

    public boolean isRepoCloned() {
        boolean result;
        try (Git git = Git.open(localRepoDirFile)) {
            // TODO make sure the repo is valid
            result = true;
        } catch (Exception e) {
            result = false;
            logger.error(e.getMessage());
        }

        return result;
    }

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
                logger.info("deleting: " + file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                logger.info("deleting: " + dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
