package cn.org.alice.socket.core.beans;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageData implements Serializable {

    private String primary;

    private String fromServer;

    private String business;

    private Object data;
}