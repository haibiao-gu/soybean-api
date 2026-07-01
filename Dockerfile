FROM eclipse-temurin:21-jre-alpine

# 设置工作目录
WORKDIR /app

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# 创建日志目录
RUN mkdir -p /app

# 暴露端口
EXPOSE 8080

ADD /soybean-app/target/app.jar ./app.jar

# JVM 参数优化（针对 Java 21）
ENV JAVA_OPTS="-XX:+UseZGC \
               -XX:+ZGenerational \
               -XX:MaxDirectMemorySize=256m \
               -Djava.security.egd=file:/dev/./urandom \
               -Dfile.encoding=UTF-8"

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
