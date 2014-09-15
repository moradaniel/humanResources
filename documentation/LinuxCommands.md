# Linux Commands

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

###Security

Users
list users

	$less /etc/passwd or $cat /etc/passwd 

create user

display user

The id command displays the user’s UID and all the user’s groups and GIDs.

	$id username


delete user

	//-r to delete home folder
	userdel -r vivek

The groups command also displays all the user’s groups:
	
	$ groups username


Check Groups
	
	cat /etc/group

##Ports
Linux List The Open Ports And The Process That Owns Them
	sudo lsof -i

##logs
The tail Command

The reverse of head is tail. Using tail, you can view the last ten lines of a file. This can be useful for viewing the last 10 lines of a log file for important system messages. You can also use tail to watch log files as they are updated. Using the -f option, tail automatically print new messages from an open file to the screen in real-time. For example, to actively watch /var/log/messages, type the following at a shell prompt as the root user:

	$tail -f /var/log/messages


