package org.example;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class GitHandlerTest {

    @Test
    void testIsRepoCloned() {
        GitHandler gh = new GitHandler();
        gh.deleteLocalRepo();
        assertFalse(gh.isRepoCloned());
        gh.cloneRepo();
        assertTrue(gh.isRepoCloned());
    }

    @Test
    void testCloneRepo() {
        GitHandler gh = new GitHandler();
        gh.deleteLocalRepo();
        assertFalse(gh.getLocalRepoDirFile().exists());
        assertFalse(gh.isRepoCloned());
        gh.cloneRepo();
        assertTrue(gh.getLocalRepoDirFile().exists());
        assertTrue(gh.isRepoCloned());
    }

    @Test
    void testDeleteRepo() {
        GitHandler gh = new GitHandler();
        if (!gh.getLocalRepoDirFile().exists()) {
            gh.cloneRepo();
        }

        assertTrue(gh.isRepoCloned());
        gh.deleteLocalRepo();
        assertFalse(gh.isRepoCloned());
    }

    @Test
    void testGitPull() {
        GitHandler gh = new GitHandler();
        if (!gh.getLocalRepoDirFile().exists()) {
            gh.cloneRepo();
        }
        assertFalse(gh.pull("i-do-not-exist"));
        assertTrue(gh.pull("main"));
    }

    @Test
    void testCheckoutToBranch() {
        GitHandler gh = new GitHandler();
        gh.deleteLocalRepo();
        gh.cloneRepo();


        assertTrue(gh.fetch("dummy-branch-for-testing"));
        assertTrue(gh.checkout("dummy-branch-for-testing"));
        assertFalse(gh.checkout("i-do-not-exist"));
    }

    @Test
    void testListBranches() throws GitAPIException, IOException {
        GitHandler gh = new GitHandler();
        if (!gh.getLocalRepoDirFile().exists()) {
            gh.cloneRepo();
        }

        assertTrue(gh.getBranchNames().contains("refs/heads/main"));
    }

    @Test
    void testGetBranchName() {
        GitHandler gh = new GitHandler();
        if (!gh.getLocalRepoDirFile().exists()) {
            gh.cloneRepo();
        }

        gh.checkout("main");
        assertEquals(gh.getCurrentBranch(), "main");
        gh.checkout("dummy-branch-for-testing");
        assertEquals(gh.getCurrentBranch(), "dummy-branch-for-testing");
    }
}
