import sys
from scapy.all import *

#Read the inputs from the file
with open("inpartb.txt") as file:
        lines = file.read().splitlines()

srcPort = int(lines[0].split(' ')[1])
dstPort = int(lines[1].split(' ')[1])

srcIP = str(lines[2].split(' ')[1])
dstIP = str(lines[3].split(' ')[1])

print("The source port passed is :" + str(srcPort));
print("The destination port passed is :" + str(dstPort));
print("The source IP address passed is :" + srcIP);
print("The destination IP address  passed is :" + dstIP);

#Send and get the response
getRequest = 'GET /manpage.html HTTP/1.1\r\nHost: ssdeep.sourceforge.net\r\n\r\n'
request = IP(dst=dstIP) / TCP(dport=dstPort, sport=srcPort, flags='A') / getRequest
reply = sr1(request)
reply.show()