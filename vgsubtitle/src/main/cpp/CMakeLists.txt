cmake_minimum_required(VERSION 3.22.1)
project(vgsubtitle LANGUAGES C CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Force Release build for maximum performance
if(NOT CMAKE_BUILD_TYPE)
    set(CMAKE_BUILD_TYPE Release)
endif()

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

message(STATUS "VGSubtitle sources:")
foreach(src ${SRC_FILES})
    message(STATUS "  ${src}")
endforeach()

set(ROOT_CPP
        ${CMAKE_CURRENT_SOURCE_DIR}/jni_bridge.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/jni_cache.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/jni_engine.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/jni_exception.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/jni_export.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/jni_ffmpeg.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/jni_inference.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/jni_logging.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/jni_media_probe.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/jni_model.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/jni_model_loader.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/jni_utils.cpp
)


file(GLOB_RECURSE ENGINE_CPP
        "${CMAKE_CURRENT_SOURCE_DIR}/src/*.cpp"
)

file(GLOB_RECURSE VAD_CPP
        "${CMAKE_CURRENT_SOURCE_DIR}/vad/src/*.cpp"
)

set(SRC_FILES
        ${ROOT_CPP}
        ${ENGINE_CPP}
        ${VAD_CPP}
)

add_library(vgsubtitle SHARED ${SRC_FILES})

target_compile_options(vgsubtitle PRIVATE
        -O3
        -ffast-math
)

target_include_directories(vgsubtitle PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR}
        ${CMAKE_CURRENT_SOURCE_DIR}/include
        ${CMAKE_CURRENT_SOURCE_DIR}/vad/include
        ${CMAKE_CURRENT_SOURCE_DIR}/whisper.cpp/include
        ${CMAKE_CURRENT_SOURCE_DIR}/whisper.cpp/ggml/include
        )

target_link_libraries(vgsubtitle PRIVATE
        whisper
        android
        log)
