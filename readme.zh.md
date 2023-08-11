
[English](./README.md) | [简体中文](./README.zh.md)

# 综合作品情况描述



这个是一个具有自动切换壁纸功能的软件，它能够将你手机的图片设置成壁纸，你也可以添加多个图片作为壁纸，然后在后台进行定时切换，它还提供了在线壁纸浏览，你可以很方便的将你看到的好看的壁纸下载下来，然后让它自动切换。它的功能简单，但是却设计精美，简单易用，同时也能很方便的扩展后续的功能。
​

主要功能：

1. 图片的浏览与加载：在Home页面上，有来自[pixaby](https://pixabay.com/zh/) 提供的高清图片，能够随意滚动浏览，点击可以查看大图进行随意放大缩小和下载：
1. 在Source页面，可以添加相册的单张照片，也可以选择整个文件夹进行遍历，然后通过列表进行展示
1. 所有的添加和删除都是动态同步到列表下面，您可以很清楚的看到自己的图片文件，并将它设置成您的壁纸

​

# 创新点和优势
它的功能简单，就是能够定时切换壁纸，但是跟其它壁纸软件不同的是，它主要依靠的不是网络，而是本地，因此可以提高稳定性和设置更大的壁纸，它直接调用系统的壁纸接口，除了文件读写外无须其它权限，也无需时刻待在后台。同时它也是一款精美的图像浏览软件，能将您手机上的照片很好的组织起来，后续还会设置小组件，方便您可以随时调用您的图片（比如健康码等）。它除了下载壁纸以外不会对您手机上的文件进行任何操作。经过测试，软件在后台运行时候占用的资源很少，因此更有普适性
​

# 产品设计
软件从头到尾以图片为主体，配合上全透明的卡片设计，能够最大限度地得到最佳视觉效果：
![pic1.png](https://cdn.nlark.com/yuque/0/2021/png/2141889/1625395844608-587674e4-2f50-45e4-a6ce-de268a43d6c8.png#clientId=u789f5b13-ee01-4&from=drop&height=544&id=u833cc62f&margin=%5Bobject%20Object%5D&name=pic1.png&originHeight=2400&originWidth=1080&originalType=binary&ratio=1&size=3934972&status=done&style=none&taskId=u6ebd5f6d-7e97-4190-9d93-d04fe708c5f&width=245)![pic2.png](https://cdn.nlark.com/yuque/0/2021/png/2141889/1625395874836-1363d22c-6955-481b-825e-12bcf7f4938d.png#clientId=u789f5b13-ee01-4&from=drop&height=544&id=ud422b31d&margin=%5Bobject%20Object%5D&name=pic2.png&originHeight=2400&originWidth=1080&originalType=binary&ratio=1&size=3133916&status=done&style=none&taskId=u27473e02-24fe-4c30-bf07-39d304367bb&width=245)![pic3.png](https://cdn.nlark.com/yuque/0/2021/png/2141889/1625395895013-ac4af3b4-5fd3-4813-9551-8834b798ce84.png#clientId=u789f5b13-ee01-4&from=drop&height=547&id=u54a27a40&margin=%5Bobject%20Object%5D&name=pic3.png&originHeight=2400&originWidth=1080&originalType=binary&ratio=1&size=1762021&status=done&style=none&taskId=u9d523a5e-a977-4e5e-958c-cbe5d2b3366&width=246)
底下透明的导航栏只有在深色背景时候才会显示出来：
![pi4.jpg](https://cdn.nlark.com/yuque/0/2021/jpeg/2141889/1625395991094-0d7b8996-94f6-4c1b-9a57-8650a2db6162.jpeg#clientId=u789f5b13-ee01-4&from=drop&height=600&id=u6b9153cd&margin=%5Bobject%20Object%5D&name=pi4.jpg&originHeight=2400&originWidth=1080&originalType=binary&ratio=1&size=126343&status=done&style=none&taskId=uc74548e2-0b91-456f-a823-4f2e670ddb1&width=270)
主页图片经过精心挑选，同时您每次设置一张壁纸，APP的背景以及中间的卡片都会动态变化，这给予了您最大的灵动：
![pic4.png](https://cdn.nlark.com/yuque/0/2021/png/2141889/1625396193356-9c1e7c21-0b15-420d-a65b-c07c92a7e895.png#clientId=u789f5b13-ee01-4&from=drop&height=531&id=u447ab4f9&margin=%5Bobject%20Object%5D&name=pic4.png&originHeight=2400&originWidth=1080&originalType=binary&ratio=1&size=3602176&status=done&style=none&taskId=u30d35c89-9b7f-4873-9c50-71555852b0d&width=239)![pic5.png](https://cdn.nlark.com/yuque/0/2021/png/2141889/1625396193108-89bfbb4c-0657-48d3-859c-610a920cd547.png#clientId=u789f5b13-ee01-4&from=drop&height=520&id=u073cc0a2&margin=%5Bobject%20Object%5D&name=pic5.png&originHeight=2400&originWidth=1080&originalType=binary&ratio=1&size=3152720&status=done&style=none&taskId=ua7d2ea9f-6cb5-4ac9-b646-d435d488209&width=234)![pic6.png](https://cdn.nlark.com/yuque/0/2021/png/2141889/1625396192265-cd042671-f147-415d-b79d-00850ec3167f.png#clientId=u789f5b13-ee01-4&from=drop&height=531&id=u8f721f83&margin=%5Bobject%20Object%5D&name=pic6.png&originHeight=2400&originWidth=1080&originalType=binary&ratio=1&size=1930722&status=done&style=none&taskId=uc7d6883b-8556-41db-9875-291773f2a83&width=239)
设置界面的大量留白，可以让您不错过您的每一张收藏
同时，滑动界面时候，能够有丰富的背景动画，您在滑动列表时候，图片也能自动根据不同的比例来进行自适应布局，给予您最好的用户体验。
# 技术实现方案
本次主要用的是原生Android + Java 作为开发基础，配合最新的安卓JetPack，让它能稳定，快速而且流畅，本次设计主要分为三个界面：主页，壁纸源，设置。使用MVVM设计模式，将视图和数据分离，能够最好的进行数据共享，同时也方便不同的Fragment之间的通信。其包括一个Activity，三个主要的Fragment以及一些小fragment组成，其中组织架构如下：
![image.png](https://cdn.nlark.com/yuque/0/2021/png/2141889/1625397443535-d65ff018-e40e-4b09-bd89-91b8e102f608.png#clientId=u789f5b13-ee01-4&from=paste&height=580&id=u94f9d434&margin=%5Bobject%20Object%5D&name=image.png&originHeight=796&originWidth=1022&originalType=binary&ratio=1&size=59253&status=done&style=none&taskId=u94e0e5f5-f215-4856-9f81-0e770df2363&width=745)
本次采用了两个ViewModel用来隔离数据，通过LiveData和Databinding，能够很方便的将数据和视图分离，从而达到《高内聚，低耦合》的目的。同时通过最新的RecycleView+ViewPager2的模式能够实现资源的回收利用和懒加载。通过引入图片加载库Glide以及Java多线程，能将耗时操作转移到后台执行。通过安卓的SharePreference能够很好的实现数据的永久存储，这也是这个软件的优点：我不保存图片（当然用户可以手动保存图片到本地相册），除了开机的背景图片外，所有图片都是在打开的时候进行自动加载，多线程和Glide保证了加载速度，同时RecycleView能很方便的实现内存回收。
​

通过安卓的后台服务机制，能很方便的执行后台的壁纸切换，同时也不会打扰到前台用户进程，能最大化用户体验。
​

本次实现并没有用到后台服务器，理由很简单，目前服务器的带宽不足以支撑太多图片的并发浏览，本次采用的是开源图片网站[https://pixabay.com/zh/](https://pixabay.com/zh/) 来提供免费开源的图片，后续也会收集更多的壁纸源来进行展示，因此作为第一版本来说，以简单为主来进行处理。
​

# 引用的框架
本次采用谷歌官方推荐的图片加载库Glide，网络请求使用了Volley的请求队列，图片壁纸源引用了[https://pixabay.com/zh/](https://pixabay.com/zh/)  提供的图片，它是一个开源图片网站，上面的图片可以免费使用。其余均为自主实现。里面的图片和资源文件是在开源网站iconfont上找到的，因为没有商业行为，因此没有对所有者请求允许。
​

​

​

​

​

​

​

​

​

