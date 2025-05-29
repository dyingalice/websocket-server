FROM openjdk:17

# 添加时区环境变量，亚洲，上海
ENV TimeZone=Asia/Shanghai

# 使用软连接，并且将时区配置覆盖/etc/timezone
RUN ln -snf /usr/share/zoneinfo/$TimeZone /etc/localtime && echo $TimeZone > /etc/timezone

ADD demo-socket-server-starter/target/*.jar /app.jar

# 创建堆转储目录
RUN mkdir /heapdumps

EXPOSE 8080

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /app.jar $PARAMS"]