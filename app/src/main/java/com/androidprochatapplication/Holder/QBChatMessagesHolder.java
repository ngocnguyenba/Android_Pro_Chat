package com.androidprochatapplication.Holder;

import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by NgocNguyenBa on 30/06/2017.
 */

public class QBChatMessagesHolder {
    private static QBChatMessagesHolder instance;
    private HashMap<String, ArrayList<QBChatMessage>> qbChatMessageArray;

    public static synchronized QBChatMessagesHolder getInstance(){
        QBChatMessagesHolder qbChatMessagesHolder;
        synchronized (QBChatMessagesHolder.class){
            if (instance == null)
                instance = new QBChatMessagesHolder();
            qbChatMessagesHolder = instance;
        }
        return qbChatMessagesHolder;
    }

    private QBChatMessagesHolder(){
        this.qbChatMessageArray = new HashMap<>();
    }

    public void putMessages(String dialogId, ArrayList<QBChatMessage> qbChatMessages){
        this.qbChatMessageArray.put(dialogId, qbChatMessages);
    }

    public void putMessage(String dialogId, QBChatMessage qbChatMessage){
        List<QBChatMessage> firstResult = (List)this.qbChatMessageArray.get(dialogId);
        firstResult.add(qbChatMessage);
        ArrayList<QBChatMessage> firstAdded = new ArrayList(firstResult.size());
        firstAdded.addAll(firstResult);
        putMessages(dialogId, firstAdded);
    }

    public ArrayList<QBChatMessage> getChatMessageByDialogId(String dialogId){
        return (ArrayList<QBChatMessage>)this.qbChatMessageArray.get(dialogId);
    }
}
