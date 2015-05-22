cmake_minimum_required(VERSION 3.2)
project(rasp_home)

SET(CMAKE_CXX_FLAGS "-pthread -lwiringPi")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES main.cpp)
add_executable(rasp_home ${SOURCE_FILES})