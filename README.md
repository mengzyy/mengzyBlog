# mengzyBlog
A blog by mzy on mblog

### 访问方式（暂时，因为涉及到新老博客的更替）
http://47.93.29.212:8082

### 前言&说明

本项目为开源博客 https://github.com/langhsu/mblog 的改进版
糅合了自己的fastdfs图片服务器，无需将图片存储在本地
并且内嵌了docker插件，无需改动配置文件，即可多环境运行（仅仅改动数据库的url）

### 技术介绍

+ MySQL
+ Spring-boot
+ Spring-data-jpa
+ Shiro
+ Lombok
+ Freemarker
+ Bootstrap
+ SeaJs
+ docker（如果需要docker部署的话）
+ fastdfs（需要自己配服务器）
+ OhMyMail

### 启动前需知
#### Question1

若没有自己的图片服务器，如果关闭fastdfs，改成native

改动yml即可

![](https://imgkr.cn-bj.ufileos.com/61aee581-6252-4e12-b04b-f7dc002f362b.png)

#### Question2

配置docker环境

1.配置自己的docker地址(pom文件)

```
<plugin>
                <groupId>com.spotify</groupId>
                <artifactId>docker-maven-plugin</artifactId>
                <version>0.4.13</version>
                <configuration>
                    <!--镜像名称-->
                    <imageName>mengzy:blog</imageName>
                    <!--dockerfile服务器 -->
                    <dockerHost>http://xx.xx.xx.xxx:2375</dockerHost>
                    <!--dockerfile地址 -->
                    <dockerDirectory>${project.basedir}/src/main/docker</dockerDirectory>
                    <resources>
                        <resource>
                            <targetPath>/</targetPath>
                            <directory>${project.build.directory}</directory>
                            <include>${project.build.finalName}.jar</include>
                        </resource>
                    </resources>
                </configuration>
            </plugin>

```

2.Docker文件配置

```
#项目运行环境 必须配置
FROM java:8
#挂载点 数据卷
VOLUME /tmp
# 第一个是你项目打包后的名字 ，请不要随意起名
ADD demo-0.0.1-SNAPSHOT.jar(这里写你的maven打包后的名字) app.jar
#暴露出来的port 不一定会被使用 可使用 懒加载
EXPOSE 8082
# java打包时的参数
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

```

### 页面展示

![](https://imgkr.cn-bj.ufileos.com/72ea60d0-a173-497f-a76e-04687d7140d0.png)









#### test merge
#### test
