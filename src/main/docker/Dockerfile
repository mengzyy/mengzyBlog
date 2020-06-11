#项目运行环境 必须配置
FROM java:8
#挂载点 数据卷
VOLUME /tmp
# 第一个是你项目打包后的名字 ，请不要随意起名
ADD demo-0.0.1-SNAPSHOT.jar app.jar
#暴露出来的port 不一定会被使用 可使用 懒加载
EXPOSE 8082
# java打包时的参数
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]