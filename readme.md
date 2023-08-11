[English](./README.md) | [简体中文](./README.zh.md)

# Getting start

This project was a pure Android App project, there is no backend in this project, thus, you can just clone this repo and run it as an Android application.

# Description of Comprehensive Project


This is a software with the functionality of automatically switching wallpapers. It can set the images on your phone as wallpapers. You can also add multiple images as wallpapers and switch them automatically in the background at scheduled intervals. It also provides online wallpaper browsing, allowing you to conveniently download beautiful wallpapers you come across and have them automatically switched. The software has simple functionality, but it is exquisitely designed, easy to use, and can be easily expanded with additional features in the future.

Main features:

1. Image browsing and loading: On the Home page, there are high-definition images provided by [pixaby](https://pixabay.com/zh/). You can freely scroll through them, and clicking on an image allows you to view it in a larger size, zoom in or out, and download it.
1. On the Source page, you can add individual photos from your album or choose to traverse through an entire folder, which will be displayed in a list.
1. All additions and deletions are dynamically synchronized with the list below, allowing you to clearly see your image files and set them as wallpapers.

# Innovations and Advantages

Its functionality is simple, which is to switch wallpapers at scheduled intervals. However, unlike other wallpaper software, it primarily relies on local resources rather than the network, which improves stability and allows for setting larger wallpapers. It directly calls the system's wallpaper interface and requires no additional permissions apart from file read and write access. It also does not need to run in the background constantly. Additionally, it is a beautifully designed image browsing software that effectively organizes the photos on your phone. In the future, it will also provide widgets for easy access to your images (such as health codes). Apart from downloading wallpapers, it does not perform any operations on the files on your phone. Through testing, it has been found to consume minimal resources when running in the background, making it widely applicable.


# Product Design

The software is primarily image-based, complemented by a fully transparent card design that maximizes visual effects:

![pic1.png](https://cdn.nlark.com/yuque/0/2021/png/2141889/1625395844608-587674e4-2f50-45e4-a6ce-de268a43d6c8.png#clientId=u789f5b13-ee01-4&from=drop&height=544&id=u833cc62f&margin=%5Bobject%20Object%5D&name=pic1.png&originHeight=2400&originWidth=1080&originalType=binary&ratio=1&size=3934972&status=done&style=none&taskId=u6ebd5f6d-7e97-4190-9d93-d04fe708c5f&width=245)
![pic2.png](https://cdn.nlark.com/yuque/0/2021/png/2141889/1625395874836-1363d22c-6955-481b-825e-12bcf7f4938d.png#clientId=u789f5b13-ee01-4&from=drop&height=544&id=ud422b31d&margin=%5Bobject%20Object%5D&name=pic2.png&originHeight=2400&originWidth=1080&originalType=binary&ratio=1&size=3133916&status=done&style=none&taskId=u27473e02-24fe-4c30-bf07-39d304367bb&width=245)
![pic3.png](https://cdn.nlark.com/yuque/0/2021/png/2141889/1625395895013-ac4af3b4-5fd3-4813-9551-8834b798ce84.png#clientId=u789f5b13-ee01-4&from=drop&height=547&id=u54a27a40&margin=%5Bobject%20Object%5D&name=pic3.png&originHeight=2400&originWidth=1080&originalType=binary&ratio=1&size=1762021&status=done&style=none&taskId=u9d523a5e-a977-4e5e-958c-cbe5d2b3366&width=246)

The transparent navigation bar at the bottom is displayed only on a dark background:

![pi4.jpg](https://cdn.nlark.com/yuque/0/2021/jpeg/2141889/1625395991094-0d7b8996-94f6-4c1b-9a57-8650a2db6162.jpeg#clientId=u789f5b13-ee01-4&from=drop&height=600&id=u6b9153cd&margin=%5Bobject%20Object%5D&name=pi4.jpg&originHeight=2400&originWidth=1080&originalType=binary&ratio=1&size=126343&status=done&style=none&taskId=uc74548e2-0b91-456f-a823-4f2e670ddb1&width=270)

The main page images are carefully selected, and every time you set a wallpaper, the background and the middle card of the app will dynamically change, providing maximum flexibility:

![pic4.png](https://cdn.nlark.com/yuque/0/2021/png/2141889/1625396193356-9c1e7c21-0b15-420d-a65b-c07c92a7e895.png#clientId=u789f5b13-ee01-4&from=drop&height=531&id=u447ab4f9&margin=%5Bobject%20Object%5D&name=pic4.png&originHeight=2400&originWidth=1080&originalType=binary&ratio=1&size=3602176&status=done&style=none&taskId=u30d35c89-9b7f-4873-9c50-71555852b0d&width=239)
![pic5.png](https://cdn.nlark.com/yuque/0/2021/png/2141889/1625396193108-89bfbb4c-0657-48d3-859c-610a920cd547.png#clientId=u789f5b13-ee01-4&from=drop&height=520&id=u073cc0a2&margin=%5Bobject%20Object%5D&name=pic5.png&originHeight=2400&originWidth=108)
# Product Design

The software is primarily image-based, complemented by a fully transparent card design that maximizes visual effects:

![pic1.png](https://cdn.nlark.com/yuque/0/2021/png/2141889/1625395844608-587674e4-2f50-45e4-a6ce-de268a43d6c8.png#clientId=u789f5b13-ee01-4&from=drop&height=544&id=u833cc62f&margin=%5Bobject%20Object%5D&name=pic1.png&originHeight=2400&originWidth=1080&originalType=binary&ratio=1&size=3934972&status=done&style=none&taskId=u6ebd5f6d-7e97-4190-9d93-d04fe708c5f&width=245)
![pic2.png](https://cdn.nlark.com/yuque/0/2021/png/2141889/1625395874836-1363d22c-6955-481b-825e-12bcf7f4938d.png#clientId=u789f5b13-ee01-4&from=drop&height=544&id=ud422b31d&margin=%5Bobject%20Object%5D&name=pic2.png&originHeight=2400&originWidth=1080&originalType=binary&ratio=1&size=3133916&status=done&style=none&taskId=u27473e02-24fe-4c30-bf07-39d304367bb&width=245)
![pic3.png](https://cdn.nlark.com/yuque/0/2021/png/2141889/1625395895013-ac4af3b4-5fd3-4813-9551-8834b798ce84.png#clientId=u789f5b13-ee01-4&from=drop&height=547&id=u54a27a40&margin=%5Bobject%20Object%5D&name=pic3.png&originHeight=2400&originWidth=1080&originalType=binary&ratio=1&size=1762021&status=done&style=none&taskId=u9d523a5e-a977-4e5e-958c-cbe5d2b3366&width=246)

The transparent navigation bar at the bottom is displayed only on a dark background:

![pi4.jpg](https://cdn.nlark.com/yuque/0/2021/jpeg/2141889/1625395991094-0d7b8996-94f6-4c1b-9a57-8650a2db6162.jpeg#clientId=u789f5b13-ee01-4&from=drop&height=600&id=u6b9153cd&margin=%5Bobject%20Object%5D&name=pi4.jpg&originHeight=2400&originWidth=1080&originalType=binary&ratio=1&size=126343&status=done&style=none&taskId=uc74548e2-0b91-456f-a823-4f2e670ddb1&width=270)

The main page images are carefully selected, and every time you set a wallpaper, the background and the middle card of the app will dynamically change, providing maximum flexibility:

![pic4.png](https://cdn.nlark.com/yuque/0/2021/png/2141889/1625396193356-9c1e7c21-0b15-420d-a65b-c07c92a7e895.png#clientId=u789f5b13-ee01-4&from=drop&height=531&id=u447ab4f9&margin=%5Bobject%20Object%5D&name=pic4.png&originHeight=2400&originWidth=1080&originalType=binary&ratio=1&size=3602176&status=done&style=none&taskId=u30d35c89-9b7f-4873-9c50-71555852b0d&width=239)
![pic5.png](https://cdn.nlark.com/yuque/0/2021/png/2141889/1625396193108-89bfbb4c-0657-48d3-859c-610a920cd547.png#clientId=u789f5b13-ee01-4&from=drop&height=520&id=u073cc0a2&margin=%5Bobject%20Object%5D&name=pic5.png&originHeight=2400&originWidth=108)