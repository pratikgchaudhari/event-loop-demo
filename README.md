# event-loop-demo

> A project to learn how _Event Loop_ works.

> I wanted to undertand how the _Event Loop_ that sits at the core of asynchronous event-driven frameworks such as _Netty_ handled synchronous as well as asynchronous execution of code trigerred in response to various events (and why it preferred doing the latter over the former).

> Hence, I built a simple event loop of my own in Java so that I could observe what happens if the event loop gets *blocked* on a long-running task.

[Screen recording of a Terminal executing a simple task that prints "Hello"](https://user-images.githubusercontent.com/34649848/170871066-e48c9ee9-75b0-44ef-862b-6dc9a395fd90.gif)
