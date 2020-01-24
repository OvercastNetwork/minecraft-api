package tc.oc.minecraft.api.text;

import com.google.inject.Binder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.multibindings.Multibinder;

/**
 * Register {@link TextRenderer}s to process all outgoing text from the server,
 * for as long as the owning plugin is enabled.
 */
public class TextRendererBinder {

    private final Multibinder<TextRenderer> renderers;

    public TextRendererBinder(Binder binder) {
        this.renderers = Multibinder.newSetBinder(binder, TextRenderer.class);
    }

    public LinkedBindingBuilder<TextRenderer> bindRenderer() {
        return renderers.addBinding();
    }
}
