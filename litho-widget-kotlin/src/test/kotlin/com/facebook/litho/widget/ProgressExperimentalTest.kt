/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.facebook.litho.kotlin.widget

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.facebook.litho.Style
import com.facebook.litho.core.height
import com.facebook.litho.core.width
import com.facebook.litho.px
import com.facebook.litho.testing.LithoViewRule
import com.facebook.litho.testing.assertj.LithoAssertions.assertThat
import com.facebook.litho.widget.ProgressView
import junit.framework.Assert.assertNotNull
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode

/** Tests for [ProgressComponent] */
@LooperMode(LooperMode.Mode.LEGACY)
@RunWith(AndroidJUnit4::class)
class ProgressExperimentalTest {

  @Rule @JvmField val lithoViewRule = LithoViewRule()

  @Test
  fun `ProgressComponent should render`() {
    val testLithoView =
        lithoViewRule.render { ProgressExperimental(style = Style.width(100.px).height(100.px)) }

    // should find an ProgressExperimental in the tree
    assertNotNull(testLithoView.findComponent(ProgressExperimental::class))

    // should mount an ProgressExperimental
    assertThat(testLithoView.lithoView.mountItemCount).isEqualTo(1)

    val content = testLithoView.lithoView.getMountItemAt(0).content as ProgressView
    assertThat(content.measuredWidth).isEqualTo(100)
    assertThat(content.measuredHeight).isEqualTo(100)
  }

  @Test
  fun `same instance should be equivalent`() {
    val component = ProgressExperimental()
    val component2 = ProgressExperimental()

    assertThat(component).isEquivalentTo(component2)
    assertThat(component).isEquivalentTo(component2, true)
  }

  @Test
  fun `components with same prop values should be equivalent`() {
    val colorDrawable = ColorDrawable(Color.RED)
    val color = Color.BLACK
    val firstProgressWithColorDrawable = ProgressExperimental(indeterminateDrawable = colorDrawable)
    val secondProgressWithColorDrawable =
        ProgressExperimental(indeterminateDrawable = colorDrawable)
    val firstProgressWithColor = ProgressExperimental(color = color)
    val secondProgressWithColor = ProgressExperimental(color = color)
    val firstProgressWithBothParams =
        ProgressExperimental(indeterminateDrawable = colorDrawable, color = color)
    val secondProgressWithBothParams =
        ProgressExperimental(indeterminateDrawable = colorDrawable, color = color)

    assertThat(firstProgressWithColorDrawable).isEquivalentTo(secondProgressWithColorDrawable)
    assertThat(firstProgressWithColorDrawable).isEquivalentTo(secondProgressWithColorDrawable, true)
    assertThat(firstProgressWithColor).isEquivalentTo(secondProgressWithColor)
    assertThat(firstProgressWithColor).isEquivalentTo(secondProgressWithColor, true)
    assertThat(firstProgressWithBothParams).isEquivalentTo(secondProgressWithBothParams)
    assertThat(firstProgressWithBothParams).isEquivalentTo(secondProgressWithBothParams, true)
  }
}
