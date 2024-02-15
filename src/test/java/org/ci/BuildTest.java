package org.ci;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

class BuildTest {

    @Test
    void testRunGradleBuild_Success() {
        Build build = new Build();
        GitHandler gh = new GitHandler("src/test/CI-build-test-Pass", "git@github.com:annelibogren/CI-build-test-Pass.git");

        if (!gh.isRepoCloned()) {
            gh.cloneRepo();
        }

        gh.pull("main");

        File projectDir = gh.getLocalRepoDirFile();
        Build.BuildResult result = build.runGradleBuild(projectDir);

        assertTrue(result.isSuccess());
    }

    @Test
    void testRunGradleBuild_Failure() {
        Build build = new Build();
        GitHandler gh = new GitHandler("src/test/CI-build-test-Fail", "git@github.com:annelibogren/CI-build-test-Fail.git");

        if (!gh.isRepoCloned()) {
            gh.cloneRepo();
        }

        gh.pull("main");

        File projectDir = gh.getLocalRepoDirFile();
        Build.BuildResult result = build.runGradleBuild(projectDir);

        assertFalse(result.isSuccess());
        assertNotNull(result.getOutput());
    }
}