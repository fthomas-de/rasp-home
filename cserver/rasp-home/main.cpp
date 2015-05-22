#include <iostream>
#include "RCSwitch.h"
#include <stdlib.h>
#include <stdio.h>

//using namespace std;

int main(int argc, char *argv[]) {
    int PIN = 0;
    char* systemCode = argv[1];
    int unitCode = atoi(argv[2]);
    int command  = atoi(argv[3]);

    if (wiringPiSetup () == -1) return 1;
    printf("sending systemCode[%s] unitCode[%i] command[%i]\n", systemCode, unitCode, command);
    RCSwitch mySwitch = RCSwitch();

}