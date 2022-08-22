<div align= >

# <img align=center width=50px height=50px src="https://media0.giphy.com/media/CVrs76nnBvU7azfTLj/giphy.gif?cid=ecf05e47om1y68g5als66xi5mn32ui6gk2g8wpnv145ag265&rid=giphy.gif&ct=s"> Flash

</div>
<p align="center">
   <img align="center" height="450px"  src="https://user-images.githubusercontent.com/71986226/185804922-31aa1626-c1a1-4a92-a5eb-cba79cf6ab5b.jpg" alt="logo">
</p>

<p align="center"> 
    <br> 
</p>

## <img align= center width=50px height=50px src="https://thumbs.gfycat.com/HeftyDescriptiveChimneyswift-size_restricted.gif"> Table of Contents

- <a href ="#about"> 📙 Overview</a>
- <a href ="#Started"> 💻 Get Started</a>
- <a href ="#Work"> 🧱 Search Engine Modules </a>
- <a href ="#Video"> 📽 GIF Demo</a>
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
<br>
<li> Build using <a href="https://en.wikipedia.org/wiki/Java_(programming_language)">Java lnaguage</a>.</li>
<li>  Web interface  for  Search Engine  using <a href="https://en.wikipedia.org/wiki/HTML">Html</a> & <a href="https://en.wikipedia.org/wiki/CSS">CSS</a> & <a href="https://en.wikipedia.org/wiki/JavaScript">JS</a>.</li>

<li> Build using <a href="https://en.wikipedia.org/wiki/MongoDB">MongoDB</a>.</li>
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
<li> You will need to read <a href="https://github.com/abdelrahman0123/Flash/blob/main/Search%20Engine%20Project%20.pdf">Search Engine Project</a> to understand project very well. </li>
</ol>
<hr style="background-color: #4b4c60"></hr>
<a id = "Work"></a>

## <img align= center width=65px height=65px src="https://raw.githubusercontent.com/EslamAsHhraf/EslamAsHhraf/main/images/skills.gif"> Search Engine Modules

<table align="left;">
<tr>
<th width=23%>Module</th>
<th>Description</th>
</tr>
<tr>
<td> 🔷 Web Crawler</td>
<td>The web crawler is a software agent that collects documents from the web. The crawler starts with a list of URL addresses (seed set). It downloads the documents identified by these URLs and extracts hyper-links from them. The extracted URLs are added to the list of URLs to be downloaded. Thus, web crawling is a recursive process.</td>
</tr>
<tr>
<td>🔶 Indexer</td>
<td>The output of web crawling process is a set of downloaded HTML documents. To respond to user queries fast enough, the contents of these documents have to be indexed in a data structure that stores the words contained in each document and their importance (e.g., whether they are in the title, in a header or in plain text).</td>
</tr>
<tr>
<td> 🔷 Query Processor</td>
<td>This module receives search queries, performs necessary preprocessing and searches the index for relevant documents. Retrieve documents containing words that share the same stem with those in the search query. For example, the search query “travel” should match (with lower degree) the words “traveler”, “traveling” … etc.</td>
</tr>
<tr>
<td>🔶 Phrase Searching</td>
<td>Search engines will generally search for words as phrases when quotation marks are placed around the phrase.</td>
</tr>
<tr>
<td>🔷 Ranker</td>
<td>
<p>The ranker module sorts documents based on their popularity and relevance to the search query.
</p>
<ol>
<li>Relevance</li>
<p>Relevance is a relation between the query words and the result page and could be calculated in several ways such as tf-idf of the query word in the result page or simply whether the query word appeared in the title, heading, or body. And then you aggregate the scores from all query words to produce the final page relevance score.</p>
<li>Popularity</li>
<p>Popularity is a measure for the importance of any web page regardless the requested query. You can use pagerank algorithm (as explained in the lecture) or other ranking algorithms to calculate each page popularity.</p>
</ol>
</td>
</tr>
<tr>
<td>🔶 Voice Recognition Search</td>
<td>Using a voice query instead of a typed one</td>
</tr>
</table>

<hr style="background-color: #4b4c60"></hr>
<a id ="Video"></a>

## <img  align= center width= 70px height =70px src="https://img.genial.ly/5f91608064ad990c6ee12237/bd7195a3-a8bb-494b-8a6d-af48dd4deb4b.gif?genial&1643587200063"> GIF Demo

<div  align="center">
  <img align="center" height=370px  src="https://user-images.githubusercontent.com/71986226/185816905-080167a3-b937-487f-a587-5dad5b310ce0.gif">
</div>
<hr style="background-color: #4b4c60"></hr>
<div  align="center">
<video src="https://user-images.githubusercontent.com/71986226/185817750-c0768762-230c-4562-a414-bfe79ef07c1d.mp4"   >
</video> 
</div>

<hr style="background-color: #4b4c60"></hr>

## <img  align="center" width= 70px height =55px src="https://media0.giphy.com/media/Xy702eMOiGGPzk4Zkd/giphy.gif?cid=ecf05e475vmf48k83bvzye3w2m2xl03iyem3tkuw2krpkb7k&rid=giphy.gif&ct=s"> Contributors <a id ="Contributors"></a>

<table align="center" >
  <tr>
    <td align="center"><a href="https://github.com/abdelrahman0123"><img src="https://avatars.githubusercontent.com/u/67989900?v=4" width="150;" alt=""/><br /><sub><b>Abdelrahman Hamdy</b></sub></a><br /></td>
       <td align="center"><a href="https://github.com/AbdelrahmanNoaman"><img src="https://avatars.githubusercontent.com/u/76150639?v=4" width="150;" alt=""/><br /><sub><b>Abdelrahman Noaman</b></sub></a><br /></td>
     <td align="center"><a href="https://github.com/AdhamAliAbdelAal" ><img src="https://avatars.githubusercontent.com/u/83884426?v=4" width="150;" alt=""/><br /><sub><b>Adham Ali</b></sub></a><br />
    </td>
     <td align="center"><a href="https://github.com/EslamAsHhraf"><img src="https://avatars.githubusercontent.com/u/71986226?v=4" width="150;" alt=""/><br /><sub><b>Eslam Ashraf</b></sub></a><br /></td>
  </tr>
</table>

<a id ="License"></a>

## 🔒 License

> **Note**: This software is licensed under MIT License, See [License](https://github.com/abdelrahman0123/Flash/blob/main/LICENSE) for more information ©abdelrahman0123.
