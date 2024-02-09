package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertFalse(gh.pull("i-do-not-exit"));
        assertTrue(gh.pull("main"));
    }

    @Test
    void testCheckoutToBranch() {
        GitHandler gh = new GitHandler();
        if (!gh.getLocalRepoDirFile().exists()) {
            gh.cloneRepo();
        }

        assertTrue(gh.checkout("12-set-up-build"));
        assertFalse(gh.checkout("i-do-not-exist"));
    }
}
