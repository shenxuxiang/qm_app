package com.example.qm_app.common

object RegularPattern {
    /**
     * 验证模式：手机号码验证
     * */
    val userPhone = Regex("^1[3456789][0-9]{9}")

    /**
     * 验证模式：【用户登录、用户注册】验证码
     * */
    val verificationCode = Regex("[0-9]{6}")

    /**
     * 密码验证规则
     * */
    val password =
        Regex("""^(?=.*\d+)(?=.*[a-zA-Z]+)(?=.*[~!@#$%^&*\.]+)[~!@#$%^&*\.0-9a-zA-Z]{6,18}""")
}