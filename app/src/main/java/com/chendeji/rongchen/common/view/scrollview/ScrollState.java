package com.chendeji.rongchen.common.view.scrollview;

/**
 * Created by chendeji on 30/6/15.
 */
public enum ScrollState {

    /**
     * Widget is stopped.
     * This state does not always mean that this widget have never been scrolled.
     */
    STOP,

    /**
     * Widget is scrolled up by swiping it down.
     */
    UP,

    /**
     * Widget is scrolled down by swiping it up.
     */
    DOWN,

}
