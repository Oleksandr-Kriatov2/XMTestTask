package com.epam.myapplication

import com.epam.myapplication.common.UiDataEvent
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class UiDataEventTest : StringSpec({

    "get content if not handled" {
        val data = "data"
        val event = UiDataEvent(data)
        event.getContentIfNotHandled() shouldBe data
        event.getContentIfNotHandled() shouldBe null
    }
})