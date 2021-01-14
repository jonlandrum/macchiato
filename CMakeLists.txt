cmake_minimum_required(VERSION 3.17)
project(macchiato)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_INCLUDE_CURRENT_DIR ON)

set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTOUIC ON)
set(CMAKE_AUTORCC ON)

find_package(Qt5Widgets REQUIRED)
set(QT_VERSION 5)
set(REQUIRED_LIBS Core)
set(REQUIRED_LIBS_QUALIFIED Qt5::Core)

add_executable(${PROJECT_NAME}
        lib/qautostart/src/qautostart.cpp
        lib/qautostart/src/qautostart.h
        src/macchiato.cpp
        src/macchiato.h
        src/main.cpp
        macchiato.qrc)
target_link_libraries(macchiato Qt5::Widgets)