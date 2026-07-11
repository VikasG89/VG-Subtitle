cmake_minimum_required(VERSION 3.22.1)
project(vgsubtitle LANGUAGES C CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Force Release build for maximum performance
set(CMAKE_BUILD_TYPE Release CACHE STRING "Choose the type of build" FORCE)

# Enable aggressive compiler optimizations
if(MSVC)
    add_compile_options(/O2)
else()
    add_compile_options(-O3 -ffast-math -fno-finite-math-only -DNDEBUG)
endif()

set(BUILD_SHARED_LIBS OFF CACHE BOOL "" FORCE)
set(WHISPER_BUILD_TESTS OFF CACHE BOOL "" FORCE)
set(WHISPER_BUILD_EXAMPLES OFF CACHE BOOL "" FORCE)
set(WHISPER_BUILD_SERVER OFF CACHE BOOL "" FORCE)
set(WHISPER_CURL OFF CACHE BOOL "" FORCE)
set(GGML_NATIVE OFF CACHE BOOL "" FORCE)
set(GGML_OPENMP OFF CACHE BOOL "" FORCE)

# Enable ARM optimizations for Android
if(ANDROID)
    if(${ANDROID_ABI} STREQUAL "arm64-v8a")
        set(GGML_ARM_FMA ON CACHE BOOL "" FORCE)
        set(GGML_NEON ON CACHE BOOL "" FORCE)
    elseif(${ANDROID_ABI} STREQUAL "armeabi-v7a")
        set(GGML_NEON ON CACHE BOOL "" FORCE)
    endif()
    set(GGML_I_AM_DEVELOPER ON CACHE BOOL "" FORCE)
endif()

add_subdirectory(whisper.cpp whisper-build)

file(GLOB_RECURSE SRC_FILES
    src/*.cpp
    vad/src/*.cpp
    *.cpp
)

add_library(vgsubtitle SHARED ${SRC_FILES})

target_include_directories(vgsubtitle PRIVATE
        include
        vad/include
        whisper.cpp/include
        whisper.cpp/ggml/include)

target_link_libraries(vgsubtitle PRIVATE
        whisper
        android
        log)
