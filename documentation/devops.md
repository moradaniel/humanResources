DevOps
=====================

xsfsfgsfgsfdgsdfsfgsf


## Ubuntu server
 - version 14.04

## Java

Install Oracle Java8

	sudo add-apt-repository ppa:webupd8team/java
	sudo apt-get update
	sudo apt-get install oracle-java8-installer



Switching between Oracle Java 7 and Oracle Java 8

Switch to Oracle Java 8 (from Oracle java 7)


	sudo update-java-alternatives -s java-8-oracle


switch to Oracle Java 8 (from Oracle java 7)

	sudo update-java-alternatives -s java-7-oracle



```
$ java -version
```

## Apache Web Server
 - version
 
### Installation

```
$sudo apt-get install apache2
```

the default web page should display at

	http://localhost/

To see the apache directory structure

	dmora@ubuntuhr:/$ ls /etc/apache2/ -la
	total 96
	drwxr-xr-x   8 root root  4096 sep 13 14:59 .
	drwxr-xr-x 122 root root 12288 sep 13 16:09 ..
	-rw-r--r--   1 root root  7115 ene  7  2014 apache2.conf
	drwxr-xr-x   2 root root  4096 sep 13 14:59 conf-available
	drwxr-xr-x   2 root root  4096 sep 13 14:59 conf-enabled
	-rw-r--r--   1 root root  1782 ene  3  2014 envvars
	-rw-r--r--   1 root root 31063 ene  3  2014 magic
	drwxr-xr-x   2 root root 12288 sep 13 14:59 mods-available
	drwxr-xr-x   2 root root  4096 sep 13 14:59 mods-enabled
	-rw-r--r--   1 root root   320 ene  7  2014 ports.conf
	drwxr-xr-x   2 root root  4096 sep 13 14:59 sites-available
	drwxr-xr-x   2 root root  4096 sep 13 14:59 sites-enabled



/etc/hosts

### Restart
```
$sudo service apache2 restart
```
 
## Load Balancing and Scalability

![](./tomcatClustering.png)

-mod_jk
Apache Server and Tomcat instances
## Tomcat 
 - Version 8.0.12
### Installation

We will install Tomcat on the /opt/ directory, the one used for third party software.

    $ sudo wget http://mirrors.dcarsat.com.ar/apache/tomcat/tomcat-8/v8.0.12/bin/apache-tomcat-8.0.12.tar.gz
    $ sudo tar -zxvf apache-tomcat-8.0.12.tar.gz
    $ sudo mv apache-tomcat-8.0.12 apache-tomcat-8.0.12-server01
    $ sudo tar -zxvf apache-tomcat-8.0.12.tar.gz
    $ sudo mv apache-tomcat-8.0.12 apache-tomcat-8.0.12-server02



Before I can start the servers, I need to edit the port numbers to avoid conflicts:

	 mike@ubuntu:~$ vim /opt/tomcat/apache-tomcat-8.0.5-server2/conf/server.xml  
	 <Server port="9005" shutdown="SHUTDOWN">  
	  <Listener className="org.apache.catalina.core.AprLifecycleListener" SSLEngine="on" />  
	  <Listener className="org.apache.catalina.core.JreMemoryLeakPreventionListener" />  
	  <Listener className="org.apache.catalina.mbeans.GlobalResourcesLifecycleListener" />  
	  <Listener className="org.apache.catalina.core.ThreadLocalLeakPreventionListener" />  
	  <GlobalNamingResources>  
	   <Resource name="UserDatabase" auth="Container"  
	        type="org.apache.catalina.UserDatabase"  
	        description="User database that can be updated and saved"  
	        factory="org.apache.catalina.users.MemoryUserDatabaseFactory"  
	        pathname="conf/tomcat-users.xml" />  
	  </GlobalNamingResources>  
	  <Service name="Catalina">  
	   <Connector port="9009" protocol="AJP/1.3" redirectPort="9443" />  
	   <Engine name="Catalina" defaultHost="localhost">  
	    <Realm className="org.apache.catalina.realm.LockOutRealm">  
	     <Realm className="org.apache.catalina.realm.UserDatabaseRealm"  
	         resourceName="UserDatabase"/>  
	    </Realm>  
	    <Host name="localhost" appBase="webapps"  
	       unpackWARs="true" autoDeploy="true">  
	     <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"  
	         prefix="localhost_access_log" suffix=".txt"  
	         pattern="%h %l %u %t &quot;%r&quot; %s %b" />  
	    </Host>  
	   </Engine>  
	  </Service>  
	 </Server>  

Above is the full server.xml for my “server2”. The only difference to the server.xml of server1 is the value of the port attribute in the main Server element and the port and redirectPort attributes in the Connector element. You might also note that I have removed the HTTP connector, leaving only the AJP connector. The reason for that is that I want my web apps to be only accessible through the load balancer, and that communication will be over the more performant AJP protocol. It’s perfectly fine to leave HTTP connectors there while testing.


###Installation
    $ sudo wget http://mirrors.dcarsat.com.ar/apache/tomcat/tomcat-8/v8.0.12/bin/apache-tomcat-8.0.12.tar.gz
	//decompress
    $ sudo tar -zxvf apache-tomcat-8.0.12.tar.gz
	//create symbolic link
	$ sudo ln -s apache-tomcat-8.0.12 tomcat

