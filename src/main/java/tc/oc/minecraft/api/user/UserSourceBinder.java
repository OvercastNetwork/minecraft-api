package tc.oc.minecraft.api.user;

import com.google.inject.Binder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.multibindings.Multibinder;

public class UserSourceBinder {

    private final Multibinder<UserSource> sources;

    public UserSourceBinder(Binder binder) {
        this.sources = Multibinder.newSetBinder(binder, UserSource.class);
    }

    public LinkedBindingBuilder<UserSource> addBinding() {
        return sources.addBinding();
    }
}
