cmake_minimum_required(VERSION 3.16)
project(Boost_Echo_Client)

set(Boost_USE_STATIC_LIBS OFF)
set(Boost_USE_MULTITHREADED ON)
set(Boost_USE_STATIC_RUNTIME OFF)
#[[find_package(Boost 1.45.0 COMPONENTS *boost libraries here*)

if(Boost_FOUND)
    include_directories(${Boost_INCLUDE_DIRS})
    add_executable(progname file1.cxx file2.cxx)
    target_link_libraries(progname ${Boost_LIBRARIES})
endif()]]

set(CMAKE_CXX_STANDARD 11)
link_libraries("-lboost_system")
link_libraries("-pthread")
include_directories(include)

add_executable(Boost_Echo_Client
        include/connectionHandler.h
        src/connectionHandler.cpp
        src/BGRSclient.cpp src/Task.cpp include/Task.h)
