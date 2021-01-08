//
// Created by spl211 on 03/01/2021.
//

#include "connectionHandler.h"
#include "Task.h"
using namespace std;
//todo remove flag and stopp
Task::Task(ConnectionHandler& connectionHandler,std::vector<string> &vec): connectionHandler(connectionHandler),  shouldterminate(false), flag(false),vec(vec){};

void Task::shouldTerminate(){shouldterminate=true;}
void Task::flagChanger(){flag=false;}

void Task::run() {
    while (!shouldterminate) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        vec.push_back(line);
        if(line=="LOGOUT")
            flag=true;
        while(flag){}
    }
}