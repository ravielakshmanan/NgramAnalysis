import sys, getopt
import random
import string
from scapy.all import *

#Function to generate random 10 character strings
def stringGenerator(size=10, chars=string.ascii_uppercase + string.digits):
   return ''.join(random.choice(chars) for _ in range(size))

def main(argv):
   #initialize the values of source and destination ports
   sourcePort = 5000;
   destPort = 4000;

   #parse the input arguments
   try:
      opts, args = getopt.getopt(argv,"hs:d:",["sourcePort=","destPort="])
   except getopt.GetoptError:
      print 'Wrong argument passed.'
      print 'Usage: scapyPort.py -s <sourcePort> -d <destPort>'
      sys.exit(2)
   for opt, arg in opts:
      if opt == '-h':
         print 'Usage: scapyPort.py -s <sourcePort> -d <destPort>'
         sys.exit()
      elif opt in ("-s", "--sourcePort"):
         sourcePort = arg
      elif opt in ("-d", "--destPort"):
         destPort = arg

   #print the port numbers passed
   print("Source Port Passed is " + str(sourcePort))
   print("Destination Port Passed is " + str(destPort))

   #instantiate the IP layer
   ipLayer=IP(src='127.0.0.1',dst='127.0.0.1')

   #Loop through the destination ports 3000 to 3021 to send the packets
   for destnPort in range(3000,3021):
        transportLayer=TCP(sport=int(sourcePort),dport=int(destnPort))
        packet=ipLayer/transportLayer
        print("Sending packet to destination port: " + str(destnPort))
        packet.show()
        send(packet)

   #Send 5 packets with random strings
   for packetCount in range(0,5):
        data = stringGenerator()
        print("Random data string generated to send to port "+ str(destPort) + ": " + data)
        transportLayer=TCP(sport=int(sourcePort),dport=int(destPort))
        packet=ipLayer/transportLayer/Raw(load=data)
        packet.show()
        send(packet)

if __name__ == "__main__":
   main(sys.argv[1:])