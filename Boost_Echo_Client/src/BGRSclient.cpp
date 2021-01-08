#include <stdlib.h>
#include <mutex>
#include <Task.h>
#include <connectionHandler.h>


/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
using namespace std;
int twoSpacesCase(string line, char *lineAsChar){
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

int fourBytesCase(string line, char* lineAsChar){
    line = line.substr(line.find(" ")+1);
    int num = atoi(line.c_str());
    lineAsChar[2]=((num>>8)&0xFF);
    lineAsChar[3]= (num&0xFF);

    return 4;
}

//this method changes the user input to the required command;
int getCommandInOpcode(string line , char *lineAsChar){
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

string answerReader(string answer){
        string toReturn="";
        string op=answer.substr(0,2);
        string messageOp=answer.substr(2,2);
        if(messageOp[0]=='0')
            messageOp=messageOp[1];
        if(op.compare("12") == 0){
            toReturn+="ACK "+messageOp+answer.substr(4);

        } else if(op.compare("13") == 0){
            toReturn+="ERROR "+messageOp;
        }
    return toReturn;
}

int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]); //server port
/*    std::string host = "0.0.0.0";
    short port = atoi("7777"); //server port*/

    ConnectionHandler connectionHandler(host, port);
    std::vector<string> vec;
    Task task(connectionHandler,vec);
    thread taskThread(&Task::run, &task);

    if (!connectionHandler.connect()) {
        return 1;
    }

    while (1) {
        while(vec.empty()){}
        string line=vec[0];
        vec.erase(vec.begin());
        char lineAsChar[line.length()];
        int len = getCommandInOpcode(line, lineAsChar);
        if (!connectionHandler.sendBytes(lineAsChar, len)) {
            break;
        }

        std::string answer; //

        if (!connectionHandler.getLine(answer)) {
            break;
        };

        //unlock
        answer=answerReader(answer);
        std::cout << answer << std::endl;

        if (answer[4] == '4'){
            task.shouldTerminate();
            task.flagChanger();
            taskThread.join();
            break;
        } else if (answer == "ERROR 4"){
            task.flagChanger();
        }

    }
    return 0;
}


