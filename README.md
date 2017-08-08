# MultiThreadDownLoad

#多线程断点续传下载，利用AsyncTask来实现

#主要有下面几个类:

DownLoadManager:下载管理类，外部通过这个类对下载进行管理，比如添加下载任务，停止下载任务

DownLoader：下载器，每一个下载任务对应一个下载器

DownLoadTask：下载任务类，具体实现下载功能的AsyncTask类，比如设置3个线程来下载一个文件，那么每一个下载任务对应3个DownLoadTask

DownLoadInfo：下载线程的信息

AppInfo：代表一个下载的文件的信息，由于当前想实现一个各种app应用的下载功能，所以命名为AppInfo

断点续传功能用到的数据库，是利用GreenDao第三方ORM库来实现的

下载的进度和状态的反馈，是利用EventBus进行发送和接受的，DownLoadProgressEvent类包含下载文件的信息，可以从里面读取下载的状态Status和下载的进度



