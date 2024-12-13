package com.whalefall.learncases.ftp;

import jakarta.annotation.PreDestroy;
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
import java.util.Optional;

@Slf4j
@Component
public class VfsService {
    public static final FileSystemOptions FILE_SYSTEM_OPTS;
    public static final int TEN_SECONDS = 10;
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
        String canonicalName = this.getClass().getCanonicalName();
        try {
            MANAGER.init();
            /*if (vfs2Configuration.getBaseUrls().stream().anyMatch(url -> {
                Optional<FileObject> optional = VfsService.doResolveFile(url);
                optional.ifPresent(MANAGER::setBaseFile);
                return optional.isPresent();
            })) {
                log.info("init ftp basefile successfully {}", MANAGER.getBaseFile().getPublicURIString());
            } else {
                log.info("init ftp failed");
            }*/
        } catch (FileSystemException e) {
            log.error("{} init StandardFileSystemManager failed", canonicalName, e);
        }
    }

    public static Optional<FileObject> doResolveFile(final String url) {
        try {
            return Optional.of(MANAGER.resolveFile(url, FILE_SYSTEM_OPTS));
        } catch (FileSystemException e) {
            log.error("connect to ftp failed", e);
            return Optional.empty();
        }
    }

    @SuppressWarnings("unused")
    public String getRemoteFileContentAsGb2312(String uri) {
        try {

            Optional<FileObject> optional = doResolveFile(uri);
            if (optional.isPresent()) {
                InputStream in = optional.get().getContent().getInputStream();
                if (in != null) {
                    return IOUtils.toString(in, "gb2312");
                }
            }

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        throw new UnsupportedOperationException("操作失败");
    }

    @SuppressWarnings("unused")
    public void uploadFile(String localFilePath, String remoteFilePath) {
        try {
            File f = new File(localFilePath);
            FileObject localFile = MANAGER.resolveFile(f.getAbsolutePath());

            Optional<FileObject> optional = doResolveFile(remoteFilePath);
            if (optional.isPresent()) {
                optional.get().copyFrom(localFile, Selectors.SELECT_ALL);
                log.info("successfully upload file {}", remoteFilePath);
            } else {
                log.error("upload file {} failed ", remoteFilePath);
            }

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @PreDestroy
    public void destroy() {
        MANAGER.close();
        log.info("destroy finished");
    }

}
