cmake_minimum_required(VERSION 3.14)

project(qtone LANGUAGES CXX)

set(CMAKE_AUTOUIC ON)
set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

find_package(QT NAMES Qt6 Qt5 REQUIRED COMPONENTS Core)
find_package(Qt${QT_VERSION_MAJOR} REQUIRED COMPONENTS Core)

add_executable(qtone
  main.cpp
  application.h application.cpp
  matrix.h matrix.cpp
  number.h
)
target_link_libraries(qtone Qt${QT_VERSION_MAJOR}::Core)

install(TARGETS qtone
    LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR})
