#include <stdlib.h>
#include <mutex>
#include <Task.h>
#include "../include/connectionHandler.h"

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
    std::string host = "0.0.0.0"; // argv[1];
    short port = atoi("7777");//(argv[2]); //server port
    mutex mutex;
    ConnectionHandler connectionHandler(host, port);
    Task sendsToServer(mutex, host, port);
    thread taskThread(&Task::run, &sendsToServer);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    //From here we will see the rest of the ehco client implementation:
    while (1) {

        // We can use one of three options to read data from the server:
        // 1. Read a fixed number of characters
        // 2. Read a line (up to the newline character using the getline() buffered reader
        // 3. Read up to the null character
        std::string answer; //

        // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
        // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
        if (!connectionHandler.getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        int len=answer.length();
        // A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
        // we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
        answer.resize(len-1);
        answer=answerReader(answer);
        std::cout << "Reply: " << answer << " " << len << " bytes " << std::endl;
        if (answer == "bye") {
            std::cout << "Exiting...\n" << std::endl;
            break;
        }
    }
    return 0;
}


