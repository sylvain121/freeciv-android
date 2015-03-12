
APP_CFLAGS += -Wno-error=format-security
LOCAL_DISABLE_FORMAT_STRING_CHECKS := true
include $(call all-subdir-makefiles)
