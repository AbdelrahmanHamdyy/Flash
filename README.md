<div align= >

# <img align=center width=50px height=50px src="https://media0.giphy.com/media/CVrs76nnBvU7azfTLj/giphy.gif?cid=ecf05e47om1y68g5als66xi5mn32ui6gk2g8wpnv145ag265&rid=giphy.gif&ct=s"> Flash

</div>
<p align="center">
   <img align="center" height="500px"  src="https://user-images.githubusercontent.com/71986226/183626365-0709b859-60b9-4738-b53e-b19261018304.jpg" alt="logo">
</p>

<p align="center"> 
    <br> 
</p>

## <img align= center width=50px height=50px src="https://thumbs.gfycat.com/HeftyDescriptiveChimneyswift-size_restricted.gif"> Table of Contents

- <a href ="#about"> 📙 Overview</a>
- <a href ="#Started"> 💻 Get Started</a>
- <a href ="#Work"> 🧱 Data Structures Used </a>
- <a href ="#Contributors"> ✨ Contributors</a>
- <a href ="#License"> 🔒 License</a>
<hr style="background-color: #4b4c60"></hr>

<a id = "about"></a>

## <img align="center"  height =50px src="https://user-images.githubusercontent.com/71986226/154076110-1233d7a8-92c2-4d79-82c1-30e278aa518a.gif"> Overview

<ul>
<li> The aim of this project is to develop a simple Crawler- based search engine that demonstrates the main features of a search engine
and the interaction between them.</li>
<li> The main features of a search engine</li>
<ul>
<li> Web Crawling</li>
<li> Indexing</li> 
<li> Ranking</li>
</ul>
<li> Build using <a href="https://en.wikipedia.org/wiki/Java_(programming_language)">Java lnaguage</a>.</li>
<li>  Web interface  for  Search Engine  using <a href="https://en.wikipedia.org/wiki/HTML">Html</a> & <a href="https://en.wikipedia.org/wiki/CSS">CSS</a>.</li>
</ul>
<hr style="background-color: #4b4c60"></hr>
<a id = "Started"></a>

## <img  align= center width=50px height=50px src="https://c.tenor.com/HgX89Yku5V4AAAAi/to-the-moon.gif"> Get Started

<ol>
<li>Clone the repository.

<br>

```
git clone https://github.com/abdelrahman0123/Flash
```

</li>
<li> You will need to download <a href="https://www.oracle.com/java/technologies/downloads/">Jdk</a>. </li>
<li> You will need to download <a href="https://tomcat.apache.org/download-90.cgi">Tomcat</a>. </li>
</ol>
<hr style="background-color: #4b4c60"></hr>

## <img align= center width=70px height=70px src="https://user-images.githubusercontent.com/71986226/178469374-15498392-26a1-4ba0-99d7-9ce899c131f0.gif"> Data Structures Used

<br>
<ul>
<li>Queue</li>
<li>Priority Queue</li>
<li>Process Data
<ul>
<li>Arrival time</li>
<li>Priority</li>
<li>Run time</li>
<li>ID</li>
<li>Memsize</li>
</ul>
</li>
<li>Memory Node
<ul>
<li>Size</li>
<li>Start</li>
<li>Pointer to the next memory node</li>
</ul>
</li>
<li> PCB for each process
<ul>
<li>ID</li>
<li>PID</li>
<li>Arrival time</li>
<li>Burst time</li>
<li>Finish time</li>
<li>Running time</li>
<li>Stop time</li>
<li>Priority</li>
<li>Start time</li>
<li>Start Address in memory</li>
<li>Memory Size</li>
</ul>
</li>
</ul>
<hr style="background-color: #4b4c60"></hr>
 <a id ="Algorithms"></a>

## <img align= "center" width=70px height=70px src="https://media0.giphy.com/media/Lqo3UBlXeHwZDoebKX/giphy.gif?cid=ecf05e47axkic0jguefzmfvqv5ncejylr7hhml03ciklbmdw&rid=giphy.gif&ct=s">Algorithms Explanation

<br>
<ol>
<li>HPF
<ul>
<li>The main loop checks if the processes are all finished or not. </li>
<li>In an inner loop, we get all the processes arrived at this particular second and enqueue them in the ready queue.</li>
<li>If there is no current process and the ready queue isn’t empty, we directly pop from the ready queue as it’s already sorted with the minimum priority, then start the process, fork it, and execl Process.c.</li>
<li>Last check: If the remaining time of the running process is zero, then it is finished and the running process is set to NULL. The finished processes counter is incremented as well and the loop continues.</li>
</ul>
</li>
<li>SRTN
<ul>
<li>Receiving the processes from the process generator.</li>
<li>Checking if the coming process's burst time is smaller than the remaining time of the running process.</li>
<li>If it's smaller than the running process will be added to the ready queue and saved in stop resuming queue and the smaller one will be the running process.</li>
<li>Then every time we a process starts then we check if this process was stopped before “existing in stop resuming queue" or not to know if it started or resumed to resume the stopped process.</li>
</ul>
</li>
<li>RR
<ul>
<li>Check if there are arriving processes, receive them in ready queue.</li>
<li>Check if there is a processes finished or the time slot ended, change the state of the process from running to finished or ready respectively.</li>
<li>Check if there are processes in ready queue and no process is running so pick up one of them and run it.</li>
</ul>
</li>
</ol>
<hr style="background-color: #4b4c60"></hr>
<a id ="Assumptions"></a>

## <img align= "center" width=60px height=70px src="https://media2.giphy.com/media/8pEnqbR2gapFekW4KK/giphy.gif?cid=ecf05e47ire2dp6wrcli5orn0gddraxve7sug4v3753pquxa&rid=giphy.gif&ct=s">Assumptions

<br>
<ol>
<li>
In the memory waiting queue, it is implemented as a priority queue based on the algorithm so if it’s a SRTN, the priority is the remaining time while for RR, it is based on the memory size where the smaller one gets put into the ready queue first. As for the HPF, there is no need since the running process is the only one put into the ready queue.
</li>
<br>
<li>
We made an array of arrivals in the process generator as a shared memory with the scheduler in order to make sure that any process arriving at a specific time is read by the scheduler and not skipped.
</li>
<br>
<li>We synchronize between the stopped process and the arrived one if they come in the same second so that the stopped process is put into the queue before the arrived process.
</li>
</ol>

<a id ="License"></a>

## 🔒 License

> **Note**: This software is licensed under MIT License, See [License](https://github.com/abdelrahman0123/Flash/blob/main/LICENSE) for more information ©abdelrahman0123.
