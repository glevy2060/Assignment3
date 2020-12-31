LDFLAGS:=-lboost_system

# All Targets
all: bin/BGRSclient

# Tool invocations
# Executable "BGRSclient" depends on the files BGRSclient.o connectionHandler.o echoClient.o
bin/BGRSclient: bin/BGRSclient.o bin/connectionHandler.o bin/echoClient.o
	@echo 'Building target: BGRSclient'
	@echo 'Invoking: C++ Linker'
	g++ -o bin/BGRSclient bin/BGRSclient.o bin/connectionHandler.o bin/echoClient.o $(LDFLAGS)
	@echo 'Finished building target: BGRSclient'
	@echo ' '

#BGRSclient.o Depends on the source and header files src/BGRSclient.cpp include/connectionHandler.h
bin/BGRSclient.o: src/BGRSclient.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/BGRSclient.o src/BGRSclient.cpp

#connectionHandler.o Depends on the source and header files src/connectionHandler.cpp include/connectionHandler.h
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/connectionHandler.o src/connectionHandler.cpp $(LDFLAGS)

#echoClient.o Depends on the source and header files src/echoClient.cpp include/echoClient.h
bin/echoClient.o: src/echoClient.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/echoClient.o src/echoClient.cpp


#Clean the build directory
clean:
	rm -f bin/*