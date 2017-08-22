package com.aruistar.other

import org.slf4j.Logger
import org.slf4j.LoggerFactory

trait AruisLog {
    private Logger _log


    Logger getLog() {

        if (_log == null)
            _log = LoggerFactory.getLogger(this.class)
        return _log
    }
}
