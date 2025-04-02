cmake_minimum_required(VERSION 3.10.2)

add_library( # Library name
        getme
        SHARED
        getme.cpp )

find_library( # Locate the Android NDK log library
        log-lib
        log )

target_link_libraries( # Link the libraries
        getme
        ${log-lib} )

cmake_minimum_required(VERSION 3.4.1)

add_library(native-lib SHARED native-lib.cpp)

find_library(log-lib log)

target_link_libraries(native-lib ${log-lib})


cmake_minimum_required(VERSION 3.4.1)

add_library(
        license
        SHARED
        license.cpp)

find_library(
        log-lib
        log)

target_link_libraries(
        license
        ${log-lib})

