//
// Created by spl211 on 03/01/2021.
//

#include "connectionHandler.h"
#include "Task.h"

using namespace std;
//todo remove flag and stopp
Task::Task(ConnectionHandler& connectionHandler, std::mutex& mutex, condition_variable& cv): connectionHandler(connectionHandler), mutex(mutex), cv(cv), shouldterminate(false){};
int Task::twoSpacesCase(string line, char *lineAsChar){
    line = line.substr(line.find(" ") + 1, line.length());
    int size=line.length();
    for(int i=2;i<size+2;i++){
        if(line[i-2]!=' ')
            lineAsChar[i]=line[i-2];
        else
            lineAsChar[i]='\0';
    }
    lineAsChar[line.length()+2] = '\0';
    return line.length()+3;
}

int Task::fourBytesCase(string line, char* lineAsChar){
    line = line.substr(line.find(" ")+1);
    int num = atoi(line.c_str());
    lineAsChar[2]=((num>>8)&0xFF);
    lineAsChar[3]= (num&0xFF);

    return 4;
}

//this method changes the user input to the required command;
int Task::getCommandInOpcode(string line , char *lineAsChar){
    string op = line.substr(0, line.find(" "));
    string toReturn = "";
    if(op.compare("ADMINREG") == 0) {
        lineAsChar[0] = 0;
        lineAsChar[1] = 1;
        return twoSpacesCase(line,lineAsChar);
    }
    if(op.compare("STUDENTREG") == 0){
        lineAsChar[0] = 0;
        lineAsChar[1] = 2;
        return twoSpacesCase(line,lineAsChar);
    }
    if(op.compare("LOGIN") == 0){
        lineAsChar[0] = 0;
        lineAsChar[1] = 3;
        return twoSpacesCase(line,lineAsChar);
    }if(op.compare("LOGOUT") == 0){
        lineAsChar[0] = 0;
        lineAsChar[1] = 4;
        return 2;
    }
    if(op.compare("COURSEREG") == 0){
        lineAsChar[0] = 0;
        lineAsChar[1] = 5;
        return fourBytesCase(line, lineAsChar);
    }
    if(op.compare("KDAMCHECK") == 0){
        lineAsChar[0] = 0;
        lineAsChar[1] = 6;
        return fourBytesCase(line, lineAsChar);
    }
    if(op.compare("COURSESTAT") == 0){
        lineAsChar[0] = 0;
        lineAsChar[1] = 7;
        return fourBytesCase(line, lineAsChar);
    }
    if(op.compare("STUDENTSTAT") == 0){
        lineAsChar[0] = 0;
        lineAsChar[1] = 8;
        line = line.substr(line.find(" ") + 1);
        int size=line.length();
        for(int i= 2; i< size +2; i++){
            lineAsChar[i]=line[i-2];
        }
        lineAsChar[line.length()+2] = '\0';
        return line.length()+3;
    }
    if(op.compare("ISREGISTERED") == 0){
        lineAsChar[0] = 0;
        lineAsChar[1] = 9;
        return fourBytesCase(line, lineAsChar);
    }
    if(op.compare("UNREGISTER") == 0){
        lineAsChar[0] = 1;
        lineAsChar[1] = 0;
        return fourBytesCase(line, lineAsChar);
    }
    if(op.compare("MYCOURSES") == 0){
        lineAsChar[0] = 1;
        lineAsChar[1] = 1;
        return 2;
    }
    return -1;
}

void Task::run() {

    while (!shouldterminate) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        char lineAsChar[line.length()];
        int len = getCommandInOpcode(line, lineAsChar);

        if (!connectionHandler.sendBytes(lineAsChar, len)) {
            break;
        }

        if(line == "LOGOUT"){
            std::unique_lock<std::mutex>lk(mutex);
            cv.wait(lk);
        }
    }
}

void Task::shouldTerminate(){shouldterminate = true;}
