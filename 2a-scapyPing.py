import sys
from scapy.all import *

#Set the destination to the machine you want to ping
#Create the packet
packet = IP(dst='209.2.234.198')

#Specify the ICMP protocol
icmp = ICMP(type=8,code=0)
data = "hello from Cloud VM!"

#get the response
response= sr1(packet/icmp/data)
response.summary()
response.show()