package com.epam.myapplication

import com.epam.myapplication.common.UiEvent
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class UiEventTest : StringSpec({

    "get content is handled" {
        val event = UiEvent()
        event.notHandedYet shouldBe true
        event.hasBeenHandled shouldBe false
        event.handle()
        event.notHandedYet shouldBe false
        event.hasBeenHandled shouldBe true
    }
})