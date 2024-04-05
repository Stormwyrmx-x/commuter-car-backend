# 使用官方的Java运行时环境作为基础镜像
FROM openjdk:17-oracle
# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 复制target目录下的jar文件到工作目录
COPY commuter-car-backend-0.0.1-SNAPSHOT.jar /commuter-car-backend.jar

# 设置容器启动时的命令
ENTRYPOINT ["java","-jar","/commuter-car-backend.jar"]