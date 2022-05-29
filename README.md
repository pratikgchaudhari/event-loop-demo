# event-loop-demo

> A project to learn how _Event Loop_ works.

> I wanted to undertand how the _Event Loop_ that sits at the core of asynchronous event-driven frameworks such as _Netty_ handled synchronous as well as asynchronous execution of code trigerred in response to various events (and why it preferred doing the latter over the former).

> Hence, I built a simple event loop of my own in Java so that I could observe what happens if the event loop gets *blocked* on a long-running task.

![](https://user-images.githubusercontent.com/34649848/170875278-cb4b7c89-cbbe-4498-ab0d-cfa4cb8796bd.gif)

![](https://user-images.githubusercontent.com/34649848/170875314-dd3b281c-f93d-42ab-9de7-5358dc5bbbd8.gif)

![](https://user-images.githubusercontent.com/34649848/170875338-8f171c32-c675-4960-82a2-58b36c4608dc.gif)
