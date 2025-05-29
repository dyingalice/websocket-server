package cn.org.alice.demo;

import cn.org.alice.demo.bean.Body;
import cn.org.alice.demo.service.ReplyService;
import cn.org.alice.socket.core.anno.WssControllerAdvice;
import cn.org.alice.socket.core.anno.WssExceptionHandler;
import cn.org.alice.socket.core.anno.WssPrimary;
import cn.org.alice.socket.core.exception.SocketException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;

/***
 * @author 爱莉丝
 * @since 2021/10/15 14:24
 * @apiNote 全局异常处理
 **/
@Slf4j
@WssControllerAdvice
public class AdviceException {

    @Autowired
    private ReplyService replyService;

    @WssExceptionHandler(NullPointerException.class)
    public void NullPointerException(NullPointerException ex, @WssPrimary String id) {
        log.error("NullPointerException:", ex);
        log.info("primary:{}", id);
        replyService.reply(Body.builder().message("空指针").build(), id);
    }

    @WssExceptionHandler(SocketException.class)
    public void SocketException(SocketException ex, @WssPrimary String id) {
        log.error("SocketException:", ex);
        replyService.reply(Body.builder().message(ex.getMessage()).build(), id);
    }


    @WssExceptionHandler(InvocationTargetException.class)
    public void InvocationTargetException(InvocationTargetException ex) {
        log.error("InvocationTargetException:", ex.getMessage());
    }

    @WssExceptionHandler(Exception.class)
    public void Exception(Exception ex) {
        log.error("Exception:", ex);
    }

    @WssExceptionHandler(RuntimeException.class)
    public void RuntimeException(RuntimeException ex) {
        log.error("RuntimeException:", ex);
    }

    @WssExceptionHandler({MismatchedInputException.class, JsonParseException.class})
    public void MismatchedInputException(Exception ex, MismatchedInputException miex, JsonParseException jpex) {
        log.error("Exception:", ex);
        log.error("MismatchedInputException:", miex);
        log.error("JsonParseException:", jpex);
    }
}