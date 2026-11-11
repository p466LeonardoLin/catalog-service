# Bonus answers (Practicum 1)

**1.**
A JAR contains its own server in our case it is called Tomcat, so it only needs Java to run. A WAR needs a separate shared server that someone must install and keep matching. With a JAR, the same file runs the same way everywhere and can be scaled.

**2.**
The max is 50 threads, and all of them are busy, so the 51st request waits in a queue until one is ok to be idle. 

Two ways: 
- make one machine stronger so in terms of computer it would be scaling vertically, or run more copies of the app so scaling horizontally. A cloud native app would scale e horizontal way, because it is cheaper, flexible, and still working if one fail.

**3.**
Many requests run at the same time on different threads, and they all use the same map. A normal HashMap is not safe for that and can lose or corrupt data. ConcurrentHashMap is built for many threads using it at once.

**4.**
400 means the request itself is wrong. 

A duplicate ISBN is a correct request that breaks a business rule. 

422 means it understads but can't process with it, which is better.

**5.**
Advantage: @WebMvcTest is faster, because it only loads the web layer. 

Limitation: it does not test the real server, service and repository working together, so it can miss problems between the layers.

**6.**
Developers get feedback right after each push and can fix problems while the change is fresh. 

If a slow test makes it too long, they system or the test would move that test to a later stage so doing the faster one first, so the commit stage stays fast.