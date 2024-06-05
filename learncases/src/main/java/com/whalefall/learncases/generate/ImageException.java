package com.whalefall.learncases.generate;

/**
 * @author Halcyon
 * @date 2024/6/5 21:37
 * @since 1.0.0
 */
public class ImageException extends RuntimeException {
    public ImageException(Exception e) {
        super(e);
    }

    public ImageException(String message) {
        super(message);
    }
}
