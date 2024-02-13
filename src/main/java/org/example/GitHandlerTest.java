package org.example;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class GitHandlerTest {

    @Test
    public void testIsRepoCloned() {
        GitHandler gh = new GitHandler();
        gh.deleteLocalRepo();
        assertFalse(gh.isRepoCloned());
        gh.cloneRepo();
        assertTrue(gh.isRepoCloned());
    }

    @Test
    public void testCloneRepo() {
        GitHandler gh = new GitHandler();
        gh.deleteLocalRepo();
        assertFalse(gh.getLocalRepoDirFile().exists());
        assertFalse(gh.isRepoCloned());
        gh.cloneRepo();
        assertTrue(gh.getLocalRepoDirFile().exists());
        assertTrue(gh.isRepoCloned());
    }

    @Test
    public void testDeleteRepo() {
        GitHandler gh = new GitHandler();
        if (!gh.getLocalRepoDirFile().exists()) {
            gh.cloneRepo();
        }

        assertTrue(gh.isRepoCloned());
        gh.deleteLocalRepo();
        assertFalse(gh.isRepoCloned());
    }

    @Test
    public void testGitPull() {
        GitHandler gh = new GitHandler();
        if (!gh.getLocalRepoDirFile().exists()) {
            gh.cloneRepo();
        }
        assertFalse(gh.pull("i-do-not-exist"));
        assertTrue(gh.pull("main"));
    }

    @Test
    public void testCheckoutToBranch() {
        GitHandler gh = new GitHandler();
        gh.deleteLocalRepo();
        gh.cloneRepo();


        assertTrue(gh.fetch("dummy-branch-for-testing"));
        assertTrue(gh.checkout("dummy-branch-for-testing"));
        assertFalse(gh.checkout("i-do-not-exist"));
    }

    @Test
    public void testListBranches() throws GitAPIException, IOException {
        GitHandler gh = new GitHandler();
        if (!gh.getLocalRepoDirFile().exists()) {
            gh.cloneRepo();
        }

        assertTrue(gh.getBranchNames().contains("refs/heads/main"));
    }

    @Test
    public void testGetBranchName() {
        GitHandler gh = new GitHandler();
        if (!gh.getLocalRepoDirFile().exists()) {
            gh.cloneRepo();
        }

        gh.checkout("main");
        assertEquals("main", gh.getCurrentBranch());
        gh.checkout("dummy-branch-for-testing");
        assertEquals("dummy-branch-for-testing", gh.getCurrentBranch());
    }
}
