cmake_minimum_required(VERSION 3.2)
project(cserver)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES main.cpp)
add_executable(cserver ${SOURCE_FILES})