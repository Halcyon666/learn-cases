package com.whalefall.learncases.ftp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
@SuppressWarnings("unused")
public class VfsService {
    public static final FileSystemOptions FILE_SYSTEM_OPTS;
    public static final int TEN_SECONDS = 10;
    private final Vfs2Configuration vfs2Configuration;
    private static final StandardFileSystemManager MANAGER;

    static {
        FILE_SYSTEM_OPTS = new FileSystemOptions();
        FtpFileSystemConfigBuilder instance = FtpFileSystemConfigBuilder.getInstance();
        instance.setUserDirIsRoot(FILE_SYSTEM_OPTS, true);
        instance.setDataTimeout(FILE_SYSTEM_OPTS, Duration.ofSeconds(TEN_SECONDS));
        instance.setPassiveMode(FILE_SYSTEM_OPTS, true);

        MANAGER = new StandardFileSystemManager();
    }

    @SuppressWarnings("all")
    VfsService(Vfs2Configuration vfs2Configuration) {
        this.vfs2Configuration = vfs2Configuration;
    }

    public static Optional<FileObject> doResolveFile(final String absoluteUri) {
        try {
            return Optional.of(MANAGER.resolveFile(absoluteUri, FILE_SYSTEM_OPTS));
        } catch (FileSystemException e) {
            log.error("connect to ftp failed", e);
            return Optional.empty();
        }
    }

    private static void doCloseAllResources(FileObject... fileObject) {
        MANAGER.close();
        if (fileObject != null) {
            Arrays.stream(fileObject).forEach(VfsService::close);
        }
    }

    private static void close(FileObject fileObject) {
        try {
            fileObject.close();
        } catch (FileSystemException e) {
            log.error("close fileObject failed", e);
        }
    }

    @SuppressWarnings("unused")
    public String getRemoteFileContentAsGb2312(String uri) {
        FileObject fileObject = null;
        try {
            MANAGER.init();
            for (String baseUrl : vfs2Configuration.getBaseUrls()) {
                Optional<FileObject> optional = doResolveFile(vfs2Configuration.getBaseUrl1() + uri);
                if (optional.isPresent()) {
                    fileObject = optional.get();
                    InputStream in = fileObject.getContent().getInputStream();
                    if (in != null) {
                        return IOUtils.toString(in, "gb2312");
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            doCloseAllResources(fileObject);
        }
        throw new UnsupportedOperationException("操作失败");
    }

    @SuppressWarnings("unused")
    public void uploadFile(String localFilePath, String remoteFilePath) {
        FileObject fileObject = null;
        FileObject localFile = null;
        try {
            MANAGER.init();
            File f = new File(localFilePath);
            localFile = MANAGER.resolveFile(f.getAbsolutePath());
            for (String baseUrl : vfs2Configuration.getBaseUrls()) {
                Optional<FileObject> optional = doResolveFile(remoteFilePath);
                if (optional.isPresent()) {
                    fileObject = optional.get();
                    fileObject.copyFrom(localFile, Selectors.SELECT_ALL);
                    log.info("successfully upload file {}", remoteFilePath);
                } else {
                    log.error("upload file {} failed ", remoteFilePath);
                }
            }

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            doCloseAllResources(fileObject, localFile);
        }
    }

}