### CATALINA_HOME



The directory where tomcat binaries reside is known as CATALINA_HOME, common to all running tomcat instances. In our case:

CATALINA_HOME=/opt/tomcat


/etc/environment (you do not use the command export in this file as it is not a normal bash script)

CATALINA_HOME=/path/to/the/root/folder/of/tomcat


## mod_jk

 Apache as a load balancer or proxy rather than just a server of static HTTP web pages. mod_jk is an Apache module which allows AJP communication between Apache and a back-end application server like GlassFish or JBoss. That’s really all it is – just another optional module for Apache that can forward the requests you specify from the HTTP server to a back-end application server, like JBoss


Since you’ve already used APT to install Apache, it makes sense to use it again to install mod_jk! The package in Ubuntu’s repositories is called libapache2-mod-jk and is installed as follows:

	sudo apt-get install libapache2-mod-jk  

	
	dmora@ubuntuhr:/etc/libapache2-mod-jk$ ls -la
	total 20
	drwxr-xr-x   2 root root  4096 sep 13 16:39 .
	drwxr-xr-x 123 root root 12288 sep 13 16:39 ..
	lrwxrwxrwx   1 root root    33 oct 21  2013 httpd-jk.conf -> ../apache2/mods-available/jk.conf
	-rw-r--r--   1 root root  2946 jul 23  2010 workers.properties
	


## Security
- SSL



## Zero-downtime deployment
 - Tomcat Session persistence
 - Steps for making a change in a class file

## Continuous Integration:
 - Jenkins
 - Jmeter

## Monitoring
 - cactis?

## Linux

restart networking

Download a file from the web:


    wget http://www.sevenacross.com/photos.zip


### Compress/decompress folder
For example, you have directory called /home/jerry/prog and you would like to compress this directory then you can type tar command as follows:

    $ tar -zcvf prog-1-jan-2005.tar.gz /home/jerry/prog

Above command will create an archive file called prog-1-jan-2005.tar.gz in current directory. If you wish to restore your archive then you need to use following command (it will extract all files in current directory):

    $ tar -zxvf prog-1-jan-2005.tar.gz

Symbolic Link

	$ ln -s /path/to/file /path/to/symlink


###logs
The tail Command

The reverse of head is tail. Using tail, you can view the last ten lines of a file. This can be useful for viewing the last 10 lines of a log file for important system messages. You can also use tail to watch log files as they are updated. Using the -f option, tail automatically print new messages from an open file to the screen in real-time. For example, to actively watch /var/log/messages, type the following at a shell prompt as the root user:

	$tail -f /var/log/messages



## VIM



	:w	save
	:wq	save and quit
	:q!	quit without saving
	
	Move
	1G: move cursor to the start of file
	G: move cursor to the end of file

	Delete
	dG :delete from cursor to end of file
	dd : delete current line



Edit

p : paste


Paste from Clipboard
:set noai       /* para evitar el auto indent*/
:i
right mouse button /*pega del clipboard*/
Search/Replace
Interactive Find and Replace in Vim Editor
You can perform interactive find and replace using the ‘c’ flag in the substitute, which will ask for confirmation to do substitution or to skip it as explained below. In this example, Vim editor will do a global find the word ‘awesome’ and replace it with ‘wonderful’. But it will do the replacement only based on your input as explained below.
:%s/awesome/wonderful/gc

replace with wonderful (y/n/a/q/l/^E/^Y)?
y – Will replace the current highlighted word. After replacing it will automatically highlight the next word that matched the search pattern
n – Will not replace the current highlighted word. But it will automatically highlight the next word that matched the search pattern
a – Will substitute all the highlighted words that matched the search criteria automatically.
l – This will replace only the current highlighted word and terminate the find and replace effort.


Cut/copy and paste using visual selection  Edit  
Please review this tip:
This tip was imported from vim.org and needs general review.
You might clean up comments or merge similar tips.
Add suitable categories so people can find the tip.
Please avoid the discussion page (use the Comments section below for notes).
If the tip contains good advice for current Vim, remove the {{review}} line.
Tip 386 Printable Previous Next
created 2002 · complexity basic · author rainbrot · version 6.0
Visual selection, although common in applications today, is a key feature that differentiates vim from traditional vi.
To cut (or copy) and paste using visual selection:

Position the cursor at the beginning of the text you want to cut/copy.
Press v to begin character-based visual selection (or upper case V to select whole lines, or Ctrl-v for a vertical block).
Move the cursor to the end of the text to be cut/copied. (While selecting text, you can perform searches and other advanced movement, a feature that sets vim apart from most other editors.)
Press d (as in "delete") to cut, or y (as in "yank", which I imagine meaning "yank so hard and fast that it leaves a copy behind") to copy.
Move the cursor to the desired paste location.
Press p to paste after the cursor, or P to paste before.


Layout

:sp /*horizontal */
:vsp    /* vertical */
:sp file 
:vsp file

Switch Windows
command mode: Ctrl-w + flecha


## Credits


- http://blog.c2b2.co.uk/2014/04/how-to-set-up-cluster-with-tomcat-8.html
- http://blog.c2b2.co.uk/2013/10/how-to-install-apache-and-modjk.html
