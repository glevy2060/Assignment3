#include <stdlib.h>
#include <mutex>
#include <Task.h>
#include <connectionHandler.h>


/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
using namespace std;

string answerReader(string answer){
        string toReturn="";
        string op=answer.substr(0,2);
        string messageOp=answer.substr(2,4);
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
/*    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }*/
/*
    std::string host = argv[1];
    short port = atoi(argv[2]); //server port
*/

    std::string host = "0.0.0.0";
    short port = atoi("7777"); //server port

    bool flag=false;
    mutex mutex;
    ConnectionHandler connectionHandler(host, port);
    Task task(mutex, connectionHandler,flag);
    thread taskThread(&Task::run, &task);

    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    //From here we will see the rest of the ehco client implementation:
    while (1) {
        std::string answer; //

        //lock
        if (!connectionHandler.getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        //unlock
        answer=answerReader(answer);
        std::cout << answer << std::endl;

        if (answer[4] == '4') {
            std::cout << "Exiting...\n" << std::endl;
            taskThread.detach();
            break;
        }else if(answer.compare("ERROR 4") == 0){
            flag=false;
        }
    }
    return 0;
}


