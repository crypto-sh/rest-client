package com.github.library.enums


enum class RequestBodyType(value: Int) {

    FormData(0),
    FormUrlEncode(1),
    RawJSON(2),
    RawTEXTPlain(3),
    Binary(4),
    MultiPart(5);

    internal var value = -1

    init {
        this.value = value
    }

    companion object {

        fun Parse(value: Int): RequestBodyType {
            if (value == 0) {
                return FormData
            }
            val `arr$` = values()
            for (`val` in `arr$`) {
                if (`val`.value == value) {
                    return `val`
                }
            }
            return FormData
        }
    }

}
