# Android-Freeciv - Copyright (C) 2010 - C Vaughn
#   This program is free software; you can redistribute it and/or modify
#   it under the terms of the GNU General Public License as published by
#   the Free Software Foundation; either version 2, or (at your option)
#   any later version.
#
#   This program is distributed in the hope that it will be useful,
#   but WITHOUT ANY WARRANTY; without even the implied warranty of
#   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#   GNU General Public License for more details.

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := client

CG_SUBDIRS := \
. \
agents \
gui-android \
javaharness

LOCAL_SRC_FILES += $(foreach F, $(CG_SUBDIRS), $(addprefix $(F)/,$(notdir $(wildcard $(LOCAL_PATH)/$(F)/*.c))))
LOCAL_SRC_FILES += $(foreach F, $(CG_SUBDIRS), $(addprefix $(F)/,$(notdir $(wildcard $(LOCAL_PATH)/$(F)/*.cpp))))

LOCAL_CFLAGS :=	-g -I$(LOCAL_PATH)/../ai \
				-I$(LOCAL_PATH)/../common \
				-I$(LOCAL_PATH)/../utility \
				-I$(LOCAL_PATH)/../common/aicore \
				-I$(LOCAL_PATH)/../server \
				-I$(LOCAL_PATH)/../server/generator \
				-I$(LOCAL_PATH)/../server/scripting \
				-I$(LOCAL_PATH)/include \
				-I$(LOCAL_PATH)/javaharness \
				-I$(LOCAL_PATH)/../include \
				-I$(LOCAL_PATH)/agents \
				-I$(LOCAL_PATH)/../../libpng \
				-I$(LOCAL_PATH)/../../skia/include/core \
				-I$(LOCAL_PATH)/../../skia/include/images \
				-I$(LOCAL_PATH)/../../skia/src/core \
				-I$(LOCAL_PATH)/../../skia/include/effects \
				-I$(LOCAL_PATH)/../../skia/include/utils \
				-I$(LOCAL_PATH)/../../utils/include \
				-I$(LOCAL_PATH)/../../cutils/include \
				-DHAVE_NETINET_IN_H \
				-DHAVE_SYS_SELECT_H \
				-DHAVE_SYS_SOCKET_H \
				-DHAVE_SYS_TIME_H \
				-DHAVE_SYS_TYPES_H \
				-DHAVE_UNISTD_H	\
				-DHAVE_SOCKLEN_T \
                -DHAVE_CONFIG_H \
                -DHAVE_ARPA_INET_H \
                -DHAVE_NETDB_H \
                -DHAVE_PWD_H \
                -DHAVE_SYS_UIO_H \
                -DHAVE_SYS_UTSNAME_H \
                -DHAVE_LIBZ
                
                								
LOCAL_STATIC_LIBRARIES := common utility png  

LOCAL_LDLIBS := -lz -llog -lGLESv1_CM

include $(BUILD_SHARED_LIBRARY)