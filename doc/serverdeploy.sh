jar_port=9100
jar_pid="lsof -n -P -t -i :${jar_port}"

service_name="chatroom-server"
service_pid=`ps -ef | grep $service_name | grep -v grep | awk '{print $2}'`

jar_filename="/usr/local/chatroom_project/chatroom-server.jar"

if [ -n "${jar_pid}" ]; then
        echo "进程运行中[$service_pid]"

        while [ -n "${jar_pid}" ]  
        do
                sleep 1
                kill $service_pid
		jar_pid=`lsof -n -P -t -i :${jar_port}`
                echo "正在关闭进程[$service_pid]..."
        done
        echo "成功关闭进程"
fi

nohup java -Xms1024M -Xmx1024M -Xss256K -jar ${jar_filename} >/dev/null 2>&1 &

echo "开始启动进程["${jar_port}"]..."

#检测进程是否启动完成
while [ -z "${jar_pid}" ]
do
 sleep 1
 #echo "TOMCAT_PID["${TOMCAT_PID}"]"
        jar_pid=`lsof -n -P -t -i :${jar_port}`
 echo "正在启动进程["${jar_port}"]..."
done

echo "成功启动进程."
