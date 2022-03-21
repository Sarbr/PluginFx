package com.sarbr.plugin.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginException extends RuntimeException {

    public static Logger logger = LoggerFactory.getLogger(PluginException.class);

    public PluginException(String s) {
        super(s);
        logger.error(s);
    }
}
