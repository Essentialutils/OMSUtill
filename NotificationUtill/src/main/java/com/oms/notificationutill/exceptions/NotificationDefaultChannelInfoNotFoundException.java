package com.oms.notificationutill.exceptions;

import android.content.res.Resources.NotFoundException;

import com.oms.notificationutill.NotificationUtill;

public class NotificationDefaultChannelInfoNotFoundException extends NotFoundException {
    public NotificationDefaultChannelInfoNotFoundException(){}
    @Override
    public String getMessage() {
        return "One or more of the next values is missing from string resources: " +
                NotificationUtill.ChannelData.ID+", " +
                NotificationUtill.ChannelData.NAME+" or " +
                NotificationUtill.ChannelData.DESCRIPTION;
    }
}
