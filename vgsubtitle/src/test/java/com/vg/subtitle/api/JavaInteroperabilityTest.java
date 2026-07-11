package com.vg.subtitle.api;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import android.content.Context;
import com.vg.subtitle.api.config.SubtitleConfig;
import com.vg.subtitle.api.listener.SubtitleListener;
import com.vg.subtitle.api.model.SubtitleProgress;
import com.vg.subtitle.api.model.Segment;
import com.vg.subtitle.api.exception.VGSubtitleException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import androidx.test.core.app.ApplicationProvider;

@RunWith(RobolectricTestRunner.class)
public class JavaInteroperabilityTest {

    @Test
    public void testEngineInitialization() {
        Context context = ApplicationProvider.getApplicationContext();
        VGSubtitleEngine engine = new VGSubtitleEngine(context);
        assertNotNull(engine);
    }

    @Test
    public void testSubtitleListenerImplementation() {
        SubtitleListener listener = new SubtitleListener() {
            @Override public void onProgress(SubtitleProgress progress) {}
            @Override public void onSubtitle(Segment segment) {}
            @Override public void onCompleted(String outputPath) {}
            @Override public void onCancelled() {}
            @Override public void onError(VGSubtitleException error) {}
        };
        assertNotNull(listener);
    }

    @Test
    public void testConfigBuilder() {
        SubtitleConfig config = new SubtitleConfig();
        assertNotNull(config);
    }
}
