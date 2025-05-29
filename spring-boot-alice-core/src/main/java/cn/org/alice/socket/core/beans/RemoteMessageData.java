package cn.org.alice.socket.core.beans;

import lombok.Data;

import java.io.Serializable;

@Data
public class RemoteMessageData implements Serializable {

    private String primary;

    private String link;

    private Object data;
}