LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := tolua

LOCAL_CPP_EXTENSION := .cpp

CG_SUBDIRS := \
src/lib

LOCAL_SRC_FILES += $(foreach F, $(CG_SUBDIRS), $(addprefix $(F)/,$(notdir $(wildcard $(LOCAL_PATH)/$(F)/*.c))))

LOCAL_CFLAGS :=	-I$(LOCAL_PATH)/include \
                -I$(LOCAL_PATH)/../lua-5.1/src

include $(BUILD_STATIC_LIBRARY)

