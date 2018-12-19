Assignment 4: Keyur Kirti Mehta

Main classes are as follows:
Lamport.java\
IMasterObj.java\
MasterObj.java\
LamportPo.java\
IProcessObj.java\
ProcessObj.java\
Queue.java\
Policy

=================================================
makefile will compile all the java files and create respective class files

Below are the steps to execute the application:

1. Login to 'in-csci-rrpc01.cs.iupui.edu' (10.234.136.55) machine of CSCI to execute the server using valid credentials.
	a. go to assignment folder. In this case it will be MehtaA4 using command 'cd MehtaA4'
	b. complie and create corresponding class files using command 'make'
	c. start the rmi registry on port 2089 by command 'rmiregistry 2089&'
	d. to execute the server use 'java -Djava.security.policy=policy Lamport'
		It will start the server and gives prompt to enter 'y' to start Lamport's logic. But before that start all 
		the POs as mentioned below and then hit 'y' to start server. 
		(This is because server will lookup for POs id in registry) 

2. Login to below mentioned machine of CSCI to execute the client using valid credentials. Repeat all the steps
   in 4 rrpc machine starting from 2-5 i.e.
   PO2 - in-csci-rrpc02.cs.iupui.edu on port 2089
   PO3 - in-csci-rrpc03.cs.iupui.edu on port 2089
   PO4 - in-csci-rrpc04.cs.iupui.edu on port 2089
   PO5 - in-csci-rrpc05.cs.iupui.edu on port 2089

	a. go to assignment folder of each machine. In this case it will be MehtaA4 using command 'cd MehtaA4'
	b. start the rmi registry on port 2089 by command 'rmiregistry 2089&'
	c. to execute client use 'java -Djava.security.policy=policy LamportPo 4 50'

(1st argument is for probability of how frequent the clock should sync. Lesser the probability it will sync frequent.
Value should be between 0-9 ideally 9 is Byzantine failure so it should be 8
2nd argument is no of event per iteration)

It will start the PO and gives prompt to enter 'y' to start Lamport's logic. But before that start all the POs. 

Follow the below mentioned steps: 
1. start rmi registry in all machines
2. start server
3. start all POs 1 on each machine
4. Press 'y' on server and hit enter
5. Press 'y' on each machine and hit enter. (Do this asap)

To stop the server/PO, press ctrl + c
=================================================
Execution:

Simulation will start and save the logical clock value in the 'Process_LogiClk.txt' file at same location.
User can change the t (number of events per iteration from argument),probability (by argument) and 
number of iteration (number of events per iteration from code change) to run during execution from 
LamportPo.java file.
