package com.oms.simplechatview.views;

import android.content.Context;
import android.view.View;


public interface ViewBuilderInterface {
    
    MessageView buildRecvView(Context context);

    
    MessageView buildSentView(Context context);

}
