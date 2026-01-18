package com.example.qm_app.common

sealed class QmIcons(val icon: String) {
    object Stroke {
        object Warn : QmIcons("\ue62b")
        object Empty : QmIcons("\ue6a6")
        object Back : QmIcons("\ue63c")
        object Forward : QmIcons("\ue63d")
        object Up : QmIcons("\ue62f")
        object Down : QmIcons("\ue62c")
        object AngleDownward : QmIcons("\ue642")
        object AngleUpward : QmIcons("\ue643")
        object Location : QmIcons("\ue620")
        object Position : QmIcons("\ue631")
        object Close : QmIcons("\ue623")
        object Delete : QmIcons("\ue626")
        object Edit : QmIcons("\ue624")
        object Refresh : QmIcons("\ue630")
        object Plus : QmIcons("\ue612")
        object Minus : QmIcons("\ue611")
        object Light : QmIcons("\ue64e")
        object File : QmIcons("\ue63e")
        object Confirm : QmIcons("\ue613")
        object Phone : QmIcons("\ue621")
        object Company : QmIcons("\ue622")
        object Modify : QmIcons("\ue625")
        object Visible : QmIcons("\ue608")
        object Invisible : QmIcons("\ue604")
        object Setting : QmIcons("\ue602")
        object Filter : QmIcons("\ue610")
        object Search : QmIcons("\ue603")
        object CloseCircle : QmIcons("\ue601")
        object Pause : QmIcons("\ue64d")
        object Play : QmIcons("\ue648")
        object Success : QmIcons("\ue628")
        object FlashLight : QmIcons("\ue64e")

        // 底部菜单栏未选中
        object TabBarMenuHome : QmIcons("\ue61a")
        object TabBarMenuService : QmIcons("\ue619")
        object TabBarMenuRelease : QmIcons("\ue616")
        object TabBarMenuAgricultural : QmIcons("\ue615")
        object TabBarMenuMine : QmIcons("\ue618")
    }

    object Filled {
        object Close : QmIcons("\ue627")
        object Video : QmIcons("\ue64a")
        object Capture : QmIcons("\ue64c")

        // 底部菜单栏选中
        object TabBarMenuHome : QmIcons("\ue61c")
        object TabBarMenuService : QmIcons("\ue61d")
        object TabBarMenuRelease : QmIcons("\ue617")
        object TabBarMenuAgricultural : QmIcons("\ue614")
        object TabBarMenuMine : QmIcons("\ue61b")
    }
}