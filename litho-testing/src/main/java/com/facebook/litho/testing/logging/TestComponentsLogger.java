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

package com.facebook.litho.testing.logging;

import androidx.annotation.Nullable;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentsLogger;
import com.facebook.litho.FrameworkLogEvents;
import com.facebook.litho.PerfEvent;
import com.facebook.litho.TestPerfEvent;
import com.facebook.litho.TreeProps;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * In test environments we don't want to recycle the events as mockity hold on to them. We therefor
 * override log() to not call release.
 */
public class TestComponentsLogger implements ComponentsLogger {

  private final List<PerfEvent> mLoggedPerfEvents = new LinkedList<>();
  private final List<PerfEvent> mCanceledPerfEvents = new LinkedList<>();

  @Override
  public boolean isTracing(PerfEvent logEvent) {
    return true;
  }

  @Override
  public PerfEvent newPerformanceEvent(
      ComponentContext c, @FrameworkLogEvents.LogEventId int eventId) {
    return new TestPerfEvent(eventId);
  }

  @Override
  public void cancelPerfEvent(PerfEvent event) {
    mCanceledPerfEvents.add(event);
  }

  @Override
  public void logPerfEvent(PerfEvent event) {
    mLoggedPerfEvents.add(event);
  }

  public List<PerfEvent> getLoggedPerfEvents() {
    return mLoggedPerfEvents;
  }

  public List<PerfEvent> getCanceledPerfEvents() {
    return mCanceledPerfEvents;
  }

  public void reset() {
    mLoggedPerfEvents.clear();
  }

  @Nullable
  @Override
  public Map<String, String> getExtraAnnotations(TreeProps treeProps) {
    return null;
  }
}
