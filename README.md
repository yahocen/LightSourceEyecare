# Light Source Eyecare

采用Java语言实现调整显示器亮度

代码内容借鉴了 https://github.com/wasteofserver/monitor-brightness-control，并在其基础上增加了界面为每个显示器滑块设置亮度值等功能

## 特色

采用UDP接收光线传感器数值通过计算调节每块屏幕的亮度

## 挑战

鉴于Jvm的特性，编译运行时内存占用较大，至少我不希望因为调整屏幕亮度的小功能就占用50m以上的内存。我尝试通过graalvm编译为本地镜像，但是目前graalvm对JNI、JNA的支持太过复杂使我无法继续

不同厂商显示器的亮度调节API是不同的，目前Windows系统不能够很好的支持每个厂商的API，可能需要自行按型号匹配调节方式



