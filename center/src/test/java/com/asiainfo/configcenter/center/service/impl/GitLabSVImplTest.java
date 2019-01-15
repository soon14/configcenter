package com.asiainfo.configcenter.center.service.impl;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.merge.Merger;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by oulc on 2018/8/1.
 */
public class GitLabSVImplTest {
    private String filePath = "E:\\asiainfo\\code\\code-asiainfo\\cc-config-file-project";

    @Test
    public void createBranch()throws Exception{
        Git git = Git.open(new File(filePath));
        git.add().call();
    }

}